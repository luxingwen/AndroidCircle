package com.api.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luxin.qimo.R;

import java.util.ArrayList;

/**
 * Created by luxin on 15-12-25.
 * http://luxin.gitcafe.io
 */
public class ApiExpandableListView extends AppCompatActivity {
    private ExpandableListView listView;
    private ArrayList<String> gData;
    private ArrayList<Item> IData;
    private ArrayList<ArrayList<Item>> iData;

    private MyBaseExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_expandablelistview);
        initView();
    }

    private void initView() {
        listView = (ExpandableListView) findViewById(R.id.api_id_expandableListView);

        gData = new ArrayList<String>();
        gData.add("AD");
        gData.add("AP");
        gData.add("TANK");

        IData = new ArrayList<Item>();
        IData.add(new Item(R.drawable.api_lol_icon14, "韦鲁斯"));
        IData.add(new Item(R.drawable.api_lol_icon13, "男枪"));
        IData.add(new Item(R.drawable.api_lol_icon3, "剑圣"));
        IData.add(new Item(R.drawable.api_lol_icon4, "德莱文"));

        iData=new ArrayList<ArrayList<Item>>();
        iData.add(IData);

        //AP组
        IData = new ArrayList<Item>();
        IData.add(new Item(R.drawable.api_lol_icon1, "提莫"));
        IData.add(new Item(R.drawable.api_lol_icon7, "安妮"));
        IData.add(new Item(R.drawable.api_lol_icon8, "天使"));
        IData.add(new Item(R.drawable.api_lol_icon9, "泽拉斯"));
        IData.add(new Item(R.drawable.api_lol_icon11, "狐狸"));
        iData.add(IData);

        //TANK组
        IData = new ArrayList<Item>();
        IData.add(new Item(R.drawable.api_lol_icon2, "诺手"));
        IData.add(new Item(R.drawable.api_lol_icon5, "德邦"));
        IData.add(new Item(R.drawable.api_lol_icon6, "奥拉夫"));
        IData.add(new Item(R.drawable.api_lol_icon9, "龙女"));
        IData.add(new Item(R.drawable.api_lol_icon12, "狗熊"));
        iData.add(IData);


        adapter=new MyBaseExpandableListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(ApiExpandableListView.this,"你点击了："+iData.get(groupPosition).get(childPosition).getName(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    private class Item {
        private int ico;
        private String name;

        public Item(int ico, String name) {
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

    private class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {


        private Context mContext;
        private LayoutInflater inflater;

        public MyBaseExpandableListAdapter(Context context) {
            this.mContext = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return gData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return iData.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return gData.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return iData.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.api_expanlist_group, null);
            TextView textView = (TextView) view.findViewById(R.id.api_id_expanlist_group_text);
            textView.setText(gData.get(groupPosition));
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.api_expanlist_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.api_expanlist_item_img);
            TextView textView = (TextView) view.findViewById(R.id.api_expanlist_item_text);
            imageView.setImageResource(iData.get(groupPosition).get(childPosition).getIco());
            textView.setText(iData.get(groupPosition).get(childPosition).getName());
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
