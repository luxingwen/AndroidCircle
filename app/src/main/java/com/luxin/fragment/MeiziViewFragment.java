package com.luxin.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.luxin.qimo.MeiziViewActivity;
import com.luxin.qimo.R;
import com.luxin.view.TouchImageView;

/**
 * Created by luxin on 16-1-2.
 * http://luxin.gitcafe.io
 */
public class MeiziViewFragment extends Fragment  implements RequestListener<String,GlideDrawable>{

    private String url;
    private boolean isShow;

    private MeiziViewActivity activity;
    private TouchImageView imageView;

    public static MeiziViewFragment newMeiziFragment(String url,boolean isshow){
        Bundle bundle=new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isshow", isshow);

        MeiziViewFragment fragment=new MeiziViewFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity= (MeiziViewActivity) getActivity();
        url=getArguments().getString("url");
        isShow=getArguments().getBoolean("isshow",false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lxw_item_meizi_viewpager,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        imageView= (TouchImageView) view.findViewById(R.id.lxw_id_item_meizi_viewpager_img);
        ViewCompat.setTransitionName(imageView,url);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                AlertDialog alert=builder.setMessage("what are you fuck doing?")
                                   .create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade(0)
                .listener(this)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

        maybeStartPostponedEnterTransition();
        return true;
    }

    private void maybeStartPostponedEnterTransition() {
        if(isShow){
            activity.supportPostponeEnterTransition();
        }
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

       imageView.setImageDrawable(resource);
        maybeStartPostponedEnterTransition();
        return true;
    }
}
