package com.suntrans.xiaofang.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.suntrans.xiaofang.BaseApplication;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.CalculateRoute_Activity;
import com.suntrans.xiaofang.activity.InfoDetail_activity;
import com.suntrans.xiaofang.activity.EditFireroomInfo_activity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.model.fireroom.FireRoomDetailInfo;
import com.suntrans.xiaofang.model.fireroom.FireRoomDetailResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/13.
 * 社区消防室详情信息fragment
 */

public class Type2__info_fragment extends Fragment implements View.OnClickListener {
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter myAdapter;


    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        return inflater.inflate(R.layout.fragment_info_type1_backup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(),LinearLayoutManager.VERTICAL));


        menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);
        menuRed.setClosedOnTouchOutside(true);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);


        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
    }

    private void initData() {
        SparseArray<String> array = new SparseArray<>();
        array.put(0, "社区消防室名称");
        array.put(1, "--");
        datas.add(array);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "地址");
        array1.put(1, "--");
        datas.add(array1);

        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "志愿消防队联系人");
        array2.put(1, "--");
        datas.add(array2);


        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "联系电话");
        array3.put(1, "--");
        datas.add(array3);

        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "消防队人数");
        array4.put(1, "--");
        datas.add(array4);

        SparseArray<String> array5 = new SparseArray<>();
        array5.put(0, "车辆配置情况");
        array5.put(1, "--");
        datas.add(array5);

        SparseArray<String> array6 = new SparseArray<>();
        array6.put(0, "装备配置情况");
        array6.put(1, "--");
        datas.add(array6);

        SparseArray<String> array7 = new SparseArray<>();
        array7.put(0, "所属区");
        array7.put(1, "--");
        datas.add(array7);

        SparseArray<String> array8 = new SparseArray<>();
        array8.put(0, "所属中队");
        array8.put(1, "--");
        datas.add(array8);

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }



    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;

            v = LayoutInflater.from(getActivity()).inflate(R.layout.item_cominfo, parent, false);
            return new ViewHolder1(v);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((ViewHolder1) holder).setData(position);
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
            TextView name;
            TextView value;

            public ViewHolder1(View itemView) {
                super(itemView);
                value = (TextView) itemView.findViewById(R.id.value);
                name = (TextView) itemView.findViewById(R.id.name);
            }

            public void setData(int position) {
                if (position==datas.size()){
                    value.setGravity(Gravity.CENTER|Gravity.LEFT);

                }else {
                    value.setGravity(Gravity.CENTER|Gravity.RIGHT);
                }
                name.setText(datas.get(position).get(0));
                value.setText(datas.get(position).get(1));
            }
        }
    }

    public FireRoomDetailInfo myInfo;
    private void getData() {
        RetrofitHelper.getApi().getFireRoomDetailInfo(((InfoDetail_activity)getActivity()).companyId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<FireRoomDetailResult>() {
                    @Override
                    public void call(FireRoomDetailResult result) {
                        if (result!=null){
                            if (!result.status.equals("0")){
                                FireRoomDetailInfo info = result.result;
                                myInfo=info;
                                LogUtil.i(info.toString());
                                refreshView(info);
                            }
                        }else {
                            UiUtils.showToast(getActivity(),"请求失败!");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        UiUtils.showToast(BaseApplication.getApplication(),"请求失败!");
                    }
                });
    }

    private void refreshView(FireRoomDetailInfo info) {
        datas.get(0).put(1,info.name);//名字
        datas.get(1).put(1,info.addr);//地址
        datas.get(2).put(1,info.contact==null?"--":info.contact);
        datas.get(3).put(1,info.phone==null?"--":info.phone);
        datas.get(4).put(1,info.membernum==null?"--":info.membernum+"人");
        datas.get(5).put(1,info.cardisp==null?"--":info.cardisp);
        datas.get(6).put(1,info.equipdisp==null?"--":info.equipdisp);
        datas.get(7).put(1,info.district==null?"--":info.district);
        datas.get(8).put(1,info.group+info.group);
        myAdapter.notifyDataSetChanged();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab1:
                final AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog =builder.create();
                dialog.setTitle("确定删除该单位?");
                dialog.show();
                break;
            case R.id.fab2:
                Intent intent = new Intent();
                intent.setClass(getActivity(), EditFireroomInfo_activity.class);
                intent.putExtra("title",((InfoDetail_activity)getActivity()).title);
                intent.putExtra("id",((InfoDetail_activity)getActivity()).companyId);
                intent.putExtra("info",myInfo);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.fab3:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(),CalculateRoute_Activity.class);
                if (getActivity().getIntent().getParcelableExtra("from")==null){
                    final AlertDialog.Builder builder1 =new AlertDialog.Builder(getActivity());
                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog1 =builder1.create();
                    dialog1.setTitle("单位未添加地理坐标,无法导航!");
                    dialog1.show();
                    break;
                }
                intent1.putExtra("from",getActivity().getIntent().getParcelableExtra("from"));
                intent1.putExtra("to",getActivity().getIntent().getParcelableExtra("to"));
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
