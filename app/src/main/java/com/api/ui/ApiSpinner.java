package com.api.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.luxin.qimo.R;

import java.util.ArrayList;

/**
 * Created by luxin on 15-12-25.
 * http://luxin.gitcafe.io
 */
public class ApiSpinner extends AppCompatActivity {
    private String[] data = {"英勇青铜", "不屈白银", "荣耀黄金", "华贵铂金", "璀璨钻石", "超凡大师", "最强王者"};

    private Spinner one;
    private Spinner two;

    private ArrayList<Hero> mDatas = null;

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_spinner);
        initView();
    }

    private void initView() {
        one = (Spinner) findViewById(R.id.api_id_spinner_one);
        two = (Spinner) findViewById(R.id.api_id_spinner_two);

        mDatas = new ArrayList<Hero>();

        mDatas.add(new Hero(R.drawable.api_lol_icon1, "迅捷斥候：提莫（Teemo）"));
        mDatas.add(new Hero(R.drawable.api_lol_icon2, "诺克萨斯之手：德莱厄斯（Darius）"));
        mDatas.add(new Hero(R.drawable.api_lol_icon3, "无极剑圣：易（Yi）"));
        mDatas.add(new Hero(R.drawable.api_lol_icon4, "德莱厄斯：德莱文（Draven）"));
        mDatas.add(new Hero(R.drawable.api_lol_icon5, "德邦总管：赵信（XinZhao）"));
        mDatas.add(new Hero(R.drawable.api_lol_icon6, "狂战士：奥拉夫（Olaf）"));
        // adapter=new SimpleAdapter(this,mDatas,R.layout.api_item_spinner,)
        adapter = new MyAdapter(this);
        two.setAdapter(adapter);

        one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ApiSpinner.this, "你的分段是" + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        two.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ApiSpinner.this, "你选择的英雄是" + mDatas.get(position).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private class MyAdapter implements SpinnerAdapter {

        private Context mContext;
        private LayoutInflater inflater;
        public MyAdapter(Context context){
            this.mContext=mContext;
            inflater=LayoutInflater.from(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view=inflater.inflate(R.layout.api_item_spinner,null);
            TextView textView= (TextView) view.findViewById(R.id.api_id_item_spinner_text);
            ImageView imageView= (ImageView) view.findViewById(R.id.api_id_item_spinner_img);
            textView.setText(mDatas.get(position).getName());
            imageView.setImageResource(mDatas.get(position).getIco());
            return view;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=inflater.inflate(R.layout.api_item_spinner,null);
            TextView textView= (TextView) view.findViewById(R.id.api_id_item_spinner_text);
            ImageView imageView= (ImageView) view.findViewById(R.id.api_id_item_spinner_img);
            textView.setText(mDatas.get(position).getName());
            imageView.setImageResource(mDatas.get(position).getIco());
            return view;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    private class Hero {
        private int ico;
        private String name;

        public Hero(int ico, String name) {
            this.ico = ico;
            this.name = name;
        }

        public int getIco() {
            return ico;
        }

        public void setIco(int ico) {
            this.ico = ico;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
