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

public class Type2_fragment extends Fragment {
    private ArrayList<HashMap<String,String>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_type2,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    private void initData() {



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
