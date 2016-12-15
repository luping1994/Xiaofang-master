package com.suntrans.xiaofang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Looney on 2016/12/13.
 */

public class Type3_fragment extends Fragment {
    private ArrayList<HashMap<String,String>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_type3,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        initData();
//        recyclerView = (RecyclerView)view.findViewById(R.id.recycleview);
//        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
//        manager = new LinearLayoutManager(getContext());
//        adapter = new MyAdapter();
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        HashMap<String,String> map = new HashMap<>();
        map.put("property","名称");
        map.put("value","null");
        datas.add(map);

        HashMap<String,String> map2 = new HashMap<>();
        map2.put("property","地址");
        map2.put("value","null");
        datas.add(map2);

        HashMap<String,String> map3 = new HashMap<>();
        map3.put("property","消防队站联系电话");
        map3.put("value","null");
        datas.add(map3);

        HashMap<String,String> map4 = new HashMap<>();
        map4.put("property","消防队员人数");
        map4.put("value","null");
        datas.add(map4);

        HashMap<String,String> map5 = new HashMap<>();
        map5.put("property","消防车总数");
        map5.put("value","null");
        datas.add(map5);

        HashMap<String,String> map6 = new HashMap<>();
        map6.put("property","消防车辆配置情况");
        map6.put("value","null");
        datas.add(map6);

        HashMap<String,String> map7 = new HashMap<>();
        map7.put("property","车载水总量（吨");
        map7.put("value","null");
        datas.add(map7);

        HashMap<String,String> map8 = new HashMap<>();
        map8.put("property","车载干粉总量（吨）");
        map8.put("value","null");
        datas.add(map8);

        HashMap<String,String> map9 = new HashMap<>();
        map9.put("property","所属区");
        map9.put("value","null");
        datas.add(map9);

        HashMap<String,String> map10 = new HashMap<>();
        map10.put("property","所属大队");
        map10.put("value","null");
        datas.add(map10);

    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType == 0) {
                v = LayoutInflater.from(getActivity()).inflate(R.layout.item_add, parent, false);
                return new ViewHolder1(v);
            } else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyAdapter.ViewHolder2) {
                ((ViewHolder2) holder).setData(position);
            } else {
                ((ViewHolder1) holder).setData(position);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        class ViewHolder1 extends RecyclerView.ViewHolder {
            TextView property;
            EditText value;
            public ViewHolder1(View itemView) {
                super(itemView);
                property = (TextView) itemView.findViewById(R.id.property);
                value = (EditText) itemView.findViewById(R.id.value);
            }

            public void setData(int position) {
                property.setText(datas.get(position).get("property"));
            }
        }

        class ViewHolder2 extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView name;
            TextView value;

            public ViewHolder2(View itemView) {
                super(itemView);
                value = (TextView) itemView.findViewById(R.id.value);
                name = (TextView) itemView.findViewById(R.id.name);
                imageView = (ImageView) itemView.findViewById(R.id.image);
            }

            public void setData(int position) {

            }
        }
    }
}
