package com.api.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-12.
 */
public class ApiSeekBar extends AppCompatActivity {
    private SeekBar seekbar;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_seekbar);
        initView();
    }

    private void initView() {
        seekbar= (SeekBar) findViewById(R.id.api_id_seekbar_seekbar);
        textView= (TextView) findViewById(R.id.api_id_seekbar_text);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("当前进度值:"+progress+"/100");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //触碰SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });
    }
}
