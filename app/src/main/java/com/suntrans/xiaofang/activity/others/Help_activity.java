package com.suntrans.xiaofang.activity.others;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.views.IViewPager;


/**
 * Created by Looney on 2016/12/1.
 */

public class Help_activity extends AppCompatActivity {
    public IViewPager vPager;    //自定义ViewPager控件
    private Toolbar toolbar;
    private TextView tx_all;
    private TextView tx_quick;
    private TextView tx_online;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
        setupToolbar();
    }

    private void initView() {
        tx_all = (TextView) findViewById(R.id.tx_all);
        tx_quick = (TextView) findViewById(R.id.tx_quick);
        tx_online = (TextView) findViewById(R.id.tx_online);
        tx_all.setOnClickListener(new MyOnClickListener(0));
        tx_quick.setOnClickListener(new MyOnClickListener(1));
        tx_online.setOnClickListener(new MyOnClickListener(2));

    }

    private void setupToolbar() {
        StatusBarCompat.compat(this, Color.TRANSPARENT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("帮助与反馈");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }


    //viewpager适配器
    public class MyPagerAdapter extends FragmentPagerAdapter {     //viewpager适配器

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = { "全部问题", "快捷帮助","在线反馈"};
        //	private final String[] titles = { "能源管控"};
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];    //获得与标题号对应的标题名
        }
        @Override
        public int getCount() {
            return titles.length;     //一共有几个头标
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:          //第一个fragment

                case 1:          //第二个fragment

                case 2:   //第3个fragment

                default:
                    return null;
            }
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            switch(arg0)   //根据页面滑到哪一页，设置标题两个textview的背景颜色
            {
                case 0:
                {
                    tx_all.setTextColor(Color.parseColor("#EB0000"));  //红色
                    tx_quick.setTextColor(Color.GRAY);     //灰色
                    tx_online.setTextColor(Color.GRAY);     //灰色

                    break;
                }
                case 1:
                {

                    break;
                }
                case 2:
                {

                    break;
                }

                default:break;
            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            switch(v.getId())   //判断按下的按钮id，设置标题两个textview的背景颜色
            {
                case R.id.tx_all:
                {
                    tx_all.setTextColor(UiUtils.getColor(Help_activity.this, R.attr.colorPrimary));  //红色
                    tx_quick.setTextColor(Color.GRAY);     //灰色
                    tx_online.setTextColor(Color.GRAY);     //灰色

                    break;
                }
                case R.id.tx_quick:
                {
                    tx_quick.setTextColor(UiUtils.getColor(Help_activity.this, R.attr.colorPrimary));  //红色
                    tx_all.setTextColor(Color.GRAY);     //灰色
                    tx_online.setTextColor(Color.GRAY);     //灰色

                    break;
                }
                case R.id.tx_online:
                {
                    tx_online.setTextColor(UiUtils.getColor(Help_activity.this, R.attr.colorPrimary));  //红色
                    tx_quick.setTextColor(Color.GRAY);     //灰色
                    tx_all.setTextColor(Color.GRAY);     //灰色
                    break;
                }
                default:break;
            }
//            vPager.setCurrentItem(index);   //根据头标选择的内容  对viewpager进行页面切换
        }
    };
}
