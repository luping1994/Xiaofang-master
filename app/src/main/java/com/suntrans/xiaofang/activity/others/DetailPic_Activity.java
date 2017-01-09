package com.suntrans.xiaofang.activity.others;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.suntrans.xiaofang.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Looney on 2017/1/3.
 */

public class DetailPic_Activity extends AppCompatActivity {
    ImageView imageView;
    String url;
    TextView errorText;
    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picdetail);
        imageView = (ImageView) findViewById(R.id.imageview);
        url = getIntent().getStringExtra("url");
        errorText = (TextView) findViewById(R.id.error);
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.nopic)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21)
                    finishAfterTransition();
                else
                    finish();
            }
        });
        LinearLayout linearLayout = new LinearLayout(this);
    }

}
