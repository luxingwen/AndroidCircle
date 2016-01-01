package com.luxin.qimo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.GridView;

import com.luxin.adapter.MeiziAdapter;
import com.luxin.bean.Meizi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxin on 16-1-1.
 */
public class MeiziActivity extends AppCompatActivity {
    private static String url = "http://gank.avosapps.com/api/data/%E7%A6%8F%E5%88%A9/20/1";
    private String TAG = "MeiziActivity";

    private MeiziAdapter adapter;
    private RecyclerView recyclerView;

    private List<Meizi> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_meizi);
        initView();
        new Thread() {
            @Override
            public void run() {
                String result = getData();
                toMeizi(result);
                mHandler.sendEmptyMessage(0x111);
            }
        }.start();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.lxw_id_meizi_gridview);
        mDatas = new ArrayList<Meizi>();
        adapter = new MeiziAdapter(this, mDatas);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
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
                mDatas.add(meizi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getData() {
        InputStream is = null;
        ByteArrayOutputStream byos = null;
        String result = "";
        try {
            URL u = new URL(url);
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
}
