package com.api.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.api.base.ContantValue;
import com.luxin.qimo.R;

/**
 * Created by luxin on 15-12-12.
 */
public class ViewGroupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayAdapter<String> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_views_layout);
        initView();

    }

    private void initView() {
        listView = (ListView) findViewById(R.id.api_id_views_listview);
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, ContantValue.viewItem);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:startIntent(ApiLinearLayout.class);
                break;
            case 1:startIntent(ApiRelativeLayout.class);
                break;
            case 2:startIntent(ApiTableLayout.class);
                break;
            case 3:startIntent(ApiGradLayout.class);
                break;
            case 4:startIntent(ApiFrameLayout.class);
                break;
            case 5:startIntent(ApiTextView.class);
                break;
            case 6:startIntent(ApiEdiText.class);
                break;
            case 7:startIntent(ApiButton.class);
                break;
            case 8:startIntent(ApiRadioButton.class);
                break;
            case 9:startIntent(ApiCheckBox.class);
                break;
            case 10:startIntent(ApiSwitchToggleButton.class);
                break;
            case 11:startIntent(ApiProgressBar.class);
                break;
            case 12:startIntent(ApiSeekBar.class);
                break;
            case 13:startIntent(ApiRatingBar.class);
                break;
            case 14:startIntent(ApiDatePicker.class);
                break;
            case 15:startIntent(ApiNotification.class);
                break;
            case 16:startIntent(ApiSpinner.class);
                break;
            case 17:startIntent(ApiExpandableListView.class);
                break;
            case 18:startIntent(ApiViewFlipper.class);
                break;
            case 19:startIntent(ApiAlertDialog.class);
                break;
            case 20:startIntent(ApiFragment.class);
                break;

        }
    }

    private void startIntent(Class classes) {
        Intent intent = new Intent(this, classes);
        startActivity(intent);
    }
}
