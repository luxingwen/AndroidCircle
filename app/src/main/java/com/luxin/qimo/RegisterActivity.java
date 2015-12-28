package com.luxin.qimo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.luxin.bean.MyUser;
import com.luxin.bean.MyUserInstallation;
import com.luxin.util.Constant;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by luxin on 15-12-13.
 *  http://luxin.gitcafe.io
 */
public class RegisterActivity extends AppCompatActivity {

    private final static String TAG="RegisterActivity";
    private EditText ediusername;
    private EditText ediemail;
    private EditText edipassword;
    private EditText edipasswordTwo;
    private Button btnReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("注册");

        initView();
        initEvent();
    }

    private void initView() {
        ediusername= (EditText) findViewById(R.id.lxw_id_reg_edi_username);
        ediemail= (EditText) findViewById(R.id.lxw_id_reg_edi_email);
        edipassword= (EditText) findViewById(R.id.lxw_id_reg_edi_password);
        edipasswordTwo= (EditText) findViewById(R.id.lxw_id_reg_edi_password_two);
        btnReg= (Button) findViewById(R.id.lxw_id_reg_btn_register);
    }

    private void initEvent() {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }



    private void register(){
        String username=ediusername.getText().toString().trim();
        String email=ediemail.getText().toString().trim();
        String password=edipassword.getText().toString().trim();
        String passwordTwo=edipasswordTwo.getText().toString().trim();
        ediusername.setError(null);
        ediemail.setError(null);
        edipassword.setError(null);
        edipasswordTwo.setError(null);
        if(TextUtils.isEmpty(username)){
            ediusername.setError("用户名不能为空");
            return ;
        }
        if(TextUtils.isEmpty(email)){
            ediemail.setError("请输入邮箱");
            return ;
        }
        if(!isEmailValidate(email)){
            ediemail.setError("这不是一个有效的邮箱");
            return ;
        }
        if(TextUtils.isEmpty(password)){
            edipassword.setError("密码不能为空");
            return ;
        }
        if(TextUtils.isEmpty(passwordTwo)){
            edipasswordTwo.setError("请再次输入一次密码");
            return ;
        }

        if(!password.equals(passwordTwo)){
            new AlertDialog.Builder(this).setMessage("两次输入的密码不一致").create().show();
            return ;
        }

        if(!isPasswordValidate(password)){
           new AlertDialog.Builder(this).setMessage("密码长度过短，最小7位以上").create().show();
            return ;
        }
        final MyUser user=new MyUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setScore(10);
        user.setSex(0);

        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {

                bindUserIdAndDriverice(user);
                Toast.makeText(RegisterActivity.this,"register--->success",Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent(Constant.ACTION_REGISTER_SUCCESS_FINISH));

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(RegisterActivity.this,"register--->failure",Toast.LENGTH_SHORT).show();
                Log.e(TAG,"===register===failure:---"+s);
            }
        });
    }

    /**
     将用户与设备绑定起来
     * @param user
     */
    private void bindUserIdAndDriverice(final MyUser user) {

        Log.e(TAG,"===user  objectid==="+user.getObjectId());
        BmobQuery<MyUserInstallation> query=new BmobQuery<MyUserInstallation>();
        query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(this));
        query.findObjects(this, new FindListener<MyUserInstallation>() {
            @Override
            public void onSuccess(List<MyUserInstallation> list) {
                if (list.size() > 0) {
                    MyUserInstallation myUserInstallation=list.get(0);
                    myUserInstallation.setUid(user.getObjectId());
                    myUserInstallation.update(RegisterActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Log.e(TAG,"===设备信息更新成功===");
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e(TAG,"===设备信息更新失败:"+s);
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }



    private boolean isPasswordValidate(String password){
        return password.length()>6;
    }

    private boolean isEmailValidate(String email){
        return email.contains("@");
    }
}
