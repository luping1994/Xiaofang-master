package com.suntrans.xiaofang.fragment.infodetail_parts;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.edit.EditCompanyLicense_activity;
import com.suntrans.xiaofang.fragment.BasedFragment;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.company.CompanyLicenseInfo;
import com.suntrans.xiaofang.model.company.CompanyLicenseResult;
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

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.R.id.cmyname;
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

//        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
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
        switch (v.getId()) {
            case R.id.fab1:
                addCompanyLicense();
                break;
            case R.id.fab2:

                Intent intent2 = new Intent();
                intent2.putExtra("companyID", id);
                intent2.putExtra("type", companyType);
                intent2.putParcelableArrayListExtra("companyLicenseItems", comLicenseLists);
                intent2.putExtra("projectInfo", projectInfo);
                intent2.setClass(getActivity(), EditCompanyLicense_activity.class);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    private void addCompantLicenseDetail(String type1, String number1, String time1, String isqualified1) {

        if (!Utils.isVaild(number1)) {
            UiUtils.showToast("请输入文号");
            return;
        }
        if (!Utils.isVaild(time1)) {
            UiUtils.showToast("请选择时间");
            return;
        }
        String info1 = "";
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            object.put("number", number1);
            object.put("time", time1);
            object.put("isqualified", isqualified1);
            object.put("type", type1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(object);
        info1 = array.toString();

        LogUtil.i("id=" + id + "info=" + info1);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在添加,请稍后..");
        progressDialog.show();
        if (companyType == MarkerHelper.S0CIETY) {
            RetrofitHelper.getApi().addCompanyLicnese(id, info1)
                    .compose(this.<AddCompanyResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    UiUtils.showToast(result.result);
                                    getData(id);
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            } else {
                                UiUtils.showToast("失败");
                            }

                        }
                    });
        } else if (companyType == MarkerHelper.COMMONCOMPANY) {
            RetrofitHelper.getApi().addCommcmyLicnese(id, info1)
                    .compose(this.<AddCompanyResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    UiUtils.showToast(result.result);
                                    getData(id);
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            } else {
                                UiUtils.showToast("失败");
                            }

                        }
                    });
        }
    }


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View v;
                v = LayoutInflater.from(getActivity()).inflate(R.layout.item_xingzhengxuke_project, parent, false);
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
            return datas.size()==0?1:datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (datas.size()==0)
                return 2;
            return 1;
        }


        class ViewHolder1 extends RecyclerView.ViewHolder {
            TextView title;
            TextView isq;
            TextView number;
            TextView time;
            TextView header;
            CardView cardview;
            LinearLayout ll_wu;
            LinearLayout ll_you;

            public ViewHolder1(View itemView) {
                super(itemView);

            }

            public void setData(int position) {


            }
        }

        class ViewHolder3 extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder3(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.value);
            }

            public void setData(int position) {
                textView.setText("无行政审批数据");
            }
        }


        class ViewHolder2 extends RecyclerView.ViewHolder {
            TextView title;
            TextView isq;
            TextView number;
            TextView time;
            TextView header;
            CardView cardview;

            public ViewHolder2(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                isq = (TextView) itemView.findViewById(R.id.isqualified);
                number = (TextView) itemView.findViewById(R.id.number);
                time = (TextView) itemView.findViewById(R.id.time);
                header = (TextView) itemView.findViewById(R.id.header);
                cardview = (CardView) itemView.findViewById(R.id.cardview);
                cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            public void setData(int position) {
                if (datas.get(position).get(5).equals("company")) {
                    title.setText(datas.get(position).get(0));
                    number.setText(datas.get(position).get(1));
                    time.setText(datas.get(position).get(2));
                    isq.setText(datas.get(position).get(3));
                    int pos = projectInfo==null?0:projectInfo.detail==null?0:projectInfo.detail.size();
                    if (position == pos) {
                        header.setVisibility(View.VISIBLE);
                        header.setText("添加的审批信息");

                    } else {
                        header.setVisibility(View.GONE);
                    }
                } else if (datas.get(position).get(5).equals("project")){
                    title.setText(datas.get(position).get(0));
                    number.setText(datas.get(position).get(1));
                    time.setText(datas.get(position).get(2));
                    isq.setText(datas.get(position).get(3));
                    if (position == 0) {
                        header.setText("绑定的项目名称:"+datas.get(position).get(4));
                        header.setVisibility(View.VISIBLE);
                    } else {
                        header.setVisibility(View.GONE);
                    }
                }

            }
        }
    }

    public CompanyLicenseInfo infos;
    LatLng to;

    private void getData(String id) {
        if (companyType == MarkerHelper.S0CIETY) {
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
        } else {
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
    private CompanyLicenseInfo.License projectInfo = null;
    private ArrayList<CompanyLicenseInfo.CompanyLicenseItem> comLicenseLists = null;

    private void refreshView(CompanyLicenseInfo infos) {
        datas.clear();
        projectInfo = null;
        comLicenseLists = null;
        if (infos != null) {
            if (infos.project != null) {
                if (infos.project.size() != 0) {
                    this.projectInfo = infos.project.get(0);
                    CompanyLicenseInfo.License item = infos.project.get(0);
                    if (item.detail != null) {
                        for (CompanyLicenseInfo.CompanyLicenseItem ci : item.detail) {
                            SparseArray<String> array = new SparseArray<>();

                            if (ci.type.equals("1")) {
                                array.put(0, "建审");
                            } else if (ci.type.equals("2")) {
                                array.put(0, "验收");
                            } else if (ci.type.equals("3")) {
                                array.put(0, "开业前");
                            }
                            if (ci.number != null)
                                array.put(1, ci.number);
                            if (ci.time != null)
                                array.put(2, ci.time);
                            if (ci.isqualified != null)
                                array.put(3, ci.isqualified.equals("1") ? "合格" : "不合格");

                            array.put(4, item.name == null ? "" : item.name);
                            array.put(5, "project");
                            datas.add(array);
                        }
                    }

                } else {
//                    SparseArray<String> array3 = new SparseArray<>();
//                    array3.put(5, "0");
//                    datas.add(array3);
                }

            } else {
//                SparseArray<String> array3 = new SparseArray<>();
//                array3.put(5, "0");
//                datas.add(array3);
            }


            if (infos.company != null) {
                comLicenseLists = (ArrayList<CompanyLicenseInfo.CompanyLicenseItem>) infos.company;
                for (CompanyLicenseInfo.CompanyLicenseItem item :
                        infos.company) {

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
                    array.put(5, "company");
                    datas.add(array);
                }

            }

        } else {
//            SparseArray<String> array3 = new SparseArray<>();
//            array3.put(5, "0");
//            datas.add(array3);
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
        return null;
    }


    /**
     * 获取公司绑定行政审批条目的所有id
     */
    public String getLicenseItemId() {
        return null;
    }

}
