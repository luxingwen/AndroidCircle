package com.api.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.luxin.qimo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by luxin on 15-12-11.
 */
public class ApiFrameLayout extends AppCompatActivity {

    private FrameLayout myFrame;
    private int imgs[]={R.drawable.s_1,R.drawable.s_2,R.drawable.s_3,R.drawable.s_4,R.drawable.s_5,R.drawable.s_6,R.drawable.s_7,R.drawable.s_8};
    private final static int API_DEFUALT_MSG=0x111;

    private int i=0;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==API_DEFUALT_MSG){
                i++;
                move(i%8);
            }
            super.handleMessage(msg);
        }
    };

    private void move(int i) {
                myFrame.setForeground(getResources().getDrawable(imgs[i]));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_framelayout);
        myFrame= (FrameLayout) findViewById(R.id.api_myframe);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(API_DEFUALT_MSG);
            }
        },0,200);
    }


}
