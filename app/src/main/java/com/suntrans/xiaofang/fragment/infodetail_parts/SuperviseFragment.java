package com.suntrans.xiaofang.fragment.infodetail_parts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.eventandissue.SuperviseDetail_activity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.adapter.RecycleviewAdapter;
import com.suntrans.xiaofang.model.supervise.Supervise;
import com.suntrans.xiaofang.model.supervise.SuperviseListResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/22.
 */

public class SuperviseFragment extends Fragment {


    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ArrayList<SparseArray<String>> datas;
    private RecycleviewAdapter adapter;

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
        adapter = new RecycleviewAdapter(getActivity(), datas);
        adapter.setOnItemClickListener(new RecycleviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                LogUtil.i("id为："+datas.get(position).get(4));
                intent.putExtra("id",datas.get(position).get(4));
                intent.setClass(getActivity(), SuperviseDetail_activity.class);
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
        getData(id);
    }

    private void getData(String id) {
        RetrofitHelper.getApi().getSuperviseList(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SuperviseListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SuperviseListResult result) {
                        if (result.status.equals("1")) {
                            datas.clear();
                            for (Supervise info :result.result) {
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
