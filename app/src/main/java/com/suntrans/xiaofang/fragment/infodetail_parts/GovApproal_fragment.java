package com.suntrans.xiaofang.fragment.infodetail_parts;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.edit.EditLicense_activity;
import com.suntrans.xiaofang.activity.mapnav.CalculateRoute_Activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.activity.others.Search_license_activity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.fragment.BasedFragment;
import com.suntrans.xiaofang.model.company.CompanyLicenseInfo;
import com.suntrans.xiaofang.model.company.CompanyLicenseResult;
import com.suntrans.xiaofang.model.license.AddLicenseResult;
import com.suntrans.xiaofang.model.license.LicenseDetailInfo;
import com.suntrans.xiaofang.model.license.LicenseDetailResult;
import com.suntrans.xiaofang.model.license.LicenseItemInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.FragmentEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.R.id.fab3;
import static com.suntrans.xiaofang.utils.Utils.pad;

/**
 * Created by Looney on 2017/1/9.
 */

public class GovApproal_fragment extends BasedFragment implements View.OnClickListener {

    private static final String TAG = "GovApproal_fragment";
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private GovApproal_fragment.MyAdapter myAdapter;

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;


    private int companyType;


    public static GovApproal_fragment newInstance(int companyType) {
        GovApproal_fragment fragment = new GovApproal_fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("companyType", companyType);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        companyType = getArguments().getInt("companyType");
        return inflater.inflate(R.layout.fragment_company_govapproal, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setVisibility(View.INVISIBLE);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public void reLoadData(View view) {
        progressBar.setVisibility(View.VISIBLE);
        error.setVisibility(View.INVISIBLE);
        getData(id);
    }

    private void initData() {


    }

    int mYear;
    int mMonth;
    int mDay;


    private boolean hasBinding = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab1:
                addCompanyLicense();
                break;
            case R.id.fab2:
                if (hasBinding){
                    //若已经绑定项目 可以绑定或者解绑 或者更改
                }else {
                    Intent intent2 = new Intent();
                    intent2.putExtra("companyID", id);
                    intent2.putExtra("type", MarkerHelper.S0CIETY);
                    intent2.setClass(getActivity(), Search_license_activity.class);
                    startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
    }

    private void addCompanyLicense() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.add_license_item, null);
        view.findViewById(R.id.time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        ((TextView) v).setText(
                                new StringBuilder()
                                        .append(mYear).append("-")
                                        .append(pad(mMonth + 1)).append("-")
                                        .append(pad(mDay))
                        );
                    }
                }, mYear, mMonth, mDay);
                pickerDialog.show();
            }
        });
        builder2.setView(view)
                .setTitle("新增行政审批信息")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int typeid = ((RadioGroup) view.findViewById(R.id.radioGroup_leixing)).getCheckedRadioButtonId();
                        int hegeid = ((RadioGroup) view.findViewById(R.id.radioGroup_hege)).getCheckedRadioButtonId();
                        String number1 = ((EditText) view.findViewById(R.id.number)).getText().toString();
                        String time1 = ((TextView) view.findViewById(R.id.time)).getText().toString();
                        String type = "";
                        String isq = "";
                        if (typeid == R.id.jianshen) {
                            type = "1";
                        } else if (typeid == R.id.yanshou) {
                            type = "2";
                        } else if (typeid == R.id.kaiye) {
                            type = "3";
                        }
                        if (hegeid == R.id.radio_hege) {
                            isq = "1";
                        } else if (hegeid == R.id.radio_buhege) {
                            isq = "0";
                        }
                        dialog.dismiss();
                        addCompantLicenseDetail(type, number1, time1, isq);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder2.create().show();
    }

    private void addCompantLicenseDetail(String type, String number1, String time1, String isq) {

    }


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View v;
                v = LayoutInflater.from(getActivity()).inflate(R.layout.item_cominfo, parent, false);
                return new GovApproal_fragment.MyAdapter.ViewHolder1(v);
            } else if (viewType == 1) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_xingzhengxuke2, parent, false);
                return new GovApproal_fragment.MyAdapter.ViewHolder2(view);
            } else if (viewType == 2) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_activity2, parent, false);
                return new GovApproal_fragment.MyAdapter.ViewHolder3(view);
            } else {
                return null;
            }


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof GovApproal_fragment.MyAdapter.ViewHolder1) {
                ((GovApproal_fragment.MyAdapter.ViewHolder1) holder).setData(position);
            } else if (holder instanceof GovApproal_fragment.MyAdapter.ViewHolder2) {
                ((GovApproal_fragment.MyAdapter.ViewHolder2) holder).setData(position);
            } else if (holder instanceof GovApproal_fragment.MyAdapter.ViewHolder3) {
                ((GovApproal_fragment.MyAdapter.ViewHolder3) holder).setData(position);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size() == 0 ? 1 : datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == datas.size())
                return 2;
            if (position < 5)
                return 0;
            return 1;
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

        class ViewHolder3 extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder3(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.value);
            }

            public void setData(int position) {
                textView.setText("单位无绑定行政审批项目");
            }
        }


        class ViewHolder2 extends RecyclerView.ViewHolder {
            TextView title;
            TextView isq;
            TextView number;
            TextView time;

            public ViewHolder2(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                isq = (TextView) itemView.findViewById(R.id.isqualified);
                number = (TextView) itemView.findViewById(R.id.number);
                time = (TextView) itemView.findViewById(R.id.time);
            }

            public void setData(int position) {
                title.setText(datas.get(position).get(0));
                number.setText(datas.get(position).get(1));
                time.setText(datas.get(position).get(2));
                isq.setText(datas.get(position).get(3));
            }
        }
    }

    public List<CompanyLicenseInfo> infos;
    LatLng to;

    private void getData(String id) {
        if (companyType == MarkerHelper.S0CIETY){
            RetrofitHelper.getApi().getCompanyLicense(id)
                    .compose(this.<CompanyLicenseResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<CompanyLicenseResult>() {
                        @Override
                        public void call(CompanyLicenseResult result) {
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    GovApproal_fragment.this.infos = result.result;
                                    refreshView(GovApproal_fragment.this.infos);
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        error.setVisibility(View.GONE);
                                    }
                                }, 500);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                error.setVisibility(View.VISIBLE);
                                UiUtils.showToast(App.getApplication(), "请求失败!");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                            error.setVisibility(View.VISIBLE);
                            UiUtils.showToast(App.getApplication(), "请求失败");
                        }
                    });
        }else {
            RetrofitHelper.getApi().getCommcmyLicense(id)
                    .compose(this.<CompanyLicenseResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<CompanyLicenseResult>() {
                        @Override
                        public void call(CompanyLicenseResult result) {
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    GovApproal_fragment.this.infos = result.result;
                                    refreshView(GovApproal_fragment.this.infos);
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        error.setVisibility(View.GONE);
                                    }
                                }, 500);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                error.setVisibility(View.VISIBLE);
                                UiUtils.showToast(App.getApplication(), "请求失败!");
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                            error.setVisibility(View.VISIBLE);
                            UiUtils.showToast(App.getApplication(), "请求失败");
                        }
                    });
        }

    }

    Handler handler = new Handler();
    List<String> licenseId = new ArrayList<>();

    private void refreshView(List<CompanyLicenseInfo> infos) {
        datas.clear();
        licenseId.clear();
        if (infos != null) {
            if (infos.size() != 0) {

                SparseArray<String> array0 = new SparseArray<>();
                array0.put(0, "建设单位");
                array0.put(1, infos.get(0).license.cmyname == null ? "--" : infos.get(0).license.cmyname);
                array0.put(2, "cmyname");
                datas.add(array0);

                SparseArray<String> array4 = new SparseArray<>();
                array4.put(0, "名称");
                array4.put(1, infos.get(0).license.name == null ? "--" : infos.get(0).license.name);
                array4.put(2, "name");
                datas.add(array4);

                SparseArray<String> array1 = new SparseArray<>();
                array1.put(0, "地址");
                array1.put(1, infos.get(0).license.addr == null ? "--" : infos.get(0).license.addr);
                array1.put(2, "addr");
                datas.add(array1);

                SparseArray<String> array2 = new SparseArray<>();
                array2.put(0, "联系人");
                array2.put(1, infos.get(0).license.contact == null ? "--" : infos.get(0).license.contact);
                array2.put(2, "contact");
                datas.add(array2);

                SparseArray<String> array3 = new SparseArray<>();
                array3.put(0, "联系电话");
                array3.put(1, infos.get(0).license.phone == null ? "--" : infos.get(0).license.phone);
                array3.put(2, "phone");
                datas.add(array3);

                for (CompanyLicenseInfo item :
                        infos) {
                    String id = item.id;
                    licenseId.add(id);
                    LogUtil.i(TAG, item.toString());
                    SparseArray<String> array = new SparseArray<>();
                    if (item.type.equals("1")) {
                        array.put(0, "建审");
                    } else if (item.type.equals("2")) {
                        array.put(0, "验收");
                    } else if (item.type.equals("3")) {
                        array.put(0, "开业前");
                    }
                    if (item.number != null)
                        array.put(1, item.number);
                    if (item.time != null)
                        array.put(2, item.time);
                    if (item.isqualified != null)
                        array.put(3, item.isqualified.equals("1") ? "合格" : "不合格");
                    datas.add(array);
                }
            }

        }
        myAdapter = null;
        myAdapter = new GovApproal_fragment.MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    public Toolbar toolbar;
    public String title;


    String id;

    public void setId(String id) {
        this.id = id;
        getData(id);
    }

    /**
     * 获取绑定行政审批的id
     */
    public String getLicenseId() {
        if (infos == null) {
            return null;
        } else if (infos.size() == 0) {
            return null;
        } else {
            return infos.get(0).license.id;
        }
    }


    /**
     * 获取绑定行政审批条目的所有
     */
    public String getLicenseItemId() {
        if (infos == null) {
            return null;
        } else if (infos.size() == 0) {
            return null;
        } else {
            if (licenseId.size() != 0) {
                JSONArray array = new JSONArray();
                for (String id :
                        licenseId) {
                    array.put(id);
                }
                return array.toString();
            } else {
                return null;
            }

        }
    }

}
