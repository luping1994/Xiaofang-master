package com.suntrans.xiaofang.activity.others;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.utils.UiUtils;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Looney on 2017/6/5.
 */

public class CmyQRCodeActivity extends BasedActivity {

    private ImageView erweima;
    private String url;
    private Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_cmy_qr);
        setUpToolbar();
        initView();
    }

    private void initView() {
        url = getIntent().getStringExtra("url");
        final String name = getIntent().getStringExtra("name");
        erweima = (ImageView) findViewById(R.id.erweima);
        Glide.with(this)
                .load(url)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.no_pic)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        uri =  saveImage(resource,name);
                        erweima.setImageBitmap(resource);
                    }
                });
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("name"));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.share:
                share(uri,"分享",CmyQRCodeActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public  void share(Uri uri, String desc, Context context)
    {

//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"");
//        shareIntent.putExtra(Intent.EXTRA_TEXT,  "");
//        shareIntent.setType("text/plain");
//        context.startActivity(Intent.createChooser(shareIntent, desc));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent,"分享到"));
    }

    public  Uri saveImage(Bitmap bmp,String name) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "wuhanxiaofang");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name+ ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
//            UiUtils.showToast("图片已保存到"+file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(file);
        Intent mIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        getApplicationContext().sendBroadcast(mIntent);
        return uri;
    }
}
