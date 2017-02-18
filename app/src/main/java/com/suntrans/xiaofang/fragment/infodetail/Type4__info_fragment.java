package com.suntrans.xiaofang.fragment.infodetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.edit.EditFiregoupinfo_activity;
import com.suntrans.xiaofang.activity.mapnav.CalculateRoute_Activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.fragment.BasedFragment;
import com.suntrans.xiaofang.model.firegroup.AddFireGroupResult;
import com.suntrans.xiaofang.model.firegroup.FireGroupDetailInfo;
import com.suntrans.xiaofang.model.firegroup.FireGroupDetailResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/13.
 * 消防中队详情信息fragment
 */

public class Type4__info_fragment extends BasedFragment implements View.OnClickListener {
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter myAdapter;


//    private FloatingActionMenu menuRed;
//    private FloatingActionButton fab1;
//    private FloatingActionButton fab2;
//    private FloatingActionButton fab3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        return inflater.inflate(R.layout.fragment_info_others, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        setupToolbar(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(),LinearLayoutManager.VERTICAL));

        recyclerView.setVisibility(View.INVISIBLE);
//
//        menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);
//        menuRed.setClosedOnTouchOutside(true);
//        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
//        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
//        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
//
//
//        fab1.setOnClickListener(this);
//        fab2.setOnClickListener(this);
//        fab3.setOnClickListener(this);
    }

    @Override
    public void reLoadData(View view) {
        progressBar.setVisibility(View.VISIBLE);
        error.setVisibility(View.INVISIBLE);
        getData();
    }

    private void initData() {
        SparseArray<String> array = new SparseArray<>();
        array.put(0, "名称");
        array.put(1, "");
        datas.add(array);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "地址");
        array1.put(1, "");
        datas.add(array1);

        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "辖区面积");
        array2.put(1, "");
        datas.add(array2);


        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "消防队站联系电话");
        array3.put(1, "");
        datas.add(array3);

        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "消防队员人数");
        array4.put(1, "");
        datas.add(array4);


        SparseArray<String> array6 = new SparseArray<>();
        array6.put(0, "消防车总数");
        array6.put(1, "");
        datas.add(array6);

        SparseArray<String> array7 = new SparseArray<>();
        array7.put(0, "消防车辆配置情况");
        array7.put(1, "");
        datas.add(array7);

        SparseArray<String> array8 = new SparseArray<>();
        array8.put(0, "车载水总量（吨）");
        array8.put(1, "");
        datas.add(array8);



        SparseArray<String> array10 = new SparseArray<>();
        array10.put(0, "车载泡沫总量（吨）");
        array10.put(1, "");
        datas.add(array10);

//        SparseArray<String> array11 = new SparseArray<>();
//        array11.put(0, "所属区");
//        array11.put(1, "");
//        datas.add(array11);


        SparseArray<String> array13 = new SparseArray<>();
        array13.put(0, "所属大队");
        array13.put(1, "");
        datas.add(array13);

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
                name.setText(datas.get(position).get(0));
                value.setText(datas.get(position).get(1));
            }
        }
    }

    public FireGroupDetailInfo myInfo;
    LatLng to ;//导航的目的地
    private void getData() {
        RetrofitHelper.getApi().getFireGroupDetailInfo(((InfoDetail_activity)getActivity()).companyId)
                .compose(this.<FireGroupDetailResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<FireGroupDetailResult>() {
                    @Override
                    public void call(FireGroupDetailResult result) {
                        if (result!=null){
                            if (!result.status.equals("0")){
                                FireGroupDetailInfo info = result.result;
                                if (info.lat!=null||info.lng!=null){
                                    try {
                                        to = new LatLng(Double.valueOf(info.lat),Double.valueOf(info.lng));
                                    }catch (Exception e){
                                        to=null;
                                    }
                                }
                                myInfo=info;
                                LogUtil.i(info.toString());
                                refreshView(info);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        error.setVisibility(View.GONE);
                                    }
                                },500);
                            }
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            error.setVisibility(View.VISIBLE);
                            UiUtils.showToast(App.getApplication(),"请求失败!");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        progressBar.setVisibility(View.INVISIBLE);
                        error.setVisibility(View.VISIBLE);
                        UiUtils.showToast(App.getApplication(),"服务器错误");
                    }
                });
    }

    private void refreshView(FireGroupDetailInfo info) {
        datas.get(0).put(1,info.name);//名字
        datas.get(1).put(1,info.addr);//地址
        datas.get(2).put(1,info.area==null?"":info.area+"平方公里");
        datas.get(3).put(1,info.phone==null?"":info.phone);

        String a = info.membernum;
        String renyuan="";
        if (a!=null){
            renyuan=Utils.parseJson(a);
            datas.get(4).put(1,renyuan==null?a:renyuan);
        }
        datas.get(5).put(1,info.carnum==null?"":info.carnum);

        String b= info.cardisp;
        String cardis="";
        if (b!=null){
            cardis = Utils.parseJson(b);
            datas.get(6).put(1,cardis==null?"":cardis);
        }

        datas.get(7).put(1,info.waterweight==null?"":info.waterweight);
        datas.get(8).put(1,info.soapweight==null?"":info.soapweight);
        datas.get(9).put(1,info.brigade_name==null?"":info.brigade_name);
        myAdapter.notifyDataSetChanged();
    }

    Handler handler = new Handler();


    @Override
    public void onClick(View v) {
        if (myInfo==null){
            UiUtils.showToast(UiUtils.getContext(),"无法获取单位信息");
            return;
        }
        switch (v.getId()){
//            case R.id.fab1:
//                final AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        delete();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog dialog =builder.create();
//                dialog.setTitle("确定删除该单位?");
//                dialog.show();
//                break;
//            case R.id.fab2:
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), EditFiregoupinfo_activity.class);
//                intent.putExtra("title",title);
////                intent.putExtra("id",((InfoDetail_activity)getActivity()).companyId);
////                intent.putExtra("from",getActivity().getIntent().getParcelableExtra("from"));
//                intent.putExtra("info",info);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//            case R.id.fab3:
//                Intent intent1 = new Intent();
//                intent1.setClass(getActivity(),CalculateRoute_Activity.class);
//                if (getActivity().getIntent().getParcelableExtra("from")==null||to==null){
//                    final AlertDialog.Builder builder1 =new AlertDialog.Builder(getActivity());
//                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    AlertDialog dialog1 =builder1.create();
//                    dialog1.setTitle("单位未添加地理坐标,无法导航!");
//                    dialog1.show();
//                    break;
//                }
//                intent1.putExtra("from",getActivity().getIntent().getParcelableExtra("from"));
//                intent1.putExtra("to",to);
//                startActivity(intent1);
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
        }
    }


    private void delete() {
        RetrofitHelper.getApi().deleteGroup(myInfo.id)
                .compose(this.<AddFireGroupResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddFireGroupResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast(UiUtils.getContext(),"删除失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddFireGroupResult result) {
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
                            UiUtils.showToast("删除失败");
                        }
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    public Toolbar toolbar;
    public String title;
    private void setupToolbar(View view) {
        setHasOptionsMenu(true);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        title = getActivity().getIntent().getStringExtra("name").split("#")[0];
        toolbar.setTitle(title);
        ((InfoDetail_activity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar =((InfoDetail_activity)getActivity()). getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.delete:
                if (myInfo==null){
                    UiUtils.showToast(UiUtils.getContext(),"无法获取单位信息");
                    break;
                }
                final AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
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
                AlertDialog dialog =builder.create();
                dialog.setTitle("确定删除该单位?");
                dialog.show();
                break;
            case R.id.gohere:
                if (myInfo==null){
                    UiUtils.showToast(UiUtils.getContext(),"无法获取单位信息");
                    break;
                }
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(),CalculateRoute_Activity.class);
                if (getActivity().getIntent().getParcelableExtra("from")==null||to==null){
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
                intent1.putExtra("to",to);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.xiugai:
                if (myInfo==null){
                    UiUtils.showToast(UiUtils.getContext(),"无法获取单位信息");
                    break;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), EditFiregoupinfo_activity.class);
                intent.putExtra("title",title);
//                intent.putExtra("id",((InfoDetail_activity)getActivity()).companyId);
//                intent.putExtra("from",getActivity().getIntent().getParcelableExtra("from"));
                intent.putExtra("info",myInfo);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detailinfo,menu);
    }
}
