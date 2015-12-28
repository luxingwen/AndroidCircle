package com.luxin.qimo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.luxin.adapter.LookImgAdapter;
import com.luxin.bean.Helps;
import com.luxin.bean.PhontoFiles;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by luxin on 15-12-17.
 *  http://luxin.gitcafe.io
 */
public class LookImageViewPagerActivity extends AppCompatActivity {
    private final static String TAG="LookImageViewPagerActivity";

    private ViewPager viewPager;

    private List<BmobFile> mData;
    private int position;
    private LookImgAdapter adapter;

    private TextView pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_look_image);
        initView();
        initData();
        setData();
    }



    private void initData() {
      //  helps= (Helps) getIntent().getSerializableExtra("helps");
        PhontoFiles phontoFiles= (PhontoFiles) getIntent().getSerializableExtra("phontoFiles");
        mData=phontoFiles.getPhotos();
        position=getIntent().getIntExtra("position", 0);

        pos.setText(position+1+"/"+mData.size());
    }

    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.lxw_id_look_image_viewpager);
        pos= (TextView) findViewById(R.id.lxw_id_look_img_position);
    }

    private void setData() {
        adapter=new LookImgAdapter(this,mData);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);

        final int size=mData.size();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pos.setText(position+1+"/"+size);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
