package com.suntrans.xiaofang.fragment.infodetail;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.google.common.collect.ImmutableMap;
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

import static com.suntrans.xiaofang.utils.Utils.pad;

/**
 * Created by Looney on 2016/12/13.
 * 行政许可详情信息fragment
 */

public class Type5__info_fragment extends BasedFragment {
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter myAdapter;
    int mYear;
    int mMonth;
    int mDay;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        return inflater.inflate(R.layout.fragment_info_others, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setVisibility(View.INVISIBLE);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public void reLoadData(View view) {
        progressBar.setVisibility(View.VISIBLE);
        error.setVisibility(View.INVISIBLE);
        getData();
    }

    private void initData() {


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
            } else if (viewType == 1) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_editlicense, parent, false);
                return new ViewHolder2(view);
            } else if (viewType == 2) {
                return null;
            } else {
                return null;
            }


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder1) {
                ((ViewHolder1) holder).setData(position);
            } else if (holder instanceof ViewHolder2) {
                ((ViewHolder2) holder).setData(position);
            } else if (holder instanceof ViewHolder3) {
                ((ViewHolder3) holder).setData(position);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
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
            Button button;

            public ViewHolder3(View itemView) {
                super(itemView);

            }

            public void setData(int position) {

            }
        }


        class ViewHolder2 extends RecyclerView.ViewHolder {
            TextView title;
            TextView isq;
            TextView number;
            TextView time;
            TextView header;
            public ViewHolder2(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                isq = (TextView) itemView.findViewById(R.id.isqualified);
                number = (TextView) itemView.findViewById(R.id.number);
                time = (TextView) itemView.findViewById(R.id.time);
                header = (TextView) itemView.findViewById(R.id.header);
            }

            public void setData(int position) {
                if (position==5){
                    header.setVisibility(View.VISIBLE);
                }else {
                    header.setVisibility(View.GONE);
                }
                title.setText(datas.get(position).get(0));
                number.setText(datas.get(position).get(1));
                time.setText(datas.get(position).get(2));
                isq.setText(datas.get(position).get(3));
            }
        }
    }

    public LicenseDetailInfo info;
    LatLng to;

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
                                if (info.lat != null || info.lng != null) {
                                    try {
                                        to = new LatLng(Double.valueOf(info.lat), Double.valueOf(info.lng));
                                    } catch (Exception e) {
                                        to = null;
                                    }
                                }
                                Type5__info_fragment.this.info = info;
                                LogUtil.i(info.toString());
                                refreshView(Type5__info_fragment.this.info);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        error.setVisibility(View.GONE);
                                    }
                                }, 500);
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
                        throwable.printStackTrace();
                        progressBar.setVisibility(View.INVISIBLE);
                        error.setVisibility(View.VISIBLE);
                        UiUtils.showToast(App.getApplication(), "服务器数据错误!");
                    }
                });
    }

    Handler handler = new Handler();

    private void refreshView(LicenseDetailInfo info) {
        datas.clear();
        SparseArray<String> array0 = new SparseArray<>();
        array0.put(0, "建设单位");
        array0.put(1, info.cmyname == null ? "" : info.cmyname);
        array0.put(2, "cmyname");
        datas.add(array0);

        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "名称");
        array4.put(1, info.name);
        array4.put(2, "name");
        datas.add(array4);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "地址");
        array1.put(1, info.addr);
        array1.put(2, "addr");
        datas.add(array1);

        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "联系人");
        array2.put(1, info.contact);
        array2.put(2, "contact");
        datas.add(array2);

        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "联系电话");
        array3.put(1, info.phone);
        array3.put(2, "phone");
        datas.add(array3);


        List<LicenseItemInfo> items = info.detail;
        for (LicenseItemInfo item :
                items) {
            SparseArray<String> array = new SparseArray<>();
            if (item.type.equals("1")) {
                array.put(0, "建审");
            } else if (item.type.equals("2")) {
                array.put(0, "验收");
            } else if (item.type.equals("3")) {
                array.put(0, "开业前");
            }
            array.put(1, item.number);
            array.put(2, item.time);
            array.put(3, item.isqualified==null?"":item.isqualified.equals("1") ? "合格" : "不合格");
            datas.add(array);
        }
        myAdapter = null;
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
//        myAdapter.notifyDataSetChanged();
    }


    private void delete() {
        RetrofitHelper.getApi().deleteLicense(info.id)
                .compose(this.<AddLicenseResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddLicenseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast(UiUtils.getContext(), "删除失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddLicenseResult result) {
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
        ((InfoDetail_activity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((InfoDetail_activity) getActivity()).getSupportActionBar();
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
                if (info == null) {
                    UiUtils.showToast(UiUtils.getContext(), "无法获取审批项目信息");
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
                dialog.setTitle("确定删除该审批项目?");
                dialog.show();
                break;
            case R.id.gohere:
                if (info == null) {
                    UiUtils.showToast(UiUtils.getContext(), "无法获取审批项目信息");
                    break;
                }
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
                    dialog1.setTitle("单位未添加地理坐标,无法导航!");
                    dialog1.show();
                    break;
                }
                intent1.putExtra("from", getActivity().getIntent().getParcelableExtra("from"));
                intent1.putExtra("to", to);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.xiugai:
                if (info == null) {
                    UiUtils.showToast(UiUtils.getContext(), "无法获取单位信息");
                    break;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), EditLicense_activity.class);
                intent.putExtra("title", title);
                intent.putExtra("id", ((InfoDetail_activity) getActivity()).companyId);
                intent.putExtra("info", info);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.add_item:
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
                                createLicenseDetail(type, number1, time1, isq);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder2.create().show();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detailinfo_license, menu);
    }

    private void createLicenseDetail(String type1, String number1, String time1, String isqualified1) {

        Map<String, String> map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
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
        builder.put("id", info.id);
        builder.put("info", info1);
        map = builder.build();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在新增,请稍后...");
        progressDialog.show();
        ;
        RetrofitHelper.getApi().createLicenseDetail(map)
                .compose(this.<AddLicenseResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddLicenseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast("添加失败");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(AddLicenseResult addLicenseResult) {
                        if (addLicenseResult != null) {
                            getData();
                            progressDialog.dismiss();
                            if (addLicenseResult.status.equals("1")) {
                                UiUtils.showToast(addLicenseResult.result);
                            } else {
                                UiUtils.showToast(addLicenseResult.msg);
                            }
                        } else {
                            progressDialog.dismiss();
                            UiUtils.showToast("添加失败");
                        }
                    }
                });

    }

    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction("net.suntrans.xiaofang.lp");
        intent.putExtra("type", MarkerHelper.LICENSE);
        getActivity().sendBroadcast(intent);
    }
}
