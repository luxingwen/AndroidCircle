package com.luxin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luxin.bean.Meizi;
import com.luxin.qimo.R;
import com.luxin.view.MeiziImageView;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by luxin on 16-1-1.
 * http://luxin.gitcafe.io
 */
public class MeiziAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Meizi> mDatas;

    public MeiziAdapter(Context context, List<Meizi> list) {
        this.mContext = context;
        this.mDatas = list;
        inflater = LayoutInflater.from(context);
    }


    public interface OnItemClickListener{
        void OnItemClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lxw_item_meizi, null);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Meizi meizi = mDatas.get(position);
//
        holder.imageView.setOriginalSize(meizi.getWidth(),meizi.getHeight());
        Glide.with(mContext).load(meizi.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

        if(mOnItemClickListener!=null){
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.OnItemClick(holder.imageView,pos);
                }
            });
        }



    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    MeiziImageView imageView;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = (MeiziImageView) itemView.findViewById(R.id.lxw_id_item_meizi_img);
        // itemView.setOnClickListener();
    }


}
