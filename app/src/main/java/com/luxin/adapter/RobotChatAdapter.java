package com.luxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luxin.bean.MyUser;
import com.luxin.bean.RobotChat;
import com.luxin.qimo.R;
import com.luxin.util.Constant;
import com.luxin.util.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by luxin on 15-12-25.
 * http://luxin.gitcafe.io
 */
public class RobotChatAdapter extends BaseAdapter {
    private Context mContext;
    private List<RobotChat> mData;
    private LayoutInflater inflater;
    private MyUser user;

    public RobotChatAdapter(Context context,List<RobotChat> list,MyUser user){
        this.mContext=context;
        this.mData=list;
        this.user=user;
        inflater=LayoutInflater.from(context);
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
    public int getItemViewType(int position) {
        RobotChat robotChat=mData.get(position);
        if(robotChat.getType()== RobotChat.ChatType.INCOMING){
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            if(getItemViewType(position)==0){
                convertView=inflater.inflate(R.layout.lxw_robot_from_msg,null);
                holder=new ViewHolder();
                holder.msg= (TextView) convertView.findViewById(R.id.lxw_id_robot_from_msg);
                holder.userimg= (CircleImageView) convertView.findViewById(R.id.lxw_id_robot_from_userimg);
            }else {
                convertView=inflater.inflate(R.layout.lxw_robot_to_msg,null);
                holder=new ViewHolder();
                holder.msg= (TextView) convertView.findViewById(R.id.lxw_id_robot_to_msg);
                holder.userimg= (CircleImageView) convertView.findViewById(R.id.lxw_id_robot_to_userimg);

            }
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.userimg.setImageResource(R.drawable.github);
        if(mData.get(position).getType()== RobotChat.ChatType.INCOMING){
            holder.userimg.setImageResource(R.drawable.shuaishuai);
        }else {
            if(user.getAuvter()!=null){
                Glide.with(mContext).load(Constant.USERIMG+user.getAuvter().getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.userimg);
            }
        }
        holder.msg.setText(mData.get(position).getMsg());
        return convertView;
    }


    private class ViewHolder{
        TextView msg;
        CircleImageView userimg;
    }
}
