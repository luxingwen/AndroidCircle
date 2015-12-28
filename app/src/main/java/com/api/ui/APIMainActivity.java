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
 * Created by luxin on 15-12-11.
 */
public class APIMainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayAdapter<String> iTemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_main);
        inittView();
    }

    private void inittView() {
        listView = (ListView) findViewById(R.id.api_id_listView);
        iTemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, ContantValue.mainItem);
        listView.setAdapter(iTemAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startIntent(ViewGroupActivity.class);
                break;
        }
    }

    private void startIntent(Class classes) {
        Intent intent = new Intent(this, classes);
        startActivity(intent);
    }
}
