package com.suntrans.xiaofang.fragment.infodetail_parts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntrans.xiaofang.R;

/**
 * Created by Looney on 2017/1/9.
 */

public class GovApproal_fragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        TextView textView = new TextView(getActivity());
        textView.setText("行政审批");

        return textView;
    }

}
