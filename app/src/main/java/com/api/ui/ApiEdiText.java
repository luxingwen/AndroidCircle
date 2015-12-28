package com.api.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-12.
 */
public class ApiEdiText extends AppCompatActivity {
    private EditText editText;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_editext);
        initView();
    }

    private void initView() {
        editText= (EditText) findViewById(R.id.api_id_ediText_edi);
        button= (Button) findViewById(R.id.api_id_ediText_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableString spannableString=new SpannableString("imge");
                Drawable drawable=ApiEdiText.this.getResources().getDrawable(R.mipmap.ic_launcher);
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                ImageSpan imageSpan=new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
                spannableString.setSpan(imageSpan,0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                int curosr=editText.getSelectionStart();
                editText.getText().insert(curosr,spannableString);
            }
        });
    }
}
