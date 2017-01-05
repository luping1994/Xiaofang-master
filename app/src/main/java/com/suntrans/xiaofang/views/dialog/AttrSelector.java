package com.suntrans.xiaofang.views.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.xiaofang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2016/12/30.
 */

public class AttrSelector implements View.OnClickListener {
    private Context context;

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private LinearLayoutManager manager;
    private MyAdapterSub adapterSub;


    public void setProvider(DataProvider provider) {
        this.provider = provider;
        retrieveMainAttr();
    }

    private DataProvider provider;
    private DefaultProvider defaultProvider;

    ArrayList<MainAttr> mainAttrs = new ArrayList<>();
    ArrayList<SubAttr> subAttrs = new ArrayList<>();


    ArrayList<String> subAttrIndex = new ArrayList<>();
    int mainAttrIndex = -1;
    private View view;
    private TextView textMain;
    private TextView textsub;
    private View indicator;


    public AttrSelector(Context context) {
        this.context = context;
        initViews();
        initAdapters();
        retrieveMainAttr();
    }

    private static final int WHAT_MAINATTR_PROVIDER = 1;
    private static final int WHAT_SUBATTR_PROVIDER = 2;

    private static final int INDEX_TAB_MAINATTR = 1;
    public static final int INDEX_TAB_SUBATTR = 2;

    private int currentIndex = INDEX_TAB_MAINATTR;

    private boolean isCheck = false;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_MAINATTR_PROVIDER:
                    mainAttrs = (ArrayList<MainAttr>) msg.obj;
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    break;
                case WHAT_SUBATTR_PROVIDER:
                    subAttrs = (ArrayList<SubAttr>) msg.obj;
                    if (subAttrs != null && subAttrs.size() > 0) {
                        flag = new boolean[subAttrs.size()];
                        recyclerView.setAdapter(adapterSub);
                        currentIndex = INDEX_TAB_SUBATTR;
                    } else {
                        currentIndex = INDEX_TAB_MAINATTR;
                        isCheck = true;
                    }
                    break;
            }
            updateIndicator();
            updateTabsVisibility();
            return true;
        }
    });

    private void initViews() {
        view = LayoutInflater.from(context).inflate(R.layout.selector, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        TextView queding = (TextView) view.findViewById(R.id.queding);
        TextView qvxiao = (TextView) view.findViewById(R.id.qvxiao);
        queding.setOnClickListener(this);
        qvxiao.setOnClickListener(this);

        textMain = (TextView) view.findViewById(R.id.textmain);
        textsub = (TextView) view.findViewById(R.id.textsub);
        indicator = view.findViewById(R.id.indicator);

        textMain.setOnClickListener(new OnMainAttrTabClickListener());
        textsub.setOnClickListener(new OnSubAttrTabClickListener());


        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

//
        defaultProvider = new DefaultProvider(context);
//        provider = defaultProvider;

        updateIndicator();
        updateTabsVisibility();
    }

    private void initAdapters() {
        adapter = new MyAdapter();
        adapterSub = new MyAdapterSub();
    }


    private void retrieveMainAttr() {
        if (provider!=null)
        provider.provideMainAttr(new DataProvider.AddressReceiver<MainAttr>() {
            @Override
            public void send(List<MainAttr> data) {
                handler.sendMessage(Message.obtain(handler, WHAT_MAINATTR_PROVIDER, data));
            }
        });
    }

    private void retrieveSubAttrWithId(int id) {
        if (provider!=null)
            provider.provideSubAttrWith(id, new DataProvider.AddressReceiver<SubAttr>() {
            @Override
            public void send(List<SubAttr> data) {
                handler.sendMessage(Message.obtain(handler, WHAT_SUBATTR_PROVIDER, data));
            }
        });
    }

    public View getView() {
        return view;
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder1> {


        @Override
        public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_area, parent, false);
            return new ViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder1 holder, final int position) {
            holder.setData(position);
        }

        @Override
        public int getItemCount() {
            return mainAttrs.size();
        }

        class ViewHolder1 extends RecyclerView.ViewHolder {
            LinearLayout rl_area;
            TextView textView;
            ImageView imageViewCheckMark;

            public ViewHolder1(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
                imageViewCheckMark = (ImageView) itemView.findViewById(R.id.imageViewCheckMark);
                rl_area = (LinearLayout) itemView.findViewById(R.id.rl_area);
            }

            public void setData(final int position) {
                rl_area.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        subAttrs = null;

                        mainAttrIndex = position;
                        adapterSub.notifyDataSetChanged();
                        textMain.setText(mainAttrs.get(position).name);

                        adapter.notifyDataSetChanged();
                        retrieveSubAttrWithId(mainAttrs.get(position).id);
                    }
                });
                boolean a = mainAttrIndex != -1 && mainAttrs.get(mainAttrIndex).id == mainAttrs.get(position).id;
                imageViewCheckMark.setVisibility(a ? View.VISIBLE : View.INVISIBLE);
                textView.setText(mainAttrs.get(position).name);
            }
        }


    }


    private boolean[] flag = new boolean[subAttrs.size()];

    class MyAdapterSub extends RecyclerView.Adapter<MyAdapterSub.ViewHolder1> {

        @Override
        public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_area1, parent, false);
            return new ViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder1 holder, final int position) {
            holder.setData(position);

        }

        @Override
        public int getItemCount() {
            return subAttrs.size();
        }

        class ViewHolder1 extends RecyclerView.ViewHolder {
            CheckBox checkBox;

            public ViewHolder1(View itemView) {
                super(itemView);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            }

            public void setData(final int position) {
                checkBox.setText(subAttrs.get(position).name);
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(flag[position]);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        flag[position] = isChecked;
                    }
                });
            }
        }

    }

    private class OnMainAttrTabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            currentIndex = INDEX_TAB_MAINATTR;
            recyclerView.setAdapter(adapter);

            updateTabsVisibility();
            updateIndicator();
        }
    }


    private class OnSubAttrTabClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            currentIndex = INDEX_TAB_SUBATTR;
            recyclerView.setAdapter(adapterSub);

            updateTabsVisibility();
            updateIndicator();
        }
    }

    private void updateTabsVisibility() {
        textMain.setVisibility(Lists.notEmpty(mainAttrs) ? View.VISIBLE : View.GONE);
        textsub.setVisibility(Lists.notEmpty(subAttrs) ? View.VISIBLE : View.GONE);

//        textMain.setEnabled(currentIndex != -1);
    }


    private void updateIndicator() {
        view.post(new Runnable() {
            @Override
            public void run() {
                switch (currentIndex) {
                    case INDEX_TAB_MAINATTR:
                        buildIndicatorAnimatorTowards(textMain).start();
                        break;
                    case INDEX_TAB_SUBATTR:
                        buildIndicatorAnimatorTowards(textsub).start();
                        break;
                }
            }
        });
    }

    private AnimatorSet buildIndicatorAnimatorTowards(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(indicator, "X", indicator.getX(), tab.getX());

        final ViewGroup.LayoutParams params = indicator.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(params.width, tab.getMeasuredWidth());
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (int) animation.getAnimatedValue();
                indicator.setLayoutParams(params);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(xAnimator, widthAnimator);

        return set;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.queding:
                if (currentIndex == INDEX_TAB_MAINATTR && textsub.getVisibility() == View.GONE) {

                    if (mainAttrIndex == -1) {
                        mOnAttrSelectedListener.onSelectFailed("请选择一个单位属性");
                    } else {
                        mOnAttrSelectedListener.onSelectSuccess(mainAttrs.get(mainAttrIndex), null, 1);
                    }

                } else if (currentIndex == INDEX_TAB_MAINATTR && textsub.getVisibility() == View.VISIBLE) {
                    mOnAttrSelectedListener.onSelectFailed("请选择一个单位副属性");

                } else if (currentIndex == INDEX_TAB_SUBATTR) {
                    ArrayList<SubAttr> subAttr = new ArrayList<>();
//                    for (int i = 0; i < manager.getItemCount(); i++) {
//                        CheckBox cb = (CheckBox) manager.findViewByPosition(i).findViewById(R.id.checkbox);
//                        if (cb.isChecked()) {
//                            subAttr.add(subAttrs.get(i));
//                        }
//                    }
                    for (int i=0;i<flag.length;i++){
                        if (flag[i]==true){
                            subAttr.add(subAttrs.get(i));
                        }
                    }
                    if (subAttr.size() == 0)
                        mOnAttrSelectedListener.onSelectFailed("必须选择一个副属性");
                    else {
                        mOnAttrSelectedListener.onSelectSuccess(mainAttrs.get(mainAttrIndex), subAttr, 2);
                    }

                }
                break;
            case R.id.qvxiao:
                mOnAttrSelectedListener.onSelectFailed("取消");
                break;
        }
    }


    public onAttrSelectedListener getmOnAttrSelectedListener() {
        return mOnAttrSelectedListener;
    }

    public void setmOnAttrSelectedListener(onAttrSelectedListener mOnAttrSelectedListener) {
        this.mOnAttrSelectedListener = mOnAttrSelectedListener;
    }

    onAttrSelectedListener mOnAttrSelectedListener;

}
