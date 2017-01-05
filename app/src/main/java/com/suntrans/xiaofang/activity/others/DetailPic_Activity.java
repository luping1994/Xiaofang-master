package com.suntrans.xiaofang.activity.others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.suntrans.xiaofang.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Looney on 2017/1/3.
 */

public class DetailPic_Activity extends AppCompatActivity {
    ImageView imageView ;
    String url;
    TextView errorText;
    Toolbar toolbar;
    private PhotoViewAttacher attacher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picdetail);
        imageView = (ImageView) findViewById(R.id.imageview);
        url=getIntent().getStringExtra("url");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        errorText = (TextView) findViewById(R.id.error);
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.nopic)
                .into(imageView);
    }
//
//    @Override
//    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//        errorText.setVisibility(View.VISIBLE);
//        return false;
//    }
//
//    @Override
//    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//       imageView.setImageDrawable(resource);
//        attacher = new PhotoViewAttacher(imageView);
//        errorText.setVisibility(View.INVISIBLE);
////        setPhotoViewAttacher();
//        return false;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //    private void setPhotoViewAttacher() {
//        attacher.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                new AlertDialog.Builder(DetailPic_Activity.this)
//                        .setMessage("是否保存到本地?")
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                saveImageToGallery();
//                            }
//                        })
//                        .show();
//                return true;
//            }
//        });
//    }


}
