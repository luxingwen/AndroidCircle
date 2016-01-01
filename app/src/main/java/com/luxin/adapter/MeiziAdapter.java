package com.luxin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luxin.bean.Meizi;
import com.luxin.qimo.R;
import com.luxin.util.ImageLoader;

import java.util.List;

/**
 * Created by luxin on 16-1-1.
 * http://luxin.gitcafe.io
 */
public class MeiziAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private Context mContext;
    private LayoutInflater inflater;
    private List<Meizi> mDatas;

    public MeiziAdapter(Context context,List<Meizi> list){
        this.mContext=context;
        this.mDatas=list;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.lxw_item_meizi,null);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.pictures_no);
        ImageLoader.getInstance(2, ImageLoader.Type.LIFO).loaderImage(mDatas.get(position).getUrl(),holder.imageView,true);
    }


}
class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    public MyViewHolder(View itemView) {
        super(itemView);
        imageView= (ImageView) itemView.findViewById(R.id.lxw_id_item_meizi_img);
    }
}
