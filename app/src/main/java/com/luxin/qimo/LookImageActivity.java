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

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mProgressBar.setVisibility(View.GONE);
            MyImg myImg= (MyImg) msg.obj;
            ImageView imageView=myImg.imageView;
            String path=myImg.path;
            ImageLoader.getInstance(1, ImageLoader.Type.LIFO).loaderImage(path,imageView,true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_item_viewpager_view);
        initView();
        initData();
        setData();
    }

    private void setData() {

        new Thread(){
            @Override
            public void run(){
                mProgressBar.setVisibility(View.VISIBLE);
                ImgSize imgSize=caculaterImgSize(imgpath);
                float scale=imgSize.height*1.0f/imgSize.width;
                ViewGroup.LayoutParams lp=imageView.getLayoutParams();
                DisplayMetrics displayMetrics= LookImageActivity.this.getResources().getDisplayMetrics();
                int screenWidthPixs=displayMetrics.widthPixels;
                int screenHeightPixs=displayMetrics.heightPixels;
                int height= (int) (screenWidthPixs*scale);
                if(height<screenHeightPixs){
                    height=screenHeightPixs;
                }
                lp.width=screenWidthPixs;
                lp.height=height;

                Message message=Message.obtain();
                MyImg myImg=new MyImg();
                myImg.imageView=imageView;
                myImg.path=imgpath;
                message.obj=myImg;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private ImgSize caculaterImgSize(String imgpath) {
        InputStream is=null;
        try {
            URL url=new URL(imgpath);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=false;
            Bitmap bitmap=BitmapFactory.decodeStream(is,null,options);
            int width=bitmap.getWidth();
            int height=bitmap.getHeight();
            ImgSize imgSize=new ImgSize();
            imgSize.width=width;
            imgSize.height=height;
            conn.disconnect();
            return imgSize;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void initView() {
        imageView= (ImageView) findViewById(R.id.lxw_id_item_viewpager_img);
        mProgressBar= (ProgressBar) findViewById(R.id.lxw_id_item_viewpager_progressbar);
    }
    private void initData() {
        Helps helps= (Helps) this.getIntent().getSerializableExtra("helps");
        imgpath=helps.getPhontofile().getPhoto();
    }

    private class ImgSize{
        int width;
        int height;
    }

    private class MyImg{
        ImageView imageView;
        String path;
    }


}
