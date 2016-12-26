package com.suntrans.xiaofang.fragment.infodetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.edit.EditCompanyInfo_activity;
import com.suntrans.xiaofang.activity.mapnav.CalculateRoute_Activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.fragment.infodetail_parts.DetailInfoFragment;
import com.suntrans.xiaofang.fragment.infodetail_parts.EventFragment;
import com.suntrans.xiaofang.fragment.infodetail_parts.SuperviseFragment;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.company.CompanyDetailnfo;
import com.suntrans.xiaofang.model.company.CompanyDetailnfoResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/13.
 * 社会单位详情fragment
 */

public class Type1__info_fragment extends Fragment implements View.OnClickListener {
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();

//    private RecyclerView recyclerView;
//    private LinearLayoutManager manager;
//    private MyAdapter myAdapter;
//    private CompanyDetailnfo data;

    private FloatingActionMenu menuRed;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;


    private TabLayout tabLayout;
    private ViewPager pager;
    private DetailInfoFragment detailInfoFragment0;
    private EventFragment eventFragment;
    private SuperviseFragment superviseFragment;


    LatLng to ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_type1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


//        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
//        manager = new LinearLayoutManager(getActivity());
//        myAdapter = new MyAdapter();
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(myAdapter);
//        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
//


        menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);
        menuRed.setClosedOnTouchOutside(true);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(pager);

    }
    class PagerAdapter extends FragmentStatePagerAdapter {
        String[] titles = new String[]{"单位信息","单位事件","单位监督"};
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if (detailInfoFragment0==null)
                    detailInfoFragment0 = new DetailInfoFragment();
                    return detailInfoFragment0;
                case 1:
                    if (eventFragment==null){
                        eventFragment = new EventFragment();
                    }
                    return eventFragment;
                case 2:
                    if (superviseFragment==null){
                        superviseFragment = new SuperviseFragment();
                    }
                    return superviseFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }



    public CompanyDetailnfo myInfo;
    //获取单位详情信息
    private void getData() {
        RetrofitHelper.getApi().getCompanyDetail(((InfoDetail_activity) getActivity()).companyId).enqueue(new Callback<CompanyDetailnfoResult>() {
            @Override
            public void onResponse(Call<CompanyDetailnfoResult> call, Response<CompanyDetailnfoResult> response) {
                CompanyDetailnfoResult result = response.body();
                if (result != null) {
                    if (!result.status.equals("0")) {
                        CompanyDetailnfo info = result.info;
                        if (info.lat!=null||info.lng!=null){
                            try {
                                to = new LatLng(Double.valueOf(info.lat),Double.valueOf(info.lng));
                            }catch (Exception e){
                                to=null;
                            }
                        }
                        myInfo = info;
//                        System.out.println(info.toString());
                        detailInfoFragment0.setData(info);
                        eventFragment.setId(info.id);
                        superviseFragment.setId(info.id);


                    }
                } else {
                    UiUtils.showToast(App.getApplication(), "请求失败!");
                }
            }


            @Override
            public void onFailure(Call<CompanyDetailnfoResult> call, Throwable t) {
                UiUtils.showToast(App.getApplication(), "请求失败!");
            }
        });
    }


    @Override
    public void onClick(View v) {
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
                intent.setClass(getActivity(), EditCompanyInfo_activity.class);
                intent.putExtra("title", ((InfoDetail_activity) getActivity()).title);
                intent.putExtra("info", myInfo);
//                intent.putExtra("id", ((InfoDetail_activity) getActivity()).companyId);
//                intent.putExtra("from", getActivity().getIntent().getParcelableExtra("from"));
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
        RetrofitHelper.getApi().deleteCompany(myInfo.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddCompanyResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast(UiUtils.getContext(),"删除失败错误");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddCompanyResult result) {
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

}
