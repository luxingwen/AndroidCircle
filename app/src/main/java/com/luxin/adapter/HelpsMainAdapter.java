package com.luxin.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luxin.bean.Helps;
import com.luxin.bean.MyUser;
import com.luxin.bean.PhontoFiles;
import com.luxin.qimo.LookImageActivity;
import com.luxin.qimo.LookImageViewPagerActivity;
import com.luxin.qimo.R;
import com.luxin.util.Expression;
import com.luxin.util.ImageLoader;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by luxin on 15-12-15.
 *  http://luxin.gitcafe.io
 */
public class HelpsMainAdapter extends BaseAdapter {

    private final static String TAG = "HelpsMainAdapter";

    private Context mContext;
    private List<Helps> mData;
    private LayoutInflater inflater;




    public HelpsMainAdapter(Context context, List<Helps> list) {
        this.mContext = context;
        this.mData = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lxw_item_helps, null);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.lxw_id_item_helps_include_avertor);
            holder.userimg = (ImageView) holder.linearLayout.findViewById(R.id.lxw_id_item_helps_userimg);
            holder.username = (TextView) holder.linearLayout.findViewById(R.id.lxw_id_item_helps_username);
            holder.personality = (TextView) holder.linearLayout.findViewById(R.id.lxw_id_item_helps_user_personality);
            holder.creatTime = (TextView) holder.linearLayout.findViewById(R.id.lxw_id_item_helps_create_time);

            holder.content = (TextView) convertView.findViewById(R.id.lxw_id_item_helps_content);

            holder.frameLayout = (FrameLayout) convertView.findViewById(R.id.lxw_id_item_helps_include_img);
            holder.gridView = (GridView) holder.frameLayout.findViewById(R.id.lxw_id_item_helps_gridview);
            holder.contentImg = (ImageView) holder.frameLayout.findViewById(R.id.lxw_id_item_helps_content_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.userimg.setImageResource(R.drawable.github);
        holder.contentImg.setImageResource(R.drawable.pictures_no);


        holder.contentImg.setVisibility(View.GONE);
        holder.gridView.setVisibility(View.GONE);
        holder.contentImg.setFocusable(false);
        holder.gridView.setFocusable(false);

        Helps helps = mData.get(position);
        Log.e(TAG, "===helps=====createAt=" + helps.getCreatedAt());

        MyUser myUser = helps.getUser();

        if (myUser == null) {
            Log.e(TAG, "====myUser is null===");
        }

        if (myUser.getAuvter() != null) {
            String auvterPath = "http://file.bmob.cn/" + myUser.getAuvter().getUrl();
            //  Log.e(TAG,"===auvter url===="+myUser.getAuvter().getUrl());

            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loaderImage(auvterPath, holder.userimg, true);
        }

        holder.username.setText(myUser.getUsername());
        holder.personality.setText(myUser.getPersonality());
        holder.creatTime.setText(getCreateTimes(helps.getCreatedAt()));

        SpannableString spannableString=getSpannableString(helps.getContent(),mContext);
        holder.content.setText(spannableString);

        PhontoFiles phontoFiles = helps.getPhontofile();
        if (phontoFiles != null) {
            setImages(helps, phontoFiles, holder.frameLayout, holder.gridView, holder.contentImg);
        } else {
            Log.e(TAG, "===phontofiles is null");
        }

        return convertView;
    }

    /**
     * 设置图片
     *
     * @param phontoFiles
     * @param frameLayout
     * @param gridView
     * @param contentImg
     */
    private void setImages(final Helps helps, PhontoFiles phontoFiles, FrameLayout frameLayout, GridView gridView, ImageView contentImg) {
        List<BmobFile> list = phontoFiles.getPhotos();
        String path = phontoFiles.getPhoto();
        if (list != null && list.size() > 1) {

            for (BmobFile file : list) {
                Log.e(TAG, "====list----bmobfile===" + file.getUrl());
            }

            frameLayout.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.VISIBLE);
            contentImg.setVisibility(View.GONE);

            gridView.setAdapter(new GridViewHelpsAdapter(mContext, list));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, LookImageViewPagerActivity.class);
                    intent.putExtra("phontoFiles", helps.getPhontofile());
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);
                }
            });

        } else if (path != null) {
            Log.e(TAG, "===path url====" + path);
            frameLayout.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            contentImg.setVisibility(View.VISIBLE);
            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loaderImage(path, contentImg, true);
            contentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LookImageActivity.class);
                    intent.putExtra("helps", helps);
                    mContext.startActivity(intent);
                }
            });
        } else {
            frameLayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            contentImg.setVisibility(View.GONE);
        }
    }

    private class ViewHolder {
        LinearLayout linearLayout;
        ImageView userimg;
        TextView username;
        TextView personality;
        TextView creatTime;

        TextView content;

        FrameLayout frameLayout;
        GridView gridView;
        ImageView contentImg;
    }

    //createAt=2015-12-17 15:26:45

    private String getCreateTimes(String dates) {
        Date old = toDate(dates);
        Date nowtime = new Date(System.currentTimeMillis());
        long values = nowtime.getTime() - old.getTime();
        values=values/1000;
        Log.e(TAG, "====values  time===" + values);
        if (values < 60 && values > 0) {
            return  values + "秒前";
        }
        if (values > 60 && values < 60 * 60) {
            return values/60 + "分钟前";
        }
        if (values < 60 * 60 * 24 && values > 60 * 60) {
            return values/3600+"小时前";
        }
        if(values<60*60*24*2&&values>60*60*24){
            return "昨天";
        }
        if (values < 60 * 60 * 3 * 24 && values > 60 * 60 * 24*2) {
            return  "前天";
        }
        if (values < 60 * 60 * 24 * 30 && values > 60 * 60 * 24 * 3) {
            return  values / (60 * 60 * 24 ) + "天前";
        }
        if (values < 60 * 60 * 24 * 365 && values > 60 * 60 * 24 * 30) {
           return nowtime.getMonth()-old.getMonth() + "个月前";
        }
        return  values / (60 * 60 * 24 * 30 * 365) + "年前";
    }

    private Date toDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        Date date1 = null;
        try {
            date1 = format.parse(date);
            //  Log.e(TAG,"===date==="+date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }


    /**
     * 替换表情
     * @param str
     * @param context
     * @return
     */
    private SpannableString getSpannableString(String str,Context context){
        SpannableString spannableString=new SpannableString(str);
        String s="\\[(.+?)\\]";
        Pattern pattern=Pattern.compile(s,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(spannableString);
        while(matcher.find()){
            String key=matcher.group();
            int id= Expression.getIdAsName(key);
            if(id!=0){
                Drawable drawable=context.getResources().getDrawable(id);
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                ImageSpan imageSpan=new ImageSpan(drawable);
                spannableString.setSpan(imageSpan,matcher.start(),matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
}
