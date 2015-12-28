package com.api.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-12.
 */
public class ApiCheckBox extends AppCompatActivity {
    private Button btn;
    private CheckBox up;
    private CheckBox center;
    private CheckBox daye;
    private CheckBox auxiliary;
    private CheckBox adc;
    private CheckBox all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_checkbox);
        initView();
    }

    private void initView() {
        btn= (Button) findViewById(R.id.api_id_checkbox_btn);
        up= (CheckBox) findViewById(R.id.api_id_checkbox_up);
        auxiliary= (CheckBox) findViewById(R.id.api_id_checkbox_auxiliary);
    }

    public void onCheckboxClicked(View view){
        boolean isCheck=((CheckBox)view).isChecked();
        switch (view.getId()){

        }
    }
}
