package com.luxin.qimo;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.api.ui.APIMainActivity;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.luxin.about.AboutActivity;
import com.luxin.adapter.HelpsMainAdapter;
import com.luxin.bean.Helps;
import com.luxin.bean.MyUser;
import com.luxin.util.Constant;
import com.luxin.util.ImageLoader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private final static String TAG = "MainActivity";

    private CircleImageView userImage;

    private ListView listView;
    private HelpsMainAdapter adapter;

    private TextView userName;
    private MyUser myUser = null;
    private MaterialRefreshLayout materialRefreshLayout;
    private ProgressDialog mProgressDialog;


    private FloatingActionButton fab;

    private List<Helps> mItemList;

    private int numPage;

    private boolean isRefresh = false;


    private enum RefleshType {
        REFRESH, LOAD_MORE
    }

    private RefleshType refleshType = RefleshType.LOAD_MORE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initUserImg();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        numPage = 0;

        initEvent();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initData();


    }

    private void initData() {
        mItemList = new ArrayList<Helps>();
        adapter = new HelpsMainAdapter(this, mItemList);
        listView.setAdapter(adapter);
        if (mItemList.size() == 0) {
            refleshType = RefleshType.REFRESH;
            query(0);
        }
        // materialRefreshLayout.autoRefresh();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, HelpsCommentActivity.class);
                intent.putExtra("helps", mItemList.get(position));
                startActivity(intent);
            }
        });

    }


    private void initEvent() {
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refleshType = RefleshType.REFRESH;
                Log.e(TAG, "-----refresh----");
                fab.setVisibility(View.VISIBLE);
                query(0);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                refleshType = RefleshType.LOAD_MORE;
                Log.e(TAG, "-----LoaderMore----");
                query(numPage);

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myUser != null) {
                    isRefresh = true;
                    Intent intent = new Intent(MainActivity.this, PublishActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int starx = 0;
                int stay = 800;
                int endx;
                int endy = 0;

                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    // fab.setVisibility(View.GONE);
                    starx = (int) event.getX();
                    stay = (int) event.getY();
                    Log.e(TAG, "====starx===" + starx);
                    Log.e(TAG, "====stary===" + stay);
                }

                if (MotionEvent.ACTION_MOVE == event.getAction()) {

                }


                if (MotionEvent.ACTION_UP == event.getAction()) {
                    endx = (int) event.getX();
                    endy = (int) event.getY();

                    Log.e(TAG, "====endx===" + endx);
                    Log.e(TAG, "====endy===" + endy);
                    // fab.setVisibility(View.VISIBLE);


                }

                if (stay - endy > 0) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

    }

    private void initUserImg() {
        myUser = BmobUser.getCurrentUser(this, MyUser.class);
        userImage.setImageResource(R.drawable.personal_default_user_icon);
        if (myUser != null) {
            if (myUser.getAuvter() != null) {
                String auvterPath = "http://file.bmob.cn/" + myUser.getAuvter().getUrl();
                ImageLoader.getmInstance().loaderImage(auvterPath, userImage, true);
            }
            userName.setText(myUser.getUsername());

        } else {

        }
    }

    private void initView() {
        mProgressDialog = ProgressDialog.show(this, null, "正在加载");
        userImage = (CircleImageView) findViewById(R.id.lxw_id_nav_header_img);
        userImage.setOnClickListener(this);


        userName = (TextView) findViewById(R.id.lxw_id_nav_header_username);

        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.main_refresh);
        listView = (ListView) findViewById(R.id.main_listview);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //  fab.setAnimation();
        // materialRefreshLayout.autoRefresh();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_lxw_push) {
            if (myUser != null) {
                isRefresh = true;
                Intent intent = new Intent(MainActivity.this, PublishActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            return true;
        }
        if (id == R.id.action_lxw_apidemo) {
            Intent intent = new Intent(MainActivity.this, APIMainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            Intent intent=new Intent(this,MeiziActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gank) {
            Intent intent = new Intent(MainActivity.this, GankIoActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_message) {
            if (myUser != null) {
                Intent intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_robot) {
            if (myUser != null) {
                Intent intent = new Intent(this, RobotChatActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_blog) {
            Intent intent = new Intent(MainActivity.this, LxwBlogActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lxw_id_nav_header_img:
                showUserProfile();
                break;
        }
    }

    private void showUserProfile() {
        if (myUser != null) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    private void query(int page) {
        BmobQuery<Helps> helpsBmobQuery = new BmobQuery<Helps>();
        helpsBmobQuery.setLimit(Constant.NUMBER_PAGER);
        helpsBmobQuery.order("-createdAt");
//        BmobDate date=new BmobDate(new Date(System.currentTimeMillis()));
//        helpsBmobQuery.addWhereLessThan("createdAt",date);

        helpsBmobQuery.setSkip(Constant.NUMBER_PAGER * page);
        helpsBmobQuery.include("user,phontofile");
//        helpsBmobQuery.include("phontofile");
        helpsBmobQuery.findObjects(this, new FindListener<Helps>() {
            @Override
            public void onSuccess(List<Helps> list) {
                mProgressDialog.dismiss();
                if (list.size() > 0) {
                    if (refleshType == RefleshType.REFRESH) {
                        Log.e(TAG, "====refresh list clear");
                        numPage = 0;
                        mItemList.clear();

                    }
                    if (list.size() < Constant.NUMBER_PAGER) {

                    }
                    mItemList.addAll(list);
                    numPage++;
                    adapter.notifyDataSetChanged();
                    materialRefreshLayout.finishRefresh();
                    materialRefreshLayout.finishRefreshLoadMore();
                } else if (refleshType == RefleshType.REFRESH) {

                    materialRefreshLayout.finishRefresh();
                } else if (refleshType == RefleshType.LOAD_MORE) {
                    materialRefreshLayout.finishRefreshLoadMore();
                } else {
                    numPage--;
                }


            }

            @Override
            public void onError(int i, String s) {
                mProgressDialog.dismiss();
                materialRefreshLayout.finishRefresh();
                materialRefreshLayout.finishRefreshLoadMore();

                shwoErrorDialog();
            }
        });
    }

    private void shwoErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert = builder.setMessage("未获取到数据，请检查网络数据后重试。")
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        alert.show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initUserImg();
        fab.setVisibility(View.VISIBLE);
        if (isRefresh) {
            query(numPage - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  query();
    }
}
