package com.luxin.qimo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.luxin.bean.Meizi;
import com.luxin.fragment.MeiziViewFragment;

import java.util.List;

/**
 * Created by luxin on 16-1-1.
 */
public class MeiziViewActivity extends AppCompatActivity {

    private final static String TAG="MeiziViewActivity";

    private ViewPager viewPager;
    private List<Meizi> mDatas;
    private int pos;

    private MyPagerAdapter adapter;

    public static boolean isRefresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_meizi_viewpager);
        isRefresh=true;
        supportPostponeEnterTransition();
        mDatas = MeiziActivity.mDatas;
        Log.e(TAG, "----mDatas---" + mDatas.size());
        pos = getIntent().getIntExtra("pos", 0);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.lxw_id_meizi_viewpager);
        adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
    }


    private class MyPagerAdapter extends FragmentStatePagerAdapter{


        public MyPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return MeiziViewFragment.newMeiziFragment(mDatas.get(position).getUrl(),position==pos);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }


    }
}
