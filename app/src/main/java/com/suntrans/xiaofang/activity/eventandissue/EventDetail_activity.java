package com.suntrans.xiaofang.activity.eventandissue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.activity.others.DetailPic_Activity;
import com.suntrans.xiaofang.adapter.PicAdapter;
import com.suntrans.xiaofang.model.event.Event;
import com.suntrans.xiaofang.model.event.EventDetailResult;
import com.suntrans.xiaofang.model.event.Result;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.views.MyVideoView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/22.
 */

public class EventDetail_activity extends BasedActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "EventDetail_activity";
//    @BindView(R.id.start)
//    ImageView start;
//    @BindView(R.id.loading)
//    TextView loading;
//    @BindView(R.id.vedio)
//    RelativeLayout vedio;
//    @BindView(R.id.cut)
//    Button cut;
//    @BindView(R.id.fullscreen_button)
//    ImageButton fullscreenButton;

    @BindView(R.id.companyname)
    TextView companyname;
    @BindView(R.id.contents)
    TextView contents;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.creat_at)
    TextView creatAt;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.companyaddr)
    TextView companyaddr;
    private Toolbar toolbar;
    private String id;
    private RecyclerView recyclerView_before;
    private RecyclerView recyclerView_after;
    private LinearLayoutManager manager_before;
    private LinearLayoutManager manager_after;
    private PicAdapter adapter_before;
    private PicAdapter adapter_after;

    private ArrayList<String> url_before;
    private ArrayList<String> url_after;
    private ArrayList<String> vedio_url;
    private ArrayList<SparseArray<String>> datas;


    private MediaPlayer mMediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;

    private MyVideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_eventdetail);
        ButterKnife.bind(this);
        setupToolBar();
        initData();
        initView();
    }

    private void initData() {
        datas = new ArrayList<>();
        SparseArray<String> array = new SparseArray<>();
        array.put(0, "单位名称:");
        array.put(1, "");
        datas.add(array);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "事件内容:");
        array1.put(1, "");
        datas.add(array1);
        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "录入人:");
        array2.put(1, "");
        datas.add(array2);

        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "录入时间:");
        array3.put(1, "");
        datas.add(array3);

        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "安全类型:");
        array4.put(1, "");
        datas.add(array4);

        SparseArray<String> array5 = new SparseArray<>();
        array5.put(0, "处理情况:");
        array5.put(1, "");
        datas.add(array5);

        SparseArray<String> array6 = new SparseArray<>();
        array6.put(0, "单位地址:");
        array6.put(1, "");
        datas.add(array6);
    }


    private void initView() {
        url_before = new ArrayList<>();
        url_after = new ArrayList<>();
        vedio_url = new ArrayList<>();

        videoView = (MyVideoView) findViewById(R.id.vedioview);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnErrorListener(this);

        recyclerView_before = (RecyclerView) findViewById(R.id.recycleview_before);
        recyclerView_after = (RecyclerView) findViewById(R.id.recycleview_after);
        manager_before = new LinearLayoutManager(this);
        manager_after = new LinearLayoutManager(this);
        manager_before.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager_after.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter_before = new PicAdapter(url_before, this);
        adapter_after = new PicAdapter(url_after, this);

        recyclerView_before.setLayoutManager(manager_before);
        recyclerView_before.setAdapter(adapter_before);

        recyclerView_after.setLayoutManager(manager_after);
        recyclerView_after.setAdapter(adapter_after);

        start = (ImageView) findViewById(R.id.start);

        adapter_after.setOnitemClickListener(new PicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (url_after==null||url_after.size()==0){
                    return;
                }
                if (Build.VERSION.SDK_INT < 21) {
                    Intent intent = new Intent();
                    intent.putExtra("url", url_after.get(position));
                    intent.setClass(EventDetail_activity.this, DetailPic_Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EventDetail_activity.this, DetailPic_Activity.class);
                    intent.putExtra("url", url_after.get(position));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(EventDetail_activity.this, view, getString(R.string.transition_test));
                    startActivity(intent, options.toBundle());
                }
            }
        });
        adapter_before.setOnitemClickListener(new PicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (url_before==null||url_before.size()==0){
                    return;
                }
                if (Build.VERSION.SDK_INT < 21) {
                    Intent intent = new Intent();
                    intent.putExtra("url", url_before.get(position));
                    intent.setClass(EventDetail_activity.this, DetailPic_Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EventDetail_activity.this, DetailPic_Activity.class);
                    intent.putExtra("url", url_before.get(position));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(EventDetail_activity.this, view, getString(R.string.transition_test));
                    startActivity(intent, options.toBundle());
                }
            }
        });

    }

    private void setupToolBar() {
        id = getIntent().getStringExtra("id");
        System.out.println("id是" + id);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("事件详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        System.out.println("我的ID是==>"+id);
        RetrofitHelper.getApi().getEventDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<EventDetailResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(UiUtils.getContext(), "获取事件详情失败");
                    }

                    @Override
                    public void onNext(EventDetailResult result) {
                        refreshView(result);
                    }


                });
    }


    private void refreshView(EventDetailResult result) {
        Result data = result.result;
        Event event = data.item;
        if (data.video_path_1 != null && !data.video_path_1.equals("") && !data.video_path_1.equals("null")) {
            vedio_url.add("http://xf.91yunpan.com/" + data.video_path_1);
            vedio_url.add("http://xf.91yunpan.com/" + data.video_path_2);
        }

        datas.get(0).put(1, event.company_name);
        datas.get(1).put(1, "  " + event.contents);
        datas.get(2).put(1, event.user_id);
        datas.get(3).put(1, event.created_at);
        datas.get(4).put(1, event.type_ids);
        String str = "";
        if (event.is_done.equals("1")) {
            str = "已处置";
        } else {
            str = "未处置";
        }

        if (event.status.equals("1")) {
            str += "(本人办结)";
        } else if (event.status.equals("2")) {
            str += "(明日处置)";
        } else if (event.status.equals("5")) {
            str += "(线下办结)";
        } else if (event.status.equals("3")) {
            str += "(上报民警)";
        } else if (event.status.equals("4")) {
            str += "(上报副所长)";
        }
        datas.get(5).put(1, str);
        datas.get(6).put(1, event.company_address);

        refreshlayout();


        for (String s :
                data.img_before) {
            url_before.add(s);
        }

        for (String s :
                data.img_beforeR) {
            url_before.add(s);
        }
        for (String s :
                data.img_after) {
            url_after.add(s);
        }

        for (String s :
                data.img_afterR) {
            url_after.add(s);
        }
        adapter_before.notifyDataSetChanged();
        adapter_after.notifyDataSetChanged();
        if (vedio_url != null && vedio_url.size() != 0) {
            createVideoThumbnail(vedio_url.get(0));
        }
    }

    ImageView start;

    private void refreshlayout() {
        companyname.setText(datas.get(0).get(0) + datas.get(0).get(1));
        contents.setText(datas.get(1).get(1));
        name.setText(datas.get(2).get(0) + datas.get(2).get(1));
        creatAt.setText(datas.get(3).get(0) + datas.get(3).get(1));
        type.setText(datas.get(4).get(0) + datas.get(4).get(1));
        status.setText(datas.get(5).get(0) + datas.get(5).get(1));
        companyaddr.setText(datas.get(6).get(0) + datas.get(6).get(1));
    }

    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        super.onDestroy();

    }

    public void onClick(View view) {
        if (vedio_url == null || vedio_url.size() == 0) {
            UiUtils.showToast(UiUtils.getContext(), "该事件无视频");
            return;
        }
        videoView.setBackground(null);
        start.setVisibility(View.GONE);
        Uri uri = Uri.parse(vedio_url.get(0));
        videoView.setVideoPath(uri.toString());
        videoView.setOnCompletionListener(this);
        videoView.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        UiUtils.showToast(UiUtils.getContext(), "播放失败");
        return true;
    }

    private void createVideoThumbnail(final String mVideoUrl) {
        Observable<Bitmap> observable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                int kind = MediaStore.Video.Thumbnails.MINI_KIND;
                if (Build.VERSION.SDK_INT >= 14) {
                    retriever.setDataSource(mVideoUrl, new HashMap<String, String>());
                } else {
                    retriever.setDataSource(mVideoUrl);
                }
                bitmap = retriever.getFrameAtTime();
                subscriber.onNext(bitmap);
                retriever.release();
            }
        });
        observable.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(final Bitmap bitmap) {
                        //设置封面
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                videoView.setBackground(new BitmapDrawable(bitmap));
                            }
                        });
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
    }


}
