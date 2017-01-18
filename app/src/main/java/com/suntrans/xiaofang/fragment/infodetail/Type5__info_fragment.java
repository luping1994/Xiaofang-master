package com.suntrans.xiaofang.fragment.infodetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.edit.EditLicense_activity;
import com.suntrans.xiaofang.activity.mapnav.CalculateRoute_Activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.fragment.BasedFragment;
import com.suntrans.xiaofang.model.license.AddLicenseResult;
import com.suntrans.xiaofang.model.license.LicenseDetailInfo;
import com.suntrans.xiaofang.model.license.LicenseDetailResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/13.
 * 行政许可详情信息fragment
 */

public class Type5__info_fragment extends BasedFragment implements View.OnClickListener {
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
        super.onViewCreated(view,savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setVisibility(View.INVISIBLE);


        menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);
        menuRed.setClosedOnTouchOutside(true);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);


        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
    }

    @Override
    public void reLoadData(View view) {
        progressBar.setVisibility(View.VISIBLE);
        error.setVisibility(View.INVISIBLE);
        getData();
    }

    private void initData() {
        datas.clear();
        SparseArray<String> array = new SparseArray<>();
        array.put(0, "建筑名称");
        array.put(1, "");
        array.put(2, "name");
        datas.add(array);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "地址");
        array1.put(1, "");
        array1.put(2, "addr");
        datas.add(array1);

        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "联系人");
        array2.put(1, "");
        array2.put(2, "leader");
        datas.add(array2);

        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "联系电话");
        array3.put(1, "");
        array3.put(2, "phone");
        datas.add(array3);


        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "文号");
        array4.put(1, "");
        array4.put(2, "number");
        datas.add(array4);

        SparseArray<String> array5 = new SparseArray<>();
        array5.put(0, "时间");
        array5.put(1, "");
        array5.put(2, "time");
        datas.add(array5);

        SparseArray<String> array6 = new SparseArray<>();
        array6.put(0, "是否合格");
        array6.put(1, "");
        array6.put(2, "isqualified");
        datas.add(array6);


        SparseArray<String> array7 = new SparseArray<>();
        array7.put(0, "文号");
        array7.put(1, "");
        array7.put(2, "cnumber");
        datas.add(array7);

        SparseArray<String> array8 = new SparseArray<>();
        array8.put(0, "时间");
        array8.put(1, "");
        array8.put(2, "completetime");
        datas.add(array8);

        SparseArray<String> array9 = new SparseArray<>();
        array9.put(0, "是否合格");
        array9.put(1, "");
        array9.put(2, "cisqualified");
        datas.add(array9);


        SparseArray<String> array10 = new SparseArray<>();
        array10.put(0, "文号");
        array10.put(1, "");
        array10.put(2, "onumber");
        datas.add(array10);

        SparseArray<String> array11 = new SparseArray<>();
        array11.put(0, "时间");
        array11.put(1, "");
        array11.put(2, "opentime");
        datas.add(array11);

        SparseArray<String> array12 = new SparseArray<>();
        array12.put(0, "是否合格");
        array12.put(1, "");
        array12.put(2, "oisqualified");
        datas.add(array12);


    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View v;
                v = LayoutInflater.from(getActivity()).inflate(R.layout.item_cominfo, parent, false);
                return new ViewHolder1(v);
            } else {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_xingzhengxuke2, parent, false);
                return new ViewHolder2(view);
            }


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder1) {
                ((ViewHolder1) holder).setData(position);
            } else {
                ((ViewHolder2) holder).setData(position);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size() + 4;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == 5 || position == 9 || position == 13)
                return 1;
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
                if (position < 5) {
                    name.setText(datas.get(position-1).get(0));
                    value.setText(datas.get(position-1).get(1));
                }else if (position<9){
                    name.setText(datas.get(position-2).get(0));
                    value.setText(datas.get(position-2).get(1));
                }else if (position<13){
                    name.setText(datas.get(position-3).get(0));
                    value.setText(datas.get(position-3).get(1));
                }else {
                    name.setText(datas.get(position-4).get(0));
                    value.setText(datas.get(position-4).get(1));
                }
            }
        }


        class ViewHolder2 extends RecyclerView.ViewHolder {
            TextView item;

            public ViewHolder2(View itemView) {
                super(itemView);
                item = (TextView) itemView.findViewById(R.id.item);
            }


            public void setData(int position) {
                if (position == 0)
                    item.setText("建筑信息");
                else if (position == 5)
                    item.setText("建审信息");
                else if (position == 9)
                    item.setText("验收信息");
                else if (position == 13)
                    item.setText("开业前");
            }
        }
    }

    public LicenseDetailInfo myInfo;
    LatLng to ;
    private void getData() {
        RetrofitHelper.getApi().getLicenseDetailInfo(((InfoDetail_activity) getActivity()).companyId)
                .compose(this.<LicenseDetailResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<LicenseDetailResult>() {
                    @Override
                    public void call(LicenseDetailResult result) {
                        if (result != null) {
                            if (!result.status.equals("0")) {
                                LicenseDetailInfo info = result.result;
                                if (info.lat!=null||info.lng!=null){
                                    try {
                                        to = new LatLng(Double.valueOf(info.lat),Double.valueOf(info.lng));
                                    }catch (Exception e){
                                        to=null;
                                    }
                                }
                                myInfo = info;
                                LogUtil.i(info.toString());
                                refreshView(myInfo);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        error.setVisibility(View.GONE);
                                    }
                                },500);
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            error.setVisibility(View.VISIBLE);
                            UiUtils.showToast(App.getApplication(), "请求失败!");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        progressBar.setVisibility(View.INVISIBLE);
                        error.setVisibility(View.VISIBLE);
                        UiUtils.showToast(App.getApplication(), "未知错误");
                    }
                });
    }
    Handler handler = new Handler();

    private void refreshView(LicenseDetailInfo info) {
        datas.get(0).put(1, info.building.name);//名字
        datas.get(1).put(1, info.building.addr);//地址
        datas.get(2).put(1, info.building.leader);
        datas.get(3).put(1, info.building.phone);


        datas.get(4).put(1, info.building.number==null?"--":info.building.number);
        datas.get(5).put(1, info.building.created_at==null?"--":info.building.created_at);
        datas.get(6).put(1, info.building.isqualified==null?"--":info.building.isqualified);

        datas.get(7).put(1, info.cnumber==null?"--":info.cnumber);
        datas.get(8).put(1, info.completetime==null?"--":info.completetime);
        datas.get(9).put(1, info.cisqualified==null?"--":info.cisqualified);


        datas.get(10).put(1, info.onumber==null?"--":info.onumber);
        datas.get(11).put(1, info.opentime==null?"--":info.opentime);
        datas.get(12).put(1, info.oisqualified==null?"--":info.oisqualified);


        myAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        if (myInfo==null){
            UiUtils.showToast(UiUtils.getContext(),"无法获取单位信息");
            return;
        }
        switch (v.getId()) {
            case R.id.fab1:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("确定删除该单位?");
                dialog.show();
                break;
            case R.id.fab2:

                Intent intent = new Intent();
                intent.setClass(getActivity(), EditLicense_activity.class);
                intent.putExtra("title", ((InfoDetail_activity) getActivity()).title);
                intent.putExtra("id", ((InfoDetail_activity) getActivity()).companyId);
                intent.putExtra("info", myInfo);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.fab3:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), CalculateRoute_Activity.class);
                if (getActivity().getIntent().getParcelableExtra("from") == null||to==null) {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog1 = builder1.create();
                    dialog1.setTitle("单位未添加地理坐标,无法导航!");
                    dialog1.show();
                    break;
                }
                intent1.putExtra("from", getActivity().getIntent().getParcelableExtra("from"));
                intent1.putExtra("to", to);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    private void delete() {
        RetrofitHelper.getApi().deleteLicense(myInfo.id)
                .compose(this.<AddLicenseResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddLicenseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast(UiUtils.getContext(),"删除失败错误");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddLicenseResult result) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                        if (result!=null){
                            if (result.status.equals("1")){
                                builder.setMessage(result.result).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                });
                                builder.create().show();
                            }else {
                                builder.setMessage(result.msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.create().show();
                            }
                        }else {
                            UiUtils.showToast(UiUtils.getContext(),"删除失败错误");
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
