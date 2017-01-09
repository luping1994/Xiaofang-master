package com.suntrans.xiaofang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Looney on 2016/12/22.
 */

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder1> {

    private ArrayList<String> datas;
    private Context context;

    public PicAdapter(ArrayList<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pic,parent,false);

        return new ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder1 holder, final int position) {
        holder.setdata(position);
    }


    @Override
    public int getItemCount() {
        return datas.size()==0?1:datas.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder{
        ImageView imageView ;
        public ViewHolder1(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,getAdapterPosition());
                }
            });
        }

        public void setdata(int position) {
            if (datas.size()==0){
                Glide.with(context)
                        .load(R.drawable.nopic)
                        .centerCrop()
                        .override(UiUtils.dip2px(95),UiUtils.dip2px(95))
                        .placeholder(R.drawable.nopic)
                        .into(imageView);
                return;
            }
            Glide.with(context)
                    .load(datas.get(position))
                    .centerCrop()
                    .override(UiUtils.dip2px(95),UiUtils.dip2px(95))
                    .placeholder(R.drawable.nopic)
                    .into(imageView);
        }
    }



    public onItemClickListener listener;
    public void setOnitemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
    public interface onItemClickListener{
        void onItemClick(View view,int position);
    }
}
