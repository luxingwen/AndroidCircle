package com.api.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-12.
 */
public class ApiButton extends AppCompatActivity {
    private Button btnOne;
    private Button btnTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_button);
        initView();
    }

    private void initView() {
        btnOne = (Button) findViewById(R.id.api_id_btn_button_btnone);
        btnTwo = (Button) findViewById(R.id.api_id_button_btntwo);
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnTwo.getText().toString().equals("按钮不可用")) {
                    btnOne.setFocusable(true);
                    btnTwo.setText("按钮可用");
                } else {
                    btnOne.setEnabled(false);
                    btnTwo.setText("按钮不可用");
                }
            }
        });
    }
}
