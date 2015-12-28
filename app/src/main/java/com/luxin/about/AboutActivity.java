package com.luxin.about;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-23.
 * http://luxin.gitcafe.io
 */
public class AboutActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {



    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private Toolbar mToolbar;
    private AppBarLayout appBarLayout;
    private FrameLayout frameLayout;

    private ImageView imageView;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_about);
        initView();
        setSupportActionBar(mToolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        appBarLayout.addOnOffsetChangedListener(this);
        initParallaxValues();
    }

    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) imageView.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) frameLayout.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        imageView.setLayoutParams(petDetailsLp);
        frameLayout.setLayoutParams(petBackgroundLp);
    }

    private void startAlphaAnimation(View view, long duration, int invisible) {
        AlphaAnimation alphaAnimation=(invisible==View.VISIBLE)?new AlphaAnimation(0f,1f):new AlphaAnimation(1f,0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);

    }

    private void initView() {
        mTitleContainer= (LinearLayout) findViewById(R.id.lxw_id_about_linearlayout_title);
        mToolbar= (Toolbar) findViewById(R.id.lxw_id_about_toolbar);
        appBarLayout= (AppBarLayout) findViewById(R.id.lxw_id_about_appbar);
        frameLayout= (FrameLayout) findViewById(R.id.lxw_id_about_fram_title);
        imageView= (ImageView) findViewById(R.id.lxw_id_about_imageview_placeholder);
        mTitle= (TextView) findViewById(R.id.lxw_id_about_title);


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScoll=appBarLayout.getTotalScrollRange();
        float parcentage=(float) Math.abs(i)/(float)maxScoll;
        handleAlphaOntitle(parcentage);
        handleToolbarTitleVisible(parcentage);
    }

    private void handleToolbarTitleVisible(float parcentage) {
        if (parcentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOntitle(float parcentage) {
        if (parcentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {

            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
}
