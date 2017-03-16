package com.suntrans.xiaofang.activity.eventandissue;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.activity.others.DetailPic_Activity;
import com.suntrans.xiaofang.adapter.PicAdapter;
import com.suntrans.xiaofang.model.supervise.ResultSup;
import com.suntrans.xiaofang.model.supervise.Supervise;
import com.suntrans.xiaofang.model.supervise.SuperviseDetailResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/22.
 */

public class SuperviseDetail_activity extends BasedActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.addr)
    TextView addr;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.contact_way)
    TextView contactWay;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.time)
    TextView time;

    private Toolbar toolbar;
    private String id;

    private ArrayList<String> imageRaws;


    private LinearLayoutManager manager_after;
    private RecyclerView recyclerView_after;
    private PicAdapter adapter_after;
    private ArrayList<String> imgs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_supervisedetail);
        ButterKnife.bind(this);
        initData();
        setupToolBar();
        initView();
    }

    private void initView() {
        imageRaws = new ArrayList<>();
        imgs = new ArrayList<>();


        recyclerView_after = (RecyclerView) findViewById(R.id.recycleview_after);
        manager_after = new LinearLayoutManager(this);
        manager_after.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter_after = new PicAdapter(imgs, this);
        recyclerView_after.setLayoutManager(manager_after);
        recyclerView_after.setAdapter(adapter_after);


        adapter_after.setOnitemClickListener(new PicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (imageRaws == null) {
                    return;
                }
                if (imageRaws.size() == 0) {
                    return;
                }
                if (Build.VERSION.SDK_INT < 21) {
                    Intent intent = new Intent();
                    intent.putExtra("url", imageRaws.get(position));
                    intent.setClass(SuperviseDetail_activity.this, DetailPic_Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SuperviseDetail_activity.this, DetailPic_Activity.class);
                    intent.putExtra("url", imageRaws.get(position));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(SuperviseDetail_activity.this, view, getString(R.string.transition_test));
                    startActivity(intent, options.toBundle());
                }
            }
        });
    }

    private void initData() {

        id = getIntent().getStringExtra("id");
        LogUtil.i(TAG, "监督事件id" + id);

        getData();
//
//        SparseArray<String> array5 = new SparseArray<>();
//        array5.put(0, "处理情况:");
//        array5.put(1, "");
//        datas.add(array5);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupToolBar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("监督详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getData() {
        RetrofitHelper.getApi().getSuperviseDetail(id)
                .compose(this.<SuperviseDetailResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SuperviseDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SuperviseDetailResult result) {
                        if (result != null)
                            if (result.status.equals("1")) {
                                refreshView(result);
                            }
                    }


                });
    }

    private final String TAG = "SuperviseDetail_activity";


    private void refreshView(SuperviseDetailResult result1) {
        ResultSup data = result1.result;
        System.out.println(result1.result.imgraws.get(0));
        Supervise supervise = data.item;
        name.setText(supervise.company_name);
        addr.setText(supervise.company_address);
        contact.setText(supervise.company_leader);
        contactWay.setText(supervise.company_phone);
        username.setText(supervise.user_id);
        time.setText(supervise.created_at);
        refreshlayout();
        for (String s :
                data.imgs) {
            imgs.add(s);
        }

        for (String s :
                data.imgraws) {
            imageRaws.add(s);
        }
        adapter_after.notifyDataSetChanged();
    }

    private void refreshlayout() {

    }
}
