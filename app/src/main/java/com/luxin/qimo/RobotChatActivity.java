package com.luxin.qimo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.luxin.adapter.RobotChatAdapter;
import com.luxin.bean.MyUser;
import com.luxin.bean.RobotChat;
import com.luxin.util.RobotHttpUtils;
import com.luxin.util.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by luxin on 15-12-25.
 * http://luxin.gitcafe.io
 */
public class RobotChatActivity extends AppCompatActivity {

    private ListView listView;
    private RobotChatAdapter adapter;
    private List<RobotChat> mDatas;


    private EditText editText;
    private Button btnsend;

    private MyUser user;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            RobotChat robotChat= (RobotChat) msg.obj;
            mDatas.add(robotChat);
            listView.setSelection(mDatas.size()-1);
            adapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_robot_chat);
        initView();
        initDatas();
        initEvent();
    }


    private void initDatas() {
        mDatas=new ArrayList<RobotChat>();
        RobotChat robotChat=new RobotChat();
        robotChat.setMsg("我是甩甩，甩甩是机器人喔");
        robotChat.setDate(new Date(System.currentTimeMillis()));
        robotChat.setType(RobotChat.ChatType.INCOMING);
        mDatas.add(robotChat);

        user= BmobUser.getCurrentUser(this,MyUser.class);
        adapter=new RobotChatAdapter(this,mDatas,user);
        listView.setAdapter(adapter);
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.lxw_id_robot_chat_listview);
        editText= (EditText) findViewById(R.id.lxw_id_robot_chat_edi);
        btnsend= (Button) findViewById(R.id.lxw_id_robot_chat_btnsend);


    }

    private void initEvent() {
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tomsg=editText.getText().toString().trim();
                if(TextUtils.isEmpty(tomsg)){
                    ToastUtil.show(RobotChatActivity.this,"输入的内容不能为空", Toast.LENGTH_SHORT);
                    return;
                }


                RobotChat toMsg=new RobotChat();
                toMsg.setMsg(tomsg);
                toMsg.setType(RobotChat.ChatType.OUTCOMING);
                toMsg.setDate(new Date(System.currentTimeMillis()));
                mDatas.add(toMsg);
                listView.setSelection(mDatas.size()-1);
                adapter.notifyDataSetChanged();
                editText.setText("");
                new Thread(){
                    @Override
                    public void run() {
                      RobotChat fromMsg= RobotHttpUtils.sendMessage(tomsg);
                        Message message=Message.obtain();
                        message.obj=fromMsg;
                        mHandler.sendMessage(message);
                    }
                }.start();
            }
        });
    }


}
