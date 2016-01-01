package com.api.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.api.ui.adapter.MyFragmentPagerAdapter;
import com.luxin.qimo.R;

/**
 * Created by luxin on 16-1-1.
 */
public class ApiFragment extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    public static final int PAGER_ONE=0;
    public static final int PAGER_TWO=1;
    public static final int PAGER_THREE=2;
    public static final int PAGER_FOUR=3;

    private RadioGroup radioGroup;
    private RadioButton better;
    private RadioButton channel;
    private RadioButton message;
    private RadioButton setting;

    private ViewPager viewPager;
    private MyFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_fragment);
        adapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        initView();
        better.setChecked(true);
    }

    private void initView() {
        radioGroup= (RadioGroup) findViewById(R.id.api_id_fragment_radiogroup);
        better= (RadioButton) findViewById(R.id.api_id_radiobtn_bettel);
        channel= (RadioButton) findViewById(R.id.api_id_radiobtn_channel);
        message= (RadioButton) findViewById(R.id.api_id_radiobtn_message);
        setting= (RadioButton) findViewById(R.id.api_id_radiobtn_setting);
        viewPager= (ViewPager) findViewById(R.id.api_fragment_viewpager);

        radioGroup.setOnCheckedChangeListener(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.api_id_radiobtn_bettel:
                viewPager.setCurrentItem(PAGER_ONE);
                break;
            case R.id.api_id_radiobtn_channel:
                viewPager.setCurrentItem(PAGER_TWO);
                break;
            case R.id.api_id_radiobtn_message:
                viewPager.setCurrentItem(PAGER_THREE);
                break;
            case R.id.api_id_radiobtn_setting:
                viewPager.setCurrentItem(PAGER_FOUR);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state==2){
            switch (viewPager.getCurrentItem()){
                case PAGER_ONE:better.setChecked(true);
                    break;
                case PAGER_TWO:channel.setChecked(true);
                    break;
                case PAGER_THREE:message.setChecked(true);
                    break;
                case PAGER_FOUR:setting.setChecked(true);
            }
        }
    }
}
