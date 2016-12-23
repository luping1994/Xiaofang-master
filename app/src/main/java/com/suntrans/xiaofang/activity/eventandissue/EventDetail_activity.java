package com.suntrans.xiaofang.activity.eventandissue;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.PicAdapter;
import com.suntrans.xiaofang.model.event.EventDetailResult;
import com.suntrans.xiaofang.model.event.Result;
import com.suntrans.xiaofang.network.RetrofitHelper;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/22.
 */

public class EventDetail_activity extends AppCompatActivity {

    private Toolbar toolbar;
    private String id;
    private RecyclerView recyclerView_before;
    private RecyclerView recyclerView_after;
    private LinearLayoutManager manager_before;
    private LinearLayoutManager manager_after;
    private PicAdapter adapter_before;
    private PicAdapter adapter_after;

    private ArrayList<String> url_before;
    private ArrayList<String> url_after;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_eventdetail);
        setupToolBar();
        initView();
    }

    private void initView() {
        url_before = new ArrayList<>();
        url_after = new ArrayList<>();
        recyclerView_before = (RecyclerView) findViewById(R.id.recycleview_before);
        recyclerView_after = (RecyclerView) findViewById(R.id.recycleview_after);
        manager_before = new LinearLayoutManager(this);
        manager_after = new LinearLayoutManager(this);
        manager_before.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager_after.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter_before = new PicAdapter(url_before,this);
        adapter_after = new PicAdapter(url_after,this);

        recyclerView_before.setLayoutManager(manager_before);
        recyclerView_before.setAdapter(adapter_before);

        recyclerView_after.setLayoutManager(manager_after);
        recyclerView_after.setAdapter(adapter_after);
    }

    private void setupToolBar() {
        id = getIntent().getStringExtra("id");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("事件详情");
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
        getData();
    }

    private void getData(){
        RetrofitHelper.getApi().getEventDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<EventDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(EventDetailResult result) {

                        refreshView(result);
                    }


                });
    }


    private void refreshView(EventDetailResult result) {
        Result data= result.result;
        for (String s:
             data.img_before) {
            url_before.add(s);
        }

        for (String s:
                data.img_beforeR) {
            url_before.add(s);
        }
        for (String s:
                data.img_after) {
            url_after.add(s);
        }

        for (String s:
                data.img_afterR) {
            url_after.add(s);
        }
        adapter_before.notifyDataSetChanged();
        adapter_after.notifyDataSetChanged();
    }
}
