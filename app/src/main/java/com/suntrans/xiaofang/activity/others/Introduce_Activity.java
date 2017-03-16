package com.suntrans.xiaofang.activity.others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Looney on 2017/2/27.
 */

public class Introduce_Activity extends BasedActivity {
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("使用说明");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        WebSettings settings = webview.getSettings();
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //get the newProgress and refresh progress bar
                progressbar.setProgress(newProgress);
                if (newProgress==100){
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
        settings.setJavaScriptEnabled(true);
        webview.loadUrl("http://api.91yunpan.com/help/help.html");
    }


}
