package com.luxin.qimo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.luxin.adapter.MeiziAdapter;
import com.luxin.bean.Meizi;
import com.luxin.util.ToastUtil;
import com.luxin.view.MeiziSpeceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by luxin on 16-1-1.
 */
public class MeiziActivity extends AppCompatActivity {
    private static String url = "http://gank.avosapps.com/api/data/%E7%A6%8F%E5%88%A9/20/";
    private String TAG = "MeiziActivity";

    private MeiziAdapter adapter;
    private RecyclerView recyclerView;

    public static List<Meizi> mDatas = new ArrayList<Meizi>();

    private ProgressDialog progressDialog;

    private StaggeredGridLayoutManager layoutManager;


    private int page;

    private boolean isre=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_meizi);
        page = 1;
        progressDialog = ProgressDialog.show(this, null, "美女正在来的路上，请等待...");
        initView();
        new Thread() {
            @Override
            public void run() {
                String result = getData(page);
                toMeizi(result);
                mHandler.sendEmptyMessage(0x111);
            }
        }.start();
    }

    private void initView() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.lxw_id_meizi_gridview);
        adapter = new MeiziAdapter(this, mDatas);
        recyclerView.setLayoutManager(layoutManager);
        MeiziSpeceItemDecoration speceItemDecoration = new MeiziSpeceItemDecoration(8);
        recyclerView.addItemDecoration(speceItemDecoration);
        recyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new MeiziAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ToastUtil.show(MeiziActivity.this, "妹子" + position, Toast.LENGTH_SHORT);
                Intent intent = new Intent(MeiziActivity.this, MeiziViewActivity.class);
                intent.putExtra("pos", position);
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onScorlled();
                }
            }
        });
    }

    private void onScorlled() {

        int pos[] = new int[layoutManager.getSpanCount()];
        layoutManager.findLastVisibleItemPositions(pos);

        for (int position : pos) {
            if (position == layoutManager.getItemCount() - 1) {
                page++;
                Log.e(TAG, "-----last item");
                progressDialog.show();
                new Thread() {
                    @Override
                    public void run() {
                        String result = getData(page);
                        toMeizi(result);
                        mHandler.sendEmptyMessage(0x111);
                    }
                }.start();
            }
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    };


    public void toMeizi(String res) {

        try {
            JSONObject j = new JSONObject(res);
            JSONArray jsonArray = j.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Meizi meizi = new Meizi();

                Log.e(TAG, "----url---" + jsonObject.getString("url"));
                meizi.setUrl(jsonObject.getString("url"));
                Bitmap bitmap = getMeiziBitmap(meizi.getUrl());
                meizi.setWidth(bitmap.getWidth());
                meizi.setHeight(bitmap.getHeight());
                mDatas.add(meizi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Bitmap getMeiziBitmap(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(this).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public String getData(int page) {
        InputStream is = null;
        ByteArrayOutputStream byos = null;
        String result = "";
        try {
            URL u = new URL(url + page);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            is = conn.getInputStream();
            int len = -1;
            byte buf[] = new byte[128];
            byos = new ByteArrayOutputStream();

            while ((len = is.read(buf)) != -1) {
                byos.write(buf, 0, len);
            }
            byos.flush();
            result = new String(byos.toByteArray());
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byos != null) {
                try {
                    byos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (!MeiziViewActivity.isRefresh) {
//            page = 1;
//            mDatas.clear();
//            new Thread() {
//                @Override
//                public void run() {
//                    String result = getData(page);
//                    toMeizi(result);
//                    mHandler.sendEmptyMessage(0x111);
//                }
//            }.start();
//        }
//
//    }
//
//
//        @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
}
