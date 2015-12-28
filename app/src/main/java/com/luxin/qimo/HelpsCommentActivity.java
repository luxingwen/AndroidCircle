package com.luxin.qimo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.luxin.adapter.CommentAdapter;
import com.luxin.adapter.GridViewHelpsAdapter;
import com.luxin.bean.Comment;
import com.luxin.bean.Helps;
import com.luxin.bean.MyUser;
import com.luxin.bean.MyUserInstallation;
import com.luxin.bean.NotifyMsg;
import com.luxin.bean.PhontoFiles;
import com.luxin.util.Constant;
import com.luxin.util.DateUtil;
import com.luxin.util.ExpressionUtil;
import com.luxin.util.ImageLoader;
import com.luxin.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by luxin on 15-12-17.
 *  http://luxin.gitcafe.io
 */
public class HelpsCommentActivity extends AppCompatActivity {
    private final static String TAG = "HelpsCommentActivity";

    private ScrollView scrollView;
    private ListView listView;

    private LinearLayout linearLayout;
    private ImageView userImg;
    private TextView username;
    private TextView personality;
    private TextView creatTime;

    private TextView content;
    private ImageView contentImg;
    private GridView gridView;

    private Helps helps;
    private GridViewHelpsAdapter gridViewHelpsAdapter;

    private FloatingActionButton floatingActionButton;


    private List<Comment> mItemComment;

    private CommentAdapter adapter;


    private View view;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lxw_helps_comment);

        toolbar = (Toolbar) findViewById(R.id.lxw_id_helps_comment_toolbar);
        setSupportActionBar(toolbar);

        initView();
        initData();
        initEvent();
    }


    private void initData() {
        helps = (Helps) getIntent().getSerializableExtra("helps");
        MyUser myUser = helps.getUser();
        if (myUser.getAuvter() != null) {
            ImageLoader.getInstance(1, ImageLoader.Type.LIFO).loaderImage(Constant.USERIMG + myUser.getAuvter().getUrl(), userImg, true);
        }
        username.setText(myUser.getUsername());
        personality.setText(myUser.getPersonality());
        creatTime.setText(DateUtil.getFriendlyDate(helps.getCreatedAt()));

        SpannableString spannableString = ExpressionUtil.getSpannableString(this, helps.getContent());
        content.setText(spannableString);

        String imgpath = null;
        List<BmobFile> phontos = null;
        final PhontoFiles phontoFiles = helps.getPhontofile();
        if (phontoFiles != null) {
            Log.e(TAG, "====phonoto files is not null");
            imgpath = helps.getPhontofile().getPhoto();
            phontos = helps.getPhontofile().getPhotos();
        }

        if (imgpath != null) {
            contentImg.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            ImageLoader.getmInstance().loaderImage(imgpath, contentImg, true);
            contentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HelpsCommentActivity.this, LookImageActivity.class);
                    intent.putExtra("helps", helps);
                    startActivity(intent);
                }
            });
        } else if (phontos != null) {
            contentImg.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            gridViewHelpsAdapter = new GridViewHelpsAdapter(this, phontos);
            gridView.setAdapter(gridViewHelpsAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(HelpsCommentActivity.this, LookImageViewPagerActivity.class);
                    intent.putExtra("phontoFiles", phontoFiles);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        } else {
            contentImg.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }

        listView.addHeaderView(view);

        mItemComment = new ArrayList<Comment>();
        if (mItemComment.size() == 0) {
            query();
        }
        adapter = new CommentAdapter(this, mItemComment);
        listView.setAdapter(adapter);

    }

    private boolean isFirst = true;

    private void query() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("helps", new BmobPointer(helps));
        query.setLimit(50);
        query.include("user,helps.user");
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if (list.size() > 0) {

                    mItemComment.clear();

                    mItemComment.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    private void initView() {

        listView = (ListView) findViewById(R.id.lxw_id_helps_comment_listview);


        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.lxw_item_helps, null);


        userImg = (ImageView) view.findViewById(R.id.lxw_id_item_helps_userimg);
        username = (TextView) view.findViewById(R.id.lxw_id_item_helps_username);
        personality = (TextView) view.findViewById(R.id.lxw_id_item_helps_user_personality);
        creatTime = (TextView) view.findViewById(R.id.lxw_id_item_helps_create_time);

        content = (TextView) view.findViewById(R.id.lxw_id_item_helps_content);
        gridView = (GridView) view.findViewById(R.id.lxw_id_item_helps_gridview);
        contentImg = (ImageView) view.findViewById(R.id.lxw_id_item_helps_content_img);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.lxw_id_push_helps_comment_float);
    }

    private void initEvent() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushComment();
            }
        });


    }

    private void pushComment() {

        final MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
        if (myUser == null) {
            showLoginDialog();
           // ToastUtil.show(this, "请登录之后在评论", Toast.LENGTH_SHORT);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.lxw_push_helps_comment, null);
        final EditText ediComment = (EditText) view.findViewById(R.id.lxw_id_push_helps_comment_edi);
        ediComment.setError(null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).setPositiveButton("发布", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comment = ediComment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    ediComment.setError("内容不能为空");
                    // ToastUtil.show(this, "内容不能为空", Toast.LENGTH_SHORT);
                    return;
                }

                push(comment, myUser);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).create().show();


    }

    /**
     * 发表评论
     * @param comment
     * @param myUser
     */
    private void push(final String comment, final MyUser myUser) {
        Comment comment1 = new Comment();
        comment1.setUser(myUser);
        comment1.setHelps(helps);
        comment1.setComment(comment);
        comment1.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                if(!myUser.getObjectId().equals(helps.getUser().getObjectId())){
                    bmobpush(myUser,comment);
                }
                ToastUtil.show(HelpsCommentActivity.this, "评论成功", Toast.LENGTH_SHORT);
                isFirst = false;
                query();
                Log.e(TAG, "====评论成功====");
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.show(HelpsCommentActivity.this, "评论失败,请检查网络连接后重试", Toast.LENGTH_SHORT);
                Log.e(TAG, "====评论失败===" + s);
            }
        });
    }

    /**
     * 使用bmob进行消息推送
     * @param myUser
     */
    private void bmobpush(MyUser myUser,String comment) {
        String installationId=helps.getUser().getObjectId();
        BmobPushManager bmobPushManager=new BmobPushManager(this);
        BmobQuery<MyUserInstallation> query=new BmobQuery<MyUserInstallation>();
        query.addWhereEqualTo("uid", installationId);
        bmobPushManager.setQuery(query);
        bmobPushManager.pushMessage(myUser.getUsername() + "评论了你");

        NotifyMsg notifyMsg=new NotifyMsg();
        notifyMsg.setHelps(helps);
        notifyMsg.setUser(helps.getUser());
        notifyMsg.setAuthor(myUser);
        notifyMsg.setStatus(false);
        notifyMsg.setMessage(comment);
        notifyMsg.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG,"====notifymsg===success");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.setMessage("请登录之后在评论")
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HelpsCommentActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
    }


}
