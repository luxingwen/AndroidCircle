package com.api.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-26.
 * http://luxin.gitcafe.io
 */
public class ApiViewFlipper extends AppCompatActivity {
    private ViewFlipper viewFlipper;

    private final static int MIN_MOVE=200;

    private int []imgs={R.drawable.api_guide_1,R.drawable.api_guide_2,R.drawable.api_guide_3};


    private GestureDetector mDetector;
    private MyGestureListener myGestureListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_viewflipper);
        initView();
        myGestureListener=new MyGestureListener();
        mDetector=new GestureDetector(this,myGestureListener);
    }

    private void initView() {
        viewFlipper= (ViewFlipper) findViewById(R.id.api_id_viewFlipper);
        for (int i=0;i<imgs.length;i++){
            viewFlipper.addView(getChildView(imgs[i]));
        }
    }

    private View getChildView(int img) {
        View view= LayoutInflater.from(this).inflate(R.layout.api_item_viewflipper,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.api_id_item_viewflipper_img);
        imageView.setImageResource(img);
        return view;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
           if(e1.getX()-e2.getX()>MIN_MOVE){
               viewFlipper.setInAnimation(ApiViewFlipper.this,R.anim.api_viewflipper_right_in);
               viewFlipper.setOutAnimation(ApiViewFlipper.this,R.anim.api_viewflipper_right_out);
               viewFlipper.showNext();
           }
            if(e2.getX()-e1.getX()>MIN_MOVE){
                viewFlipper.setInAnimation(ApiViewFlipper.this,R.anim.api_viewflipper_left_in);
                viewFlipper.setOutAnimation(ApiViewFlipper.this, R.anim.api_viewflipper_left_out);
                viewFlipper.showPrevious();
            }
            return true;
        }
    }

}
