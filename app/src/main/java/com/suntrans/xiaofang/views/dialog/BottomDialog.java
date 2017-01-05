package com.suntrans.xiaofang.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.suntrans.xiaofang.R;


public class BottomDialog extends Dialog {




    private AttrSelector selector;
    public BottomDialog(Context context,AttrSelector selector) {
        super(context, R.style.bottom_dialog);
        init(context,selector);
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context,null);
    }

    public BottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context,null);
    }

    private void init(Context context,AttrSelector selector) {
        this.selector = selector;
        this.setContentView(selector.getView());

        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = dp2px(context, 500);
        window.setAttributes(params);

        window.setGravity(Gravity.BOTTOM);
    }


    public void setSelector(AttrSelector selector) {
        this.selector = selector;
    }


    public void setSelectorListener(onAttrSelectedListener listener){
        this.selector.setmOnAttrSelectedListener(listener);
    }

    public static int dp2px(Context context, float dp) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * dp);
    }
}
