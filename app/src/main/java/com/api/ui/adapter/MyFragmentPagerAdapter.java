package com.api.ui.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.api.ui.ApiFragment;
import com.luxin.qimo.R;

/**
 * Created by luxin on 16-1-1.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Fragment1 fragment1=null;
    private Fragment2 fragment2=null;

    private Fragment3 fragment3=null;

    private Fragment4 fragment4=null;

    private static final int PAGECOUNT=4;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragment1=new Fragment1();
        fragment2=new Fragment2();
        fragment3=new Fragment3();
        fragment4=new Fragment4();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case ApiFragment.PAGER_ONE:
                fragment=fragment1;
                break;
            case ApiFragment.PAGER_TWO:
                fragment=fragment2;
                break;

            case ApiFragment.PAGER_THREE:
                fragment=fragment3;
                break;

            case ApiFragment.PAGER_FOUR:
                fragment=fragment4;
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGECOUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    private class  Fragment1 extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.api_item_fragment,container,false);
            TextView textView= (TextView) view.findViewById(R.id.api_id_item_fragment_text);
            textView.setText("第一个Fragment");
            return view;
        }
    }
    private class  Fragment2 extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.api_item_fragment,container,false);
            TextView textView= (TextView) view.findViewById(R.id.api_id_item_fragment_text);
            textView.setText("第二个Fragment");
            return view;
        }
    }
    private class  Fragment3 extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.api_item_fragment,container,false);
            TextView textView= (TextView) view.findViewById(R.id.api_id_item_fragment_text);
            textView.setText("第三个Fragment");
            return view;
        }
    }
    private class  Fragment4 extends Fragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.api_item_fragment,container,false);
            TextView textView= (TextView) view.findViewById(R.id.api_id_item_fragment_text);
            textView.setText("第四个Fragment");
            return view;
        }
    }
}
