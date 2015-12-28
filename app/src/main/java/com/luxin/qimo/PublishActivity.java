package com.luxin.qimo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bmob.BmobProFile;
import com.luxin.adapter.EmotionGridViewAdapter;
import com.luxin.adapter.EmotionPagerAdapter;
import com.luxin.adapter.ImageChoseAdapter;
import com.luxin.bean.Helps;
import com.luxin.bean.MyUser;
import com.luxin.bean.PhontoFiles;
import com.luxin.util.Expression;
import com.luxin.util.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by luxin on 15-12-10.
 *  http://luxin.gitcafe.io
 */
public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PublishActivity";

    private EditText ediContent;

    private HorizontalScrollView scrollPicContent;
    private LinearLayout layPicContent;

    private LinearLayout btnCamera;
    private LinearLayout btnEmotion;

    private LinearLayout btnSend;

    private ViewPager emojPager;

    private boolean isOpen = false;

    private ArrayList<GridView> mGridViews;

    private static final int REQUEST_CODE = 1;

    private ProgressDialog mProgressDialog;

    private List<String> filePhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_help);

        initView();
        initEmojGridview();
        initEvent();

    }

    private void initEmojGridview() {

        mGridViews = new ArrayList<GridView>();
        LayoutInflater inflater = LayoutInflater.from(this);
        mGridViews.clear();
        for (int i = 0; i < 6; i++) {
            final int j = i;
            GridView gridView = (GridView) inflater.inflate(R.layout.lxw_emoj_gridview, null, false);
            gridView.setAdapter(new EmotionGridViewAdapter(this, i));
            mGridViews.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position>0&&(position % 20 == 0 )|| (j == 5 && position == 5)) {
                        int selectionStart = ediContent.getSelectionStart();
                        String str = ediContent.getText().toString();
                        String strTemp = str.substring(0, selectionStart);
                        if (!TextUtils.isEmpty(str)) {
                            int i = strTemp.lastIndexOf("]");
                            if (i == strTemp.length() - 1) {
                                int j = strTemp.lastIndexOf("[");
                                ediContent.getEditableText().delete(j, selectionStart);
                            } else {
                                ediContent.getEditableText().delete(strTemp.length() - 1, selectionStart);
                            }
                        }
                    } else {
                        Log.e(TAG, "=====onItemClick===" + position);
                        String str = Expression.emojName[position + j * 20];
                        SpannableString spannableString = new SpannableString(str);
                        Log.e(TAG, "====Expression.getIdAsName(str)===" + Expression.getIdAsName(str));
                        Drawable drawable = PublishActivity.this.getResources().getDrawable(Expression.getIdAsName(str));
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                        spannableString.setSpan(imageSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        int cuors = ediContent.getSelectionStart();
                        ediContent.getText().insert(cuors, spannableString);
                    }
                }
            });
        }
    }

    private void initView() {

        ediContent = (EditText) findViewById(R.id.id_lxw_push_content);

        scrollPicContent = (HorizontalScrollView) findViewById(R.id.id_lxw_push_scrollPicContent);
        layPicContent = (LinearLayout) findViewById(R.id.id_lxw_push_layPicContent);

        btnCamera = (LinearLayout) findViewById(R.id.id_lxw_push_btn_btnCamera);
        btnEmotion = (LinearLayout) findViewById(R.id.id_lxw_push_btn_btnEmotion);
        btnSend = (LinearLayout) findViewById(R.id.btnSend);

        emojPager = (ViewPager) findViewById(R.id.id_lxw_push_emoj_viewpager);
    }

    private void initEvent() {
        btnCamera.setOnClickListener(this);
        btnEmotion.setOnClickListener(this);
        emojPager.setOnClickListener(this);
        btnSend.setOnClickListener(this);



        ediContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isOpen){
                    openKeyBoard();
                    isOpen=false;
                    showEmotion(isOpen);
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_lxw_push_btn_btnCamera:
                Intent intent = new Intent(this, ChoseImgActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.id_lxw_push_btn_btnEmotion:
                Log.e(TAG, "=============>emotion");
                if (isOpen) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }
                showEmotion(isOpen);
                break;
            case R.id.btnSend:
                openKeyBoard();
                pushHelp();
                break;
        }
    }

    private void showEmotion(boolean isOpen) {
        if (isOpen) {
          //  hidenkeyBoard();
            openKeyBoard();
            emojPager.setVisibility(View.VISIBLE);

            initEmotionUp();
        } else {
            emojPager.setVisibility(View.GONE);
        }
    }


    public void openKeyBoard() {

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hidenkeyBoard() {
        if(this.getCurrentFocus()!=null){
            ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    private void initEmotionUp() {
        Log.e(TAG, "======initEmotionUp=========");
        emojPager.setAdapter(new EmotionPagerAdapter(this, mGridViews));
        emojPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == resultCode) {
            Bundle datas = data.getExtras();
            // String imgs[]=datas.getStringArray("pics");
            refresUI();
        }
    }


    /**
     *
     */
    private void refresUI() {
        Set<String> Imgs = ImageChoseAdapter.mSelectImg;
        if (Imgs.size() == 0) {
            scrollPicContent.setVisibility(View.GONE);
            return;
        }
        if (Imgs.size() > 0) {
            if (layPicContent != null) {
                layPicContent.removeAllViews();
                scrollPicContent.setVisibility(View.VISIBLE);
            }
            for (String path : Imgs) {
                View itemView = LayoutInflater.from(PublishActivity.this).inflate(R.layout.lxw_item_publish_pic, null);
                ImageView img = (ImageView) itemView.findViewById(R.id.id_lxw_publish_pic_img);

                itemView.setTag(path);
                itemView.setOnClickListener(onPicTureClickListener);

                ImageLoader.getInstance(2, ImageLoader.Type.LIFO).loaderImage(path, img, false);
                if (layPicContent != null) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layPicContent.addView(itemView, lp);
                }
            }
        } else {
            if (scrollPicContent != null) {
                scrollPicContent.setVisibility(View.GONE);
            }
        }

    }


    private View.OnClickListener onPicTureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String path = v.getTag().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(PublishActivity.this);
            AlertDialog alert = builder.setMessage("确认删除图片？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ImageChoseAdapter.mSelectImg.remove(path);
                            for (int i = 0; i < layPicContent.getChildCount(); i++) {
                                View view = layPicContent.getChildAt(i);
                                if (view.getTag().toString().equals(path)) {
                                    layPicContent.removeView(view);
                                    break;
                                }
                            }
                            if (ImageChoseAdapter.mSelectImg == null || ImageChoseAdapter.mSelectImg.size() == 0) {
                                scrollPicContent.setVisibility(View.GONE);
                            }
                        }
                    }).create();
            alert.show();
        }
    };


    @Override
    protected void onRestart() {
        super.onRestart();
        refresUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresUI();
    }


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==0x110){
             //  mProgressDialog.dismiss();
           }
        }
    };

    private void pushHelp() {

        final String content = ediContent.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            return;
        }
        mProgressDialog = ProgressDialog.show(this, null, "正在上传");

        new Thread(){
            @Override
            public void run() {
                List<String> list = new ArrayList<String>(ImageChoseAdapter.mSelectImg);
                if (list != null && list.size() > 0) {
                    getCacheImgFiles(PublishActivity.this,list);
                    uploader(filePhotos, content);
                } else {
                    saveText(content);
                }
                mHandler.sendEmptyMessage(0x110);
            }
        }.start();


        // savePulish(title, content, list);

    }

    private void saveText(String content) {
        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
        Helps helps = new Helps();
        helps.setUser(user);
        helps.setContent(content);
        helps.setState(0);
        helps.save(this, new SaveListener() {
            @Override
            public void onSuccess() {

                mProgressDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                mProgressDialog.dismiss();
            }
        });
    }

    private void savePulish(String content, PhontoFiles files) {
        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
        Helps helps = new Helps();
        helps.setUser(user);
        helps.setContent(content);
        helps.setState(0);
        helps.setPhontofile(files);
        helps.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                finish();
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, "=====>savePulish===onFailure=>" + s);
                mProgressDialog.dismiss();
            }
        });
    }

    /**
     * 上传图片
     *
     * @param list
     * @return
     */
    private void uploader(List<String> list, final String content) {
        final PhontoFiles phontoFiles = new PhontoFiles();
        final PhontoFiles ps = new PhontoFiles();
        if (list.size() == 1) {
            File file = new File(list.get(0));
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    phontoFiles.setPhoto(bmobFile.getFileUrl(PublishActivity.this));
                    phontoFiles.save(PublishActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            //  phontoFiles.setObjectId(phontoFiles.getObjectId());
                            ps.setObjectId(phontoFiles.getObjectId());
                            Log.e(TAG, "hjkhjkh=======>" + phontoFiles.getObjectId());

                            savePulish(content, phontoFiles);
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.e(TAG, "=====>uploader===onFailure=>" + s);
                }
            });
        } else {
            String[] filePaths = list.toArray(new String[list.size()]);
            final int fileLength = filePaths.length;
//            Bmob.uploadBatch(this, filePaths, new UploadBatchListener() {
//                @Override
//                public void onSuccess(List<BmobFile> list, List<String> list1) {
//                    if(list1.size()==fileLength){
//                        phontoFiles.setPhotos(list);
//                        phontoFiles.save(PublishActivity.this, new SaveListener() {
//                            @Override
//                            public void onSuccess() {
//
//                              //  idAsClass(phontoFiles.getObjectId());
//                               // phontoFiles.setObjectId(phontoFiles.getObjectId());
//                                Log.e(TAG,"++++"+phontoFiles.getObjectId());
//                                ps.setObjectId(phontoFiles.getObjectId());
//                                savePulish(title,content,phontoFiles);
//                            }
//
//                            @Override
//                            public void onFailure(int i, String s) {
//
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onProgress(int i, int i1, int i2, int i3) {
//
//                }
//
//                @Override
//                public void onError(int i, String s) {
//                    Log.e(TAG,"=====>uploader===onError=>"+s);
//                }
//            });

            BmobProFile.getInstance(this).uploadBatch(filePaths, new com.bmob.btp.callback.UploadBatchListener() {
                @Override
                public void onSuccess(boolean b, String[] strings, String[] strings1, BmobFile[] bmobFiles) {
                    if (b) {
                        List<BmobFile> dataFiles = Arrays.asList(bmobFiles);
                        phontoFiles.setPhotos(dataFiles);
                        phontoFiles.save(PublishActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                savePulish( content, phontoFiles);
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {

                }
            });

        }

        Log.e(TAG, "===ps++++objectId" + ps.getObjectId());

    }


    /**
     * 获取缓存图片地址
     * @param context
     * @param list
     */
    private void getCacheImgFiles(Context context,List<String> list){
        filePhotos=new ArrayList<String>();
        for (String path:list){
            filePhotos.add(compressBitmap(context,path));
        }
    }


    /**
     *压缩指定路径图片，并将其保存在缓存目录中，并获取到缓存后的图片路径
     * @param context
     * @param path
     * @return
     */
    private String compressBitmap(Context context,String path){
        Bitmap bitmap=compressBitmapFromFile(path);
        File srcFile=new File(path);
        String desPath= getImageCacheDir(context)+srcFile.getName();
        File file=new File(desPath);
        try {
            FileOutputStream fos=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return desPath;
    }

    /**
     * 获取图片缓存路径
     * @param context
     * @return
     */
    private String getImageCacheDir(Context context) {
        String cachepath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            cachepath=context.getExternalCacheDir().getPath();
        }else{
            cachepath=context.getCacheDir().getPath();
        }
        return cachepath;
    }


    /**
     * 基于质量的压缩算法,保证图片大小小于200k
     * @param bitmap
     * @return
     */
    private Bitmap compressBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        int options=100;
        while(baos.toByteArray().length/1024>200){
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options-=10;
        }
        ByteArrayInputStream byins=new ByteArrayInputStream(baos.toByteArray());
        Bitmap bm=BitmapFactory.decodeStream(byins,null,null);
        return bm;
    }

    /**
     *压缩指定路径的图片，并得到图片对象
     * @param path
     * @return
     */
    private Bitmap compressBitmapFromFile(String path){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bitmap=BitmapFactory.decodeFile(path,options);

        options.inJustDecodeBounds=false;
        int width=options.outWidth;
        int height=options.outHeight;

        float widthRadio=480f;
        float heightRadio=800f;
        int inSampleSize=1;
        if(width>height&&width>widthRadio){
            inSampleSize= (int) (width*1.0f/widthRadio);
        }else if(width<height&&height>heightRadio){
            inSampleSize= (int) (height*1.0f/heightRadio);
        }
        if (inSampleSize<=0){
            inSampleSize=1;
        }
        options.inSampleSize=inSampleSize;
        bitmap=BitmapFactory.decodeFile(path,options);
        return compressBitmap(bitmap);
    }


    private void idAsClass(String objectId) {

    }
}
