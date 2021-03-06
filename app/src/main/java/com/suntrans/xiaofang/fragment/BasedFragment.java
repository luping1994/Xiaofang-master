package com.suntrans.xiaofang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.suntrans.xiaofang.R;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by Looney on 2016/12/30.
 */

public abstract class BasedFragment extends RxFragment {

    protected ProgressBar progressBar;
    protected Button btFailed;
    protected LinearLayout error;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_bar);
        btFailed = (Button) view.findViewById(R.id.loading_failed);
        progressBar.setVisibility(View.VISIBLE);
        error= (LinearLayout) view.findViewById(R.id.errorll);
        error.setVisibility(View.GONE);
        btFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reLoadData(v);
            }
        });
    }

    public abstract void reLoadData(View view);
}
