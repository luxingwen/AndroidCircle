package com.api.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-12.
 */
public class ApiRadioButton extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_radiobutton);
        initView();
    }

    private void initView() {
        radioGroup= (RadioGroup) findViewById(R.id.api_id_radiogroup);
        button= (Button) findViewById(R.id.api_id_radiobutton_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<radioGroup.getChildCount();i++){
                    RadioButton radioButton= (RadioButton) radioGroup.getChildAt(i);
                    if(radioButton.isChecked()){
                        Toast.makeText(ApiRadioButton.this,"你选择的是:"+radioButton.getText(),Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });
    }
}
