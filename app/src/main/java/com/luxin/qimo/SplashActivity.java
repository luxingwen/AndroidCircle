package com.luxin.qimo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.luxin.util.Constant;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/**
 * Created by luxin on 15-12-14.
 * http://luxin.gitcafe.io
 */
public class SplashActivity extends AppCompatActivity {
    private final static Long TIME = 2000L;

    private int []imgs={R.drawable.api_guide_1,R.drawable.api_guide_2,R.drawable.api_guide_3};

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_splash);
        imageView= (ImageView) findViewById(R.id.lxw_id_splash_img);
        imageView.setImageResource(imgs[((int) ((Math.random()*imgs.length)))]);
        Bmob.initialize(this, Constant.KEY);
        BmobInstallation.getCurrentInstallation(this).save();
        BmobPush.startWork(this,Constant.KEY);
        redirectByTime();
    }

    private void redirectByTime() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, TIME);
    }
}
