package com.luxin.qimo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.luxin.bean.MyUser;
import com.luxin.util.ImageLoader;
import com.luxin.util.ToastUtil;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by luxin on 15-12-13.
 *  http://luxin.gitcafe.io
 */
public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "UserProfileActivity";

    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CODE = 2;


    private RelativeLayout Img;
    private ImageView userImg;
    private RelativeLayout username;

    private RelativeLayout sex;
    private TextView usernameText;
    private TextView sexText;

    private  TextView email;

    private RelativeLayout personality;
    private TextView personalityText;

    private Button btn;

    private boolean isChange = false;

    private String filePath = null;

    private MyUser myUser = null;

    private boolean isUsername = false;

    private boolean isChangeUsername = false;

    private ProgressDialog mProgressDialog;

    private boolean isChangeUserImgNo=true;

    private  MyUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_user_profile);


        getSupportActionBar().setTitle("个人资料");

        myUser = BmobUser.getCurrentUser(this, MyUser.class);

        user=new MyUser();
        initView();
        initEvent();

        initData();
    }

    private void initData() {
        if (myUser.getAuvter() != null) {
            Log.e(TAG, "===getAuvtero file url====" + myUser.getAuvter().getUrl());
            String auvterPath = "http://file.bmob.cn/" + myUser.getAuvter().getUrl();
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loaderImage(auvterPath, userImg, true);
        }
        Log.e(TAG,"====sex===="+myUser.getSex());
        if (myUser.getSex().equals(0)) {
            sexText.setText("男");
        } else {
            sexText.setText("女");
        }
        usernameText.setText(myUser.getUsername());
        email.setText(myUser.getEmail());

    }


    private void initView() {

        username = (RelativeLayout) findViewById(R.id.lxw_user_profile_username);
        usernameText = (TextView) findViewById(R.id.lxw_id_user_profile_username_text);

        email= (TextView) findViewById(R.id.lxw_id_user_profile_email);

        Img = (RelativeLayout) findViewById(R.id.lxw_user_profile_img);
        userImg = (ImageView) findViewById(R.id.lxw_id_user_profile_userimg);
        sex = (RelativeLayout) findViewById(R.id.lxw_user_profile_sex);
        sexText = (TextView) findViewById(R.id.lxw_id_user_profile_sex_text);

        personality = (RelativeLayout) findViewById(R.id.lxw_user_profile_personality);
        personalityText = (TextView) findViewById(R.id.lxw_user_profile_personality_text);

        btn = (Button) findViewById(R.id.lxw_user_profile_btn_save);


    }

    private void initEvent() {
        Img.setOnClickListener(this);
        username.setOnClickListener(this);
        sex.setOnClickListener(this);
        btn.setOnClickListener(this);
        personality.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lxw_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lxw_action_menu_logout) {
            BmobUser.logOut(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lxw_user_profile_img:
                takePic();
                break;
            case R.id.lxw_user_profile_sex:
                changeSex();
                break;
            case R.id.lxw_user_profile_username:
                isUsername = true;
                Intent intent = new Intent(this, EdiUserProfileActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.lxw_user_profile_btn_save:
                mProgressDialog = ProgressDialog.show(this, null, "正在保存，请稍后...");
                if (filePath != null) {
                    uploaderAvertor(filePath);
                } else {
                    updateProfile();
                }
                break;
            case R.id.lxw_user_profile_personality:
                isUsername = false;
                Intent intent1 = new Intent(this, EdiUserProfileActivity.class);
                startActivityForResult(intent1, REQUEST_CODE);
                break;
        }
    }

    private void takePic() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(Intent.createChooser(intent, ""), Crop.REQUEST_PICK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            Bundle bundle = data.getExtras();
            if (isUsername) {
                usernameText.setText(bundle.getString("username"));
                isChangeUsername = true;
            } else {
                personalityText.setText(bundle.getString("username"));
            }
            isChange = true;
            btnVisibility();
        } else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void handleCrop(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Log.e(TAG,"=====>Crop.getOutput(data)=====>"+Crop.getOutput(data));
            //   userImg.setImageURI(Crop.getOutput(data));
            filePath = Crop.getOutput(data).getPath();
            ImageLoader.getInstance(1, ImageLoader.Type.LIFO).loaderImage(filePath, userImg, false);
            Log.e(TAG, "===filePath====" + filePath);
            isChangeUserImgNo=false;
            isChange = true;
            btnVisibility();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void beginCrop(Uri data) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));
        Crop.of(data, destination).asSquare().start(this);
    }


    private void changeSex() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] sexs = {"男", "女"};
        AlertDialog alert = null;
        alert = builder.setTitle("请选择性别")
                .setItems(sexs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexText.setText(sexs[which]);
                        isChange = true;
                        btnVisibility();
                    }
                }).create();
        alert.show();
    }


    private void uploaderAvertor(String file) {
        File path = new File(file);
        Log.e(TAG, "=====uploade_avertor___success===>" + path.getAbsolutePath());
        final BmobFile bmobFile = new BmobFile(path);
        bmobFile.upload(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                user.setAuvter(bmobFile);
                Log.e(TAG, "=====uploade_avertor___success===>" + bmobFile.getUrl());
                updateProfile();
            }

            @Override
            public void onFailure(int i, String s) {

                Log.e(TAG, "=====uploade_avertor___onfailure===>" + s);
            }
        });
    }

    private void updateProfile() {
        String username = usernameText.getText().toString().trim();
        String sex = sexText.getText().toString().trim();
        String personality = personalityText.getText().toString().trim();
        int sexInt = 0;
        if (TextUtils.isEmpty(username)) {
            return;
        }
        if (sex.equals("男")) {
            sexInt = 0;
        } else {
            sexInt = 1;
        }


        if (isChangeUsername) {
            if(!username.equals(myUser.getUsername())){
                user.setUsername(username);
            }
        }
        user.setSex(sexInt);
        user.setPersonality(personality);
        user.update(this, myUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                mProgressDialog.dismiss();
                ToastUtil.show(UserProfileActivity.this,"信息更新成功",Toast.LENGTH_SHORT);
                btn.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int i, String s) {
                mProgressDialog.dismiss();
                ToastUtil.show(UserProfileActivity.this,"信息更新失败",Toast.LENGTH_SHORT);
                Log.e(TAG,"===faile==="+s);
            }
        });
    }

    private void btnVisibility() {
        if (isChange) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isChangeUserImgNo&&!isChange&&!isChangeUsername){
            initData();
        }
    }
}
