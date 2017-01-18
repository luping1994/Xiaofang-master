package com.suntrans.xiaofang.fragment.infodetail_parts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.eventandissue.EventDetail_activity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.adapter.RecycleviewAdapter;
import com.suntrans.xiaofang.model.event.Event;
import com.suntrans.xiaofang.model.event.EventListResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/22.
 */

public class EventFragment extends RxFragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ArrayList<SparseArray<String>> datas;
    private RecycleviewAdapter adapter;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        adapter = new RecycleviewAdapter(getActivity(), datas,0);
        adapter.setOnItemClickListener(new RecycleviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("id",datas.get(position).get(4));
                intent.setClass(getActivity(), EventDetail_activity.class);
                startActivity(intent);
            }
        });
        recyclerView.addItemDecoration(new RecyclerViewDivider(App.getApplication(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setId(String id){
        this.id = id;
        getData(id);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    private void getData(String id) {
        RetrofitHelper.getApi().getEventList(id)
                .compose(this.<EventListResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<EventListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(EventListResult result) {
                        if (result.status.equals("1")) {
                            datas.clear();
                            for (Event info :result.result) {
                                SparseArray<String> array = new SparseArray<String>();
                                array.put(0,info.contents);
                                array.put(1,info.user_id);
                                array.put(2,info.status);
                                array.put(3,info.created_at);
                                array.put(4,info.id);
                                datas.add(array);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
