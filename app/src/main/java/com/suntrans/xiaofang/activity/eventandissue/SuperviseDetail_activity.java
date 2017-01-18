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
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.activity.others.DetailPic_Activity;
import com.suntrans.xiaofang.adapter.PicAdapter;
import com.suntrans.xiaofang.model.supervise.ResultSup;
import com.suntrans.xiaofang.model.supervise.SuperviseDetailResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
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

    @BindView(R.id.companyname)
    TextView companyname;
    @BindView(R.id.contents)
    TextView contents;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.creat_at)
    TextView creatAt;
    @BindView(R.id.state)
    TextView state;
    private Toolbar toolbar;
    private String id;
    private RecyclerView recyclerView_before;
    private LinearLayoutManager manager_before;
    private PicAdapter adapter_before;
    private ArrayList<String> url_before;
    private ArrayList<SparseArray<String>> datas;


    private LinearLayoutManager manager_after;
    private RecyclerView recyclerView_after;
    private PicAdapter adapter_after;
    private ArrayList<String> url_after;

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
        url_before = new ArrayList<>();
        url_after = new ArrayList<>();
        recyclerView_before = (RecyclerView) findViewById(R.id.recycleview_before);
        manager_before = new LinearLayoutManager(this);
        manager_before.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter_before = new PicAdapter(url_before, this);
        recyclerView_before.setLayoutManager(manager_before);
        recyclerView_before.setAdapter(adapter_before);


        recyclerView_after = (RecyclerView) findViewById(R.id.recycleview_after);
        manager_after = new LinearLayoutManager(this);
        manager_after.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter_after = new PicAdapter(url_after, this);
        recyclerView_after.setLayoutManager(manager_after);
        recyclerView_after.setAdapter(adapter_after);

        adapter_before.setOnitemClickListener(new PicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (url_before == null || url_before.size() == 0) {
                    return;
                }
                if (Build.VERSION.SDK_INT < 21) {
                    Intent intent = new Intent();
                    intent.putExtra("url", url_before.get(position));
                    intent.setClass(SuperviseDetail_activity.this, DetailPic_Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SuperviseDetail_activity.this, DetailPic_Activity.class);
                    intent.putExtra("url", url_before.get(position));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(SuperviseDetail_activity.this, view, getString(R.string.transition_test));
                    startActivity(intent, options.toBundle());
                }
            }
        });

        adapter_after.setOnitemClickListener(new PicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (url_after == null || url_after.size() == 0) {
                    return;
                }
                if (Build.VERSION.SDK_INT < 21) {
                    Intent intent = new Intent();
                    intent.putExtra("url", url_after.get(position));
                    intent.setClass(SuperviseDetail_activity.this, DetailPic_Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SuperviseDetail_activity.this, DetailPic_Activity.class);
                    intent.putExtra("url", url_after.get(position));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(SuperviseDetail_activity.this, view, getString(R.string.transition_test));
                    startActivity(intent, options.toBundle());
                }
            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        SparseArray<String> array = new SparseArray<>();
        array.put(0, "地点:");
        array.put(1, "");
        datas.add(array);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "监督内容:");
        array1.put(1, "");
        datas.add(array1);
        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "录入人:");
        array2.put(1, "");
        datas.add(array2);

        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "录入时间:");
        array3.put(1, "");
        datas.add(array3);

        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "处理情况:");
        array4.put(1, "");
        datas.add(array4);

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
        id = getIntent().getStringExtra("id");
        System.out.println("监督事件id" + id);
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
                            refreshView(result);
                    }


                });
    }

    private final String TAG = "SuperviseDetail_activity";
    private ArrayList<String> vedio_url;


    private void refreshView(SuperviseDetailResult result1) {
        ResultSup data = result1.result;
        System.out.println(result1.toString());
//        Supervise supervise = data.item;

//        datas.get(0).put(1, supervise.company_name);
//        datas.get(1).put(1, "   " + supervise.contents);
//        datas.get(2).put(1, supervise.user_id);
//        datas.get(3).put(1, supervise.created_at);
//        datas.get(4).put(1, supervise.is_done.equals("1") ? "已处理" : "未处理");

        refreshlayout();
        for (String s :
                data.imgs) {
            url_after.add(s);
        }

        for (String s :
                data.imgraws) {
            url_before.add(s);
        }
        adapter_before.notifyDataSetChanged();
        adapter_after.notifyDataSetChanged();
    }

    private void refreshlayout() {
        companyname.setText(datas.get(0).get(0) + datas.get(0).get(1));
        contents.setText(datas.get(1).get(1));
        name.setText(datas.get(2).get(0) + datas.get(2).get(1));
        creatAt.setText(datas.get(3).get(0) + datas.get(3).get(1));
        state.setText(datas.get(4).get(0) + datas.get(4).get(1));
    }
}
