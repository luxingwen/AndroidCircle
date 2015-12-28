package com.luxin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.luxin.bean.PhontoFiles;
import com.luxin.qimo.R;
import com.luxin.util.ImageLoader;
import com.luxin.util.ImageSizeUtil;
import com.luxin.view.LxwImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by luxin on 15-12-17.
 *  http://luxin.gitcafe.io
 */
public class LookImgAdapter extends PagerAdapter {
    private Context mContext;
    private List<BmobFile> mData;
    private ArrayList<View> views;

    private ProgressBar progressBar;



    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           MyImageViews myImageViews= (MyImageViews) msg.obj;
            ImageView imageView=myImageViews.imageView;
            String path=myImageViews.path;
            progressBar.setVisibility(View.GONE);
            ImageLoader.getInstance(2, ImageLoader.Type.LIFO).loaderImage(path, imageView, true);

        }
    };

    public LookImgAdapter(Context context,List<BmobFile> list){
        this.mContext=context;
        this.mData=list;
        initViews();
    }

    private void initViews() {
        views=new ArrayList<View>();
        for (int i=0;i<mData.size();i++){
            View view=View.inflate(mContext, R.layout.lxw_item_viewpager_view,null);
            views.add(view);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view=views.get(position);
        final LxwImageView imageView= (LxwImageView) view.findViewById(R.id.lxw_id_item_viewpager_img);

        progressBar= (ProgressBar) view.findViewById(R.id.lxw_id_item_viewpager_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        final String path=mData.get(position).getUrl();


        /**
         * 开启新的线程，更新图片
         */
        new Thread(){
            @Override
            public void run() {
//               ImgSize imgSize= caculateImgeSize(path);
//                float scale=imgSize.height*1.0f/imgSize.width;
//                DisplayMetrics displayMetrics= mContext.getResources().getDisplayMetrics();
//                int withPix=displayMetrics.widthPixels;
//                int heightPix=displayMetrics.heightPixels;
//
//                int height= (int) (withPix*scale);
//                if(height<heightPix){
//                    height=heightPix;
//                }
//
//                ViewGroup.LayoutParams lp=imageView.getLayoutParams();
//                lp.width=withPix;
//                lp.height=height;

                Message message=Message.obtain();
                MyImageViews myImageViews=new MyImageViews();
                myImageViews.imageView=imageView;
                myImageViews.path=path;
                message.obj=myImageViews;
                mHandler.sendMessage(message);

            }
        }.start();

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    private ImgSize caculateImgeSize(String urlPath){
        InputStream is=null;

        try {
            URL url=new URL(urlPath);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(conn.getInputStream());
            is.mark(is.available());

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=false;
            Bitmap bitmap=BitmapFactory.decodeStream(is, null, options);
            int width=bitmap.getWidth();
            int height=bitmap.getHeight();
            ImgSize imgSize=new ImgSize();
            imgSize.width=bitmap.getWidth();
            imgSize.height=bitmap.getHeight();
            conn.disconnect();
            return imgSize;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
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



    private class MyImageViews{
        LxwImageView imageView;
        String path;
    }

    private class ImgSize{
        int width;
        int height;
    }
}
