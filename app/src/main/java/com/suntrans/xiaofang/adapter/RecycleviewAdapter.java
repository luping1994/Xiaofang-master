package com.suntrans.xiaofang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntrans.xiaofang.R;

import java.util.ArrayList;

/**
 * Created by Looney on 2016/12/22.
 */

public class RecycleviewAdapter extends RecyclerView.Adapter {

    private int type;
    private Context context;

    public RecycleviewAdapter(Context context, ArrayList<SparseArray<String>> datas, int type) {
        this.context = context;
        this.datas = datas;
        this.type = type;
    }

    private ArrayList<SparseArray<String>> datas;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {

            View v;
            v = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
            return new ViewHolder1(v);
        } else {
            View v;
            v = LayoutInflater.from(context).inflate(R.layout.item_search_activity2, parent, false);
            return new viewHolder2(v);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1)
            ((ViewHolder1) holder).setData(position);
        else
            ((viewHolder2) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        if (datas.size() == 0) {
            return 1;
        } else {
            return datas.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size()) {
            return 1;
        }
        return 0;
    }

    class viewHolder2 extends RecyclerView.ViewHolder {
        TextView textView;

        public viewHolder2(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.value);

        }

        public void setData(int position) {
            if (type == 0)
                textView.setText("单位无事件记录");
            else
                textView.setText("单位无监督记录");

        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView content;
        TextView time;
        TextView name;
        TextView state;
        CardView root;

        public ViewHolder1(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            name = (TextView) itemView.findViewById(R.id.name);
            state = (TextView) itemView.findViewById(R.id.state);
            time = (TextView) itemView.findViewById(R.id.time);
            root = (CardView) itemView.findViewById(R.id.root);
        }

        public void setData(final int position) {
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
            content.setText("事件内容:" + datas.get(position).get(0));
            name.setText("录入人:" + datas.get(position).get(1));
            if (datas.get(position).get(2).equals("1")) {
                state.setText("已处理");
                state.setTextColor(Color.BLUE);
            } else {
                state.setText("未处理");
                state.setTextColor(Color.RED);
            }
            time.setText("录入时间:" + datas.get(position).get(3));
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
