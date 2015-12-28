package com.api.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-12.
 */
public class ApiProgressBar extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private int mProgressState=0;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==0x110){
               mProgressBar.setProgress(mProgressState);
           }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_progressbar);
        mProgressBar= (ProgressBar) findViewById(R.id.api_id_progressbar_hor);
        new Thread(){
            @Override
            public void run() {
              while(mProgressState<100){
                  int a= (int) (Math.random()*10);
                  mProgressState+=a;
                  try {
                      sleep(1000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  mHandler.sendEmptyMessage(0x110);
              }
            }
        }.start();
    }
}
