package com.luxin.qimo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luxin.bean.Helps;
import com.luxin.util.ImageLoader;
import com.luxin.util.ImageSizeUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by luxin on 15-12-17.
 *  http://luxin.gitcafe.io
 */
public class LookImageActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private ImageView imageView;
    private String imgpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_item_viewpager_view);
        initView();
        initData();
        setData();
    }

    private void setData() {
        Glide.with(this).load(imgpath).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }



    private void initView() {
        imageView= (ImageView) findViewById(R.id.lxw_id_item_viewpager_img);
        mProgressBar= (ProgressBar) findViewById(R.id.lxw_id_item_viewpager_progressbar);
    }
    private void initData() {
        Helps helps= (Helps) this.getIntent().getSerializableExtra("helps");
        imgpath=helps.getPhontofile().getPhoto();
    }




}
