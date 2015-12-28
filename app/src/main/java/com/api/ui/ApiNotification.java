package com.api.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.luxin.qimo.MainActivity;
import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-22.
 * http://luxin.gitcafe.io
 */
public class ApiNotification extends AppCompatActivity {
    private Button button;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_notification);
        initView();
        initEvent();
    }

    private void initView() {
        button= (Button) findViewById(R.id.api_id_notification_btn);
        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
    }
    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ApiNotification.this,APIMainActivity.class);
                PendingIntent it=PendingIntent.getActivity(ApiNotification.this,0,intent,0);

                NotificationCompat.Builder builder=new NotificationCompat.Builder(ApiNotification.this);
                builder.setContentTitle("luxin")
                        .setContentText("wo shi shuaishuai")
                        .setWhen(System.currentTimeMillis())
                        .setLargeIcon(bitmap)
                        .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                        .setContentIntent(it)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true);
                NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1,builder.build());
            }
        });
    }
}
