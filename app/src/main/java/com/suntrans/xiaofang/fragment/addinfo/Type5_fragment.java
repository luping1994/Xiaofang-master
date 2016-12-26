package com.suntrans.xiaofang.fragment.addinfo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.license.AddLicenseResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/13.
 */

public class Type5_fragment extends Fragment {

    //    @BindView(R.id.name)
//    EditText name;
//    @BindView(R.id.addr)
//    EditText addr;
//    @BindView(R.id.lng)
//    EditText lng;
//    @BindView(R.id.lat)
//    EditText lat;
//    @BindView(R.id.leader)
//    EditText leader;
//    @BindView(R.id.phone)
//    EditText phone;
//    @BindView(R.id.content1)
//    LinearLayout content1;
    @BindView(R.id.commit_license)
    Button commitLicense;

    private ArrayList<SparseArray<String>> datas = new ArrayList<>();

    Map<String, String> map;
    private AlertDialog dialog;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(getActivity());
        adapter = new MyAdapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    private void initData() {
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

    }


    @OnClick(R.id.commit_license)
    public void onClick() {
        addCommit();
    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_xingzhengxuke, parent, false);
                return new ViewHolder1(view);
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
        public int getItemViewType(int position) {
            if (position == 0 || position == 5)
                return 1;
            return 0;
        }

        @Override
        public int getItemCount() {
            return datas.size() + 2;
        }

        class ViewHolder1 extends RecyclerView.ViewHolder {
            TextView key;
            TextView value;

            public ViewHolder1(View itemView) {
                super(itemView);
                key = (TextView) itemView.findViewById(R.id.key);
                value = (TextView) itemView.findViewById(R.id.value);
            }

            public void setData(int position) {
                if (position < 5)
                    key.setText(datas.get(position - 1).get(0));
                else
                    key.setText(datas.get(position - 2).get(0));

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
                    item.setText("添加建筑信息");
                else
                    item.setText("建审信息");
            }
        }

    }

    private void addCommit() {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        for (int i = 0; i < manager.getChildCount(); i++) {
            if (i == 0 || i == 5)
                continue;
            EditText textView = (EditText) manager.findViewByPosition(i).findViewById(R.id.value);
            String a = textView.getText().toString();
            if (Utils.isVaild(a)) {
                if (i < 5)
                    builder.put(datas.get(i - 1).get(2), a);
                else if (i > 5) {
                    builder.put(datas.get(i - 2).get(2), a);
                }
            } else {
                UiUtils.showToast(UiUtils.getContext(), "所有字段必填");
                return;
            }

        }
        map = null;
//        String name1 = name.getText().toString();
//        String addr1 = addr.getText().toString();
//        String lng1 = lng.getText().toString();
//        String lat1 = lat.getText().toString();
//
//
//        String leader1 = leader.getText().toString();
//        String phone1 = phone.getText().toString();


//        if (Utils.isVaild(name1)) {
//            builder.put("name", name1.replace(" ", ""));
//        }
//
//        if (Utils.isVaild(addr1)) {
//            builder.put("addr", addr1.replace(" ", ""));
//        }
//        if (Utils.isVaild(lng1)) {
//            builder.put("lng", lng1.replace(" ", ""));
//        }
//        if (Utils.isVaild(lat1)) {
//            builder.put("lat", lat1.replace(" ", ""));
//        }
//        if (Utils.isVaild(leader1)) {
//            builder.put("leader", leader1.replace(" ", ""));
//        }
//
//        if (Utils.isVaild(phone1)) {
//            builder.put("phone", phone1.replace(" ", ""));
//        }

        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }

        RetrofitHelper.getApi().createLicense(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddLicenseResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
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
                            }
                        } else {
                            LogUtil.i("失败了！！！！！！！！！！！！！！！");
                        }
                    }
                });

    }
}


