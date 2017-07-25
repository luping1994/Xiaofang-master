package com.suntrans.xiaofang.fragment.infodetail_parts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.edit.EditCommcmyInfo_activity;
import com.suntrans.xiaofang.activity.edit.EditCompanyInfo_activity;
import com.suntrans.xiaofang.activity.mapnav.CalculateRoute_Activity;
import com.suntrans.xiaofang.activity.others.CmyQRCodeActivity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.company.CompanyDetailnfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.DbHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.R.id.lat;
import static com.suntrans.xiaofang.R.id.lng;

/**
 * Created by Looney on 2016/12/22.
 */

public class DetailInfoFragment extends RxFragment implements View.OnClickListener {

    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter myAdapter;


    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private CompanyDetailnfo myInfo;


    public static DetailInfoFragment newInstance() {
        DetailInfoFragment fragment = new DetailInfoFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailinfo, container, false);
        initData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.i("DetailInfoFragment==>onViewVreated");

        menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);

        view.findViewById(R.id.fab4).setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        System.out.println("onActivityCreated");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        System.out.println("onAttach");

    }

    private void initData() {
        SparseArray<String> array = new SparseArray<>();
        array.put(0, "单位名称");
        array.put(1, "");
        datas.add(array);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "单位地址");
        array1.put(1, "");
        datas.add(array1);


        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "火灾危险性");
        array2.put(1, "");
        datas.add(array2);


        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "单位属性");
        array3.put(1, "");
        datas.add(array3);

        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "消防管辖");
        array4.put(1, "");
        datas.add(array4);


        SparseArray<String> array5 = new SparseArray<>();
        array5.put(0, "建筑面积");
        array5.put(1, "");
        datas.add(array5);

        SparseArray<String> array6 = new SparseArray<>();
        array6.put(0, "安全出口数");
        array6.put(1, "");
        datas.add(array6);

        SparseArray<String> array7 = new SparseArray<>();
        array7.put(0, "疏散楼梯数");
        array7.put(1, "");
        datas.add(array7);

        SparseArray<String> array8 = new SparseArray<>();
        array8.put(0, "自动消防设施");
        array8.put(1, "");
        datas.add(array8);



        SparseArray<String> array9 = new SparseArray<>();
        array9.put(0, "法定代表人");
        array9.put(1, "");
        datas.add(array9);

        SparseArray<String> array10 = new SparseArray<>();
        array10.put(0, "法定代表人身份证");
        array10.put(1, "");
        datas.add(array10);


        SparseArray<String> array11 = new SparseArray<>();
        array11.put(0, "法定代表人电话");
        array11.put(1, "");
        datas.add(array11);

        SparseArray<String> array12 = new SparseArray<>();
        array12.put(0, "消防安全管理人");
        array12.put(1, "");
        datas.add(array12);

        SparseArray<String> array13 = new SparseArray<>();
        array13.put(0, "消防安全管理人身份证");
        array13.put(1, "");
        datas.add(array13);


        SparseArray<String> array14 = new SparseArray<>();
        array14.put(0, "消防安全管理人电话");
        array14.put(1, "");
        datas.add(array14);

        SparseArray<String> array15 = new SparseArray<>();
        array15.put(0, "消防安全责任人");
        array15.put(1, "");
        datas.add(array15);


        SparseArray<String> array16 = new SparseArray<>();
        array16.put(0, "消防安全责任人身份证");
        array16.put(1, "");
        datas.add(array16);

        SparseArray<String> array17 = new SparseArray<>();
        array17.put(0, "责任人电话");
        array17.put(1, "");
        datas.add(array17);


        SparseArray<String> array18 = new SparseArray<>();
        array18.put(0, "单位编码");
        array18.put(1, "");
        datas.add(array18);


        SparseArray<String> array19 = new SparseArray<>();
        array19.put(0, "组织机构代码");
        array19.put(1, "");
        datas.add(array19);

        SparseArray<String> array20 = new SparseArray<>();
        array20.put(0, "单位其它情况");
        array20.put(1, "");
        datas.add(array20);

        SparseArray<String> array21 = new SparseArray<>();
        array21.put(0, "上级单位");
        array21.put(1, "");
        datas.add(array21);

        SparseArray<String> array22 = new SparseArray<>();
        array22.put(0, "单位成立时间");
        array22.put(1, "");
        datas.add(array22);


        SparseArray<String> array23 = new SparseArray<>();
        array23.put(0, "单位电话");
        array23.put(1, "");
        datas.add(array23);

        SparseArray<String> array24 = new SparseArray<>();
        array24.put(0, "职工人数");
        array24.put(1, "");
        datas.add(array24);

        SparseArray<String> array25 = new SparseArray<>();
        array25.put(0, "占地面积");
        array25.put(1, "");
        datas.add(array25);
//
        SparseArray<String> array26 = new SparseArray<>();
        array26.put(0, "消防员人数");
        array26.put(1, "");
        datas.add(array26);


        SparseArray<String> array27 = new SparseArray<>();
        array27.put(0, "消防车道数");
        array27.put(1, "");
        datas.add(array27);

        SparseArray<String> array28 = new SparseArray<>();
        array28.put(0, "消防电梯数");
        array28.put(1, "");
        datas.add(array28);


        SparseArray<String> array29 = new SparseArray<>();
        array29.put(0, "消防车道类型");
        array29.put(1, "");
        datas.add(array29);

        SparseArray<String> array30 = new SparseArray<>();
        array30.put(0, "避难层数");
        array30.put(1, "");
        datas.add(array30);

        SparseArray<String> array31 = new SparseArray<>();
        array31.put(0, "避难层位置");
        array31.put(1, "");
        datas.add(array31);

        SparseArray<String> array32 = new SparseArray<>();
        array32.put(0, "单位毗邻情况");
        array32.put(1, "");
        datas.add(array32);

        SparseArray<String> array33= new SparseArray<>();
        array33.put(0, "备注");
        array33.put(1, "");
        datas.add(array33);
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


    public void setData(CompanyDetailnfo info) {
        this.myInfo = info;
        refreshView(info);
    }

    private void refreshView(CompanyDetailnfo info) {
        datas.get(0).put(1, info.name);//名字
        datas.get(1).put(1, info.addr);//地址
        String dangerlevels = "";
        if (info.dangerlevel != null) {
            if (info.dangerlevel.equals("1")) {
                dangerlevels = "火灾高危单位";
            } else if (info.dangerlevel.equals("2")) {
                dangerlevels = "一般消防安全重点单位";
            } else if (info.dangerlevel.equals("3")) {
                dangerlevels = "十小场所";
            } else if (info.dangerlevel.equals("4")) {
                dangerlevels = "其他非重点单位";
            }
        }
        datas.get(2).put(1, dangerlevels);


        datas.get(3).put(1, "");
        String mainId = info.mainattribute;
        String mainId_small = info.mainattribute_small;
        if (info.special.equals("1")) {
            if (mainId != null) {
                if (info.special != null) {
                    if (info.special.equals("1")) {
                        StringBuilder sb = new StringBuilder();
                        DbHelper helper = new DbHelper(getActivity(), "Fire", null, 1);
                        SQLiteDatabase db = helper.getReadableDatabase();
                        db.beginTransaction();
                        Cursor cursor = db.rawQuery("select Name from attr_main where Id=?", new String[]{mainId});
                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                sb.append(cursor.getString(0));
                            }
                        }
                        if (info.subattribute != null) {
                            Cursor cursor2 = db.rawQuery("select Name from attr_sub where Id=?", new String[]{info.subattribute});
                            if (cursor2.getCount() > 0) {
                                while (cursor2.moveToNext()) {
                                    sb.append("(")
                                            .append(cursor2.getString(0))
                                            .append(")");
                                }
                            }
                            cursor2.close();
                        }
                        String attr = sb.toString();
                        if (Utils.isVaild(attr))
                            datas.get(3).put(1, attr);
                        cursor.close();
                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }
                }

            }
        } else {
            if (mainId_small != null) {
                String[] ids = mainId_small.split("#");
                String attr = "";
                DbHelper helper = new DbHelper(getActivity(), "Fire", null, 1);
                SQLiteDatabase db = helper.getReadableDatabase();
                db.beginTransaction();
                for (int i = 0; i < ids.length; i++) {
                    if (info.special.equals("0")) {

                        Cursor cursor = db.rawQuery("select Name from attr_general where Id=?", new String[]{ids[i]});
                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                attr += cursor.getString(0);
                            }
                        }
                        cursor.close();

                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                datas.get(3).put(1, attr);

            }
        }

        datas.get(4).put(1, info.incharge == null ? "" : info.incharge);

        datas.get(5).put(1, info.buildarea == null ? "" : info.buildarea + "平方米");
        datas.get(6).put(1, info.exitnum == null ? "" : info.exitnum + "个");
        datas.get(7).put(1, info.stairnum == null ? "" : info.stairnum + "个");

        if (info.facility != null) {
            String s = "";
            s = info.facility;
            if (s.startsWith("#")) {
                s = s.substring(1, s.length());
            }
            if (s.endsWith("#")) {
                s = s.substring(0, s.length() - 1);
            }
            datas.get(8).put(1, s.replace("#","、"));//数据库暂无该字段
        }


        datas.get(9).put(1, info.artiname == null ? "" : info.artiname);
        datas.get(10).put(1, info.artiid == null ? "" : info.artiid);
        datas.get(11).put(1, info.artiphone == null ? "" : info.artiphone);

        datas.get(12).put(1, info.managername == null ? "" : info.managername);
        datas.get(13).put(1, info.managerid == null ? "" : info.managerid);
        datas.get(14).put(1, info.managerphone == null ? "" : info.managerphone);

        datas.get(15).put(1, info.responname == null ? "" : info.responname);
        datas.get(16).put(1, info.responid == null ? "" : info.responid);
        datas.get(17).put(1, info.responphone == null ? "" : info.responphone);

        datas.get(18).put(1, info.companyid == null ? "" : info.companyid);

        datas.get(19).put(1, info.orgid == null ? "" : info.orgid);
        datas.get(20).put(1, info.otherdisp == null ? "" : info.otherdisp);

        datas.get(21).put(1, info.leaderdepart == null ? "" : info.leaderdepart);
        datas.get(22).put(1, info.foundtime == null ? "" : info.foundtime);
        datas.get(23).put(1, info.phone == null ? "" : info.phone);
        datas.get(24).put(1, info.staffnum == null ? "" : info.staffnum + "人");
        datas.get(25).put(1, info.area == null ? "" : info.area + "平方米");
        datas.get(26).put(1, info.firemannum == null ? "" : info.firemannum + "人");
        datas.get(27).put(1, info.lanenum == null ? "" : info.lanenum + "个");
        datas.get(28).put(1, info.elevatornum == null ? "" : info.elevatornum + "个");

        if (info.lanepos != null) {
            String s = "";
            s = info.lanepos;
            if (s.startsWith("#")) {
                s = s.substring(1, s.length());
            }
            if (s.endsWith("#")) {
                s = s.substring(0, s.length() - 1);
            }
            datas.get(29).put(1, s.replace("#","、"));//数据库暂无该字段
        }
        datas.get(30).put(1, info.refugenum == null ? "" : info.refugenum + "层");
        datas.get(31).put(1, info.refugepos == null ? "" : info.refugepos + "");
//        if (info.east == null && info.west == null && info.south == null && info.north == null) {
//            datas.get(30).put(1, "");
//        } else {
//
//            datas.get(30).put(1,"东:"+ info.east + "\n" + "西:" + info.west + "\n" + "南:" + info.south + "\n" + "北:" + info.north);
//        }
        if (info.nearby != null) {
            datas.get(32).put(1, info.nearby);
        }
        datas.get(33).put(1, info.remark);

        myAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab4:
                if (myInfo == null) {
                    UiUtils.showToast(UiUtils.getContext(), "无法获取单位信息");
                    break;
                }
                Intent intent = new Intent(getActivity(), CmyQRCodeActivity.class);
                intent.putExtra("url",myInfo.url);
                intent.putExtra("name",myInfo.name);
                startActivity(intent);
                break;
            case R.id.fab1:
                if (myInfo == null) {
                    UiUtils.showToast(UiUtils.getContext(), "无法获取单位信息");
                    break;
                }
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
                editInfo();
                break;
            case R.id.fab3:
                if (myInfo == null) {
                    UiUtils.showToast(UiUtils.getContext(), "无法获取单位信息");
                    break;
                }
                LatLng to = null;
                if (Utils.isVaild(myInfo.lat) && Utils.isVaild(myInfo.lng))
                    to = new LatLng(Double.valueOf(myInfo.lat), Double.valueOf(myInfo.lng));

                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), CalculateRoute_Activity.class);
                if (getActivity().getIntent().getParcelableExtra("from") == null || to == null) {
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog1 = builder1.create();
                    dialog1.setTitle("无法获取当前位置或单位未添加地理坐标,无法导航!");
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
        if (myInfo.special.equals("1")) {
            RetrofitHelper.getApi().deleteCompany(myInfo.id)
                    .compose(this.<AddCompanyResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            UiUtils.showToast(UiUtils.getContext(), "删除失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    sendBroadcast();
                                    builder.setMessage(result.result).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    });
                                    builder.create().show();
                                } else {
                                    builder.setMessage(result.msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.create().show();
                                }
                            } else {
                                UiUtils.showToast("删除失败");
                            }
                        }
                    });
        } else if (myInfo.special.equals("0")) {
            RetrofitHelper.getApi().deleteCommCompany(myInfo.id)
                    .compose(this.<AddCompanyResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            UiUtils.showToast(UiUtils.getContext(), "删除失败");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    sendBroadcast();
                                    builder.setMessage(result.result).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    });
                                    builder.create().show();
                                } else {
                                    builder.setMessage(result.msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.create().show();
                                }
                            } else {
                                UiUtils.showToast("删除失败");
                            }
                        }
                    });
        }
    }

    public void editInfo() {

        if (myInfo == null) {
            UiUtils.showToast(UiUtils.getContext(), "无法获取单位信息");
            return;
        }
        Intent intent = new Intent();
        if (myInfo.special.equals("1")) {

            intent.setClass(getActivity(), EditCompanyInfo_activity.class);
        } else if (myInfo.special.equals("0")) {
            intent.setClass(getActivity(), EditCommcmyInfo_activity.class);

        } else {
            UiUtils.showToast("单位未添加单位属性");
            return;
        }
        intent.putExtra("title", myInfo.name);
        intent.putExtra("info", myInfo);

        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction("net.suntrans.xiaofang.lp");
        if (myInfo.special.equals("1")) {
            intent.putExtra("type", MarkerHelper.S0CIETY);
        } else {
            intent.putExtra("type", MarkerHelper.COMMONCOMPANY);

        }
        getActivity().sendBroadcast(intent);
    }
}
