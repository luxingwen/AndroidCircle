package com.api.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-26.
 * http://luxin.gitcafe.io
 */
public class ApiAlertDialog extends AppCompatActivity {
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_alertdialog);
        initView();
    }

    private void initView() {
        one = (Button) findViewById(R.id.api_id_alertdialog_one);
        two = (Button) findViewById(R.id.api_id_alertdialog_two);
        three = (Button) findViewById(R.id.api_id_alertdialog_three);
        four = (Button) findViewById(R.id.api_id_alertdialog_four);
        five = (Button) findViewById(R.id.api_id_alertdialog_five);
        six= (Button) findViewById(R.id.api_id_alertdialog_six);
    }

    public void one(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert = builder.setTitle("系统提示!")
                .setMessage("这是一个最普通的AlertDialog.\n带有三个按钮，分别是取消，中立和确定")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("取消");
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("确定");
                    }
                })
                .setNeutralButton("中立", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("中立");
                    }
                }).create();
        alert.show();
    }

    public void two(View view) {
        final String[] os = {"Windows", "Linux", "IOS", "FreeBSD", "Unix"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert = builder.setTitle("选择你喜欢的操作系统")
                .setItems(os, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("你选择了" + os[which]);
                    }
                }).create();
        alert.show();

    }

    public void three(View view) {
        final String[] language = {"c", "c++", "java", "php", "C#", "JavaScript", "Go", "Swift"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert = builder.setTitle("选择你喜欢的编程语言，只能选择一种喔~")
                .setSingleChoiceItems(language, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("你选择了" + language[which]);
                    }
                }).create();
        alert.show();

    }

    public void four(View view) {
        final String[] webitem = {"Google", "youtube", "facebook", "Yahoo", "Twitter", "weibo"};
        final boolean[] isCheck = {false, false, false, false, false, false};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert = builder.setTitle("选择你喜欢上的网站，可以多选")
                .setMultiChoiceItems(webitem, isCheck, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        isCheck[which] = isChecked;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = "";
                        for (int i = 0; i < webitem.length; i++) {
                            if (isCheck[i]) {
                                result += " " + webitem[i];
                            }
                        }
                        showToast("你选择了 " + result);
                    }
                }).create();
        alert.show();
    }

    public void five(View view) {

    }


    private ProgressDialog progressDialog;
    private int values=0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           if(values>=100){
               progressDialog.dismiss();
           }else {
               values++;
               progressDialog.incrementProgressBy(1);
               mHandler.sendEmptyMessageDelayed(0x11,200);
           }
        }
    };

    public void six(View view) {
        values=0;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在上传图片");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        mHandler.sendEmptyMessage(0x11);
    }

    public void showToast(CharSequence charSequence) {
        Toast toast = null;
        if (toast == null) {
            toast = Toast.makeText(this, charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
