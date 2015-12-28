package com.luxin.qimo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.luxin.adapter.MessageAdapter;
import com.luxin.bean.Helps;
import com.luxin.bean.MyUser;
import com.luxin.bean.NotifyMsg;
import com.luxin.util.Constant;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by luxin on 15-12-23.
 * http://luxin.gitcafe.io
 */
public class MessageActivity extends AppCompatActivity {


    private final static String TAG="MessageActivity";

    private ListView listView;

    private MessageAdapter adapter;

    private List<NotifyMsg> mItemData;

    private MaterialRefreshLayout materialRefreshLayout;

    private RefreshType mType=RefreshType.LOADMORE;

    private MyUser myUser;

    private int numPage;

    private enum RefreshType{
        REFRESH,LOADMORE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        numPage=0;
        myUser= BmobUser.getCurrentUser(this,MyUser.class);
        initView();
        initData();
        initEvent();

    }


    private void initView() {
        listView = (ListView) findViewById(R.id.lxw_id_message_listview);
        materialRefreshLayout= (MaterialRefreshLayout) findViewById(R.id.lxw_id_message_refresh);
    }

    private void initEvent() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mType = RefreshType.REFRESH;
                query(0);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                mType = RefreshType.LOADMORE;
                query(numPage);
            }
        });
    }

    private void initData() {
        mItemData=new ArrayList<NotifyMsg>();
        adapter=new MessageAdapter(this,mItemData);
        listView.setAdapter(adapter);
        if(mItemData.size()==0){
            mType=RefreshType.REFRESH;
            query(0);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Helps helps = mItemData.get(position).getHelps();
                Intent intent = new Intent(MessageActivity.this, HelpsCommentActivity.class);
                intent.putExtra("helps", helps);
                startActivity(intent);
                if(!mItemData.get(position).isStatus()){
                    updateMessageStatus(position);
                }
            }
        });
    }

    /**
     *
     * @param position
     */
    private void updateMessageStatus(int position) {
        NotifyMsg msg=mItemData.get(position);
        msg.setStatus(true);
        msg.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG,"===update success ===");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG,"===update  error==="+s);
            }
        });
    }

    private void query(int page) {
        BmobQuery<NotifyMsg> query=new BmobQuery<NotifyMsg>();
        String objectId=myUser.getObjectId();
        query.setLimit(Constant.NUMBER_PAGER);
        query.order("-createdAt");
        query.setSkip(Constant.NUMBER_PAGER * page);
        query.addWhereEqualTo("user", myUser);
        query.include("author,helps,user,comment,helps.phontofile");
        query.findObjects(this, new FindListener<NotifyMsg>() {
            @Override
            public void onSuccess(List<NotifyMsg> list) {
                if(list.size()>0){
                    if(mType==RefreshType.REFRESH){
                        mItemData.clear();
                        numPage=0;
                    }
                    numPage++;
                    mItemData.addAll(list);
                    adapter.notifyDataSetChanged();
                    materialRefreshLayout.finishRefresh();
                    materialRefreshLayout.finishRefreshLoadMore();
                }else if(RefreshType.REFRESH==mType){
                    materialRefreshLayout.finishRefresh();
                }else if(RefreshType.LOADMORE==mType){
                    materialRefreshLayout.finishRefreshLoadMore();
                }else {
                    numPage--;
                }
            }

            @Override
            public void onError(int i, String s) {
                materialRefreshLayout.finishRefresh();
                materialRefreshLayout.finishRefreshLoadMore();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       // query(numPage-1);
    }
}
