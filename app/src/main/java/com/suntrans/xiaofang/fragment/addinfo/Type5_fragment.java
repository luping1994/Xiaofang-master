package com.suntrans.xiaofang.fragment.addinfo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.license.AddLicenseResult;
import com.suntrans.xiaofang.model.license.LicenseDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.R.id.addItem;
import static com.suntrans.xiaofang.R.id.buildcompany;
import static com.suntrans.xiaofang.R.id.lat;
import static com.suntrans.xiaofang.R.id.lng;
import static com.suntrans.xiaofang.R.id.map;

/**
 * Created by Looney on 2016/12/13.
 */

public class Type5_fragment extends RxFragment implements View.OnClickListener {


    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.buildcompany)
    EditText buildcompany;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.getposition)
    Button getposition;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.phone)
    EditText phone;


    private AlertDialog dialog;
    private Button additem;


    private int mYear;
    private int mMonth;
    private int mDay;
    private LicenseDetailInfo info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type5, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        additem = (Button) view.findViewById(addItem);
        additem.setOnClickListener(listener);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        getposition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getposition:
                Intent intent = new Intent(getActivity(), MapChoose_Activity.class);
                startActivityForResult(intent, 601);
                getActivity().overridePendingTransition(android.support.design.R.anim.abc_slide_in_bottom, android.support.design.R.anim.abc_slide_out_bottom);
                break;
        }
    }

    static class ViewTag {
        String type;
        String title;
    }

    final String[] items = new String[]{"建审", "验收", "开业前"};
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            new AlertDialog.Builder(getContext())
                    .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_xingzhenxuke3, null);
                            ViewTag tag = new ViewTag();
                            switch (which) {
                                case 0:
                                    ((TextView) (view.findViewById(R.id.title))).setText("建审");
                                    tag.title = "建审";
                                    tag.type = "1";
                                    break;
                                case 1:
                                    ((TextView) (view.findViewById(R.id.title))).setText("验收");
                                    tag.title = "验收";
                                    tag.type = "2";
                                    break;
                                case 2:
                                    ((TextView) (view.findViewById(R.id.title))).setText("开业前");
                                    tag.title = "开业前";
                                    tag.type = "3";
                                    break;
                            }
                            view.setTag(tag);
                            view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    llContent.removeView(view);
                                }
                            });
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
                            llContent.addView(view);
                            dialog.dismiss();
                        }
                    })
                    .setTitle("请选择添加类型")
                    .create().show();
        }
    };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void initData() {

    }


    String lng;
    String lat;

    public void addCommit() {
        Map<String, String> map=null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String buildcompany1 = buildcompany.getText().toString();
        String contact1 = contact.getText().toString().trim();
        String phone1 = phone.getText().toString();
        String info = "";
        if (llContent.getChildCount() > 5) {
            JSONArray array = new JSONArray();
            for (int i = 5; i < llContent.getChildCount(); i++) {
                View view = llContent.getChildAt(i);
                RadioGroup group = (RadioGroup) view.findViewById(R.id.radioGroup);
                String number1 = ((EditText) view.findViewById(R.id.number)).getText().toString();
                String time1 = ((TextView) view.findViewById(R.id.time)).getText().toString();
                String type1 = ((ViewTag) view.getTag()).type;
                String isqualified1 = "";
                int id = group.getCheckedRadioButtonId();
                if (id == R.id.radio_hege)
                    isqualified1 = "1";
                else if (id == R.id.radio_buhege)
                    isqualified1 = "0";

                if (!Utils.isVaild(number1) || !Utils.isVaild(time1)) {
                    UiUtils.showToast("请输入完整的信息");
                    return;
                }
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
            }
            info = array.toString();
        }
        if (!Utils.isVaild(buildcompany1)) {
            UiUtils.showToast("建设单位不能为空");
            return;
        }
        if (!Utils.isVaild(name1)) {
            UiUtils.showToast("名称不能为空");
            return;
        }
        if (!Utils.isVaild(addr1)) {
            UiUtils.showToast("地址不能为空");
            return;
        }
        if (!Utils.isVaild(contact1)) {
            UiUtils.showToast("联系人不能为空");
            return;
        }
        if (!Utils.isVaild(phone1)) {
            UiUtils.showToast("电话不能为空");
            return;
        }

        builder.put("name", name1);
        builder.put("addr", addr1);
        builder.put("cmpname", buildcompany1);
        builder.put("contact", contact1);
        builder.put("phone", phone1);
        if (Utils.isVaild(info))
            builder.put("info", info);

        if (Utils.isVaild(lat) && Utils.isVaild(lng)) {
            builder.put("lat", lat);
            builder.put("lng", lng);
        }

        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }


        RetrofitHelper.getApi().createLicense(map)
                .compose(this.<AddLicenseResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddLicenseResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast(UiUtils.getContext(), "添加失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddLicenseResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                String result1 = result.result;
                                dialog = new AlertDialog.Builder(getActivity())
                                        .setMessage(result1)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getActivity().finish();
                                            }
                                        })
                                        .create();
                                dialog.show();
                            } else {
                                UiUtils.showToast(result.msg);
                            }
                        } else {

                            UiUtils.showToast(UiUtils.getContext(), "添加失败");
                        }
                    }
                });
    }



    public void upDateLicense(){
        Map<String, String> map=null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String buildcompany1 = buildcompany.getText().toString();
        String contact1 = contact.getText().toString().trim();
        String phone1 = phone.getText().toString();

        if (!Utils.isVaild(buildcompany1)) {
            UiUtils.showToast("建设单位不能为空");
            return;
        }
        if (!Utils.isVaild(name1)) {
            UiUtils.showToast("名称不能为空");
            return;
        }
        if (!Utils.isVaild(addr1)) {
            UiUtils.showToast("地址不能为空");
            return;
        }
        if (!Utils.isVaild(contact1)) {
            UiUtils.showToast("联系人不能为空");
            return;
        }
        if (!Utils.isVaild(phone1)) {
            UiUtils.showToast("电话不能为空");
            return;
        }

        builder.put("name", name1);
        builder.put("id", info.id);
        builder.put("addr", addr1);
        builder.put("cmpname", buildcompany1);
        builder.put("contact", contact1);
        builder.put("phone", phone1);

        if (Utils.isVaild(lat) && Utils.isVaild(lng)) {
            builder.put("lat", lat);
            builder.put("lng", lng);
        }

        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }


        RetrofitHelper.getApi().updateLicense(map)
                .compose(this.<AddLicenseResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddLicenseResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast("修改失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddLicenseResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                String result1 = result.result;
                                dialog = new AlertDialog.Builder(getActivity())
                                        .setMessage(result1)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getActivity().finish();
                                            }
                                        })
                                        .create();
                                dialog.show();
                            } else {
                                UiUtils.showToast(result.msg);
                            }
                        } else {

                            UiUtils.showToast( "修改失败");
                        }
                    }
                });
    }


    public void setData(LicenseDetailInfo info) {
        this.info=info;
        additem.setVisibility(View.GONE);
        name.setText(info.name==null?"":info.name);
        buildcompany.setText(info.cmyname==null?"":info.cmyname);
        contact.setText(info.contact==null?"":info.contact);
        phone.setText(info.phone==null?"":info.phone);
        addr.setText(info.addr==null?"":info.addr);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 601) {
            if (resultCode == -1) {
                PoiItem poiItem = data.getParcelableExtra("addrinfo");
//                name.setText(poiItem.getTitle());
                lat=poiItem.getLatLonPoint().getLatitude() +"";
                lng=poiItem.getLatLonPoint().getLongitude() +"";
                addr.setText(poiItem.getSnippet());
            }
        }

    }
}


