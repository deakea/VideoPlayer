package com.android.deak.videoplayer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deak.videoplayer.adapter.VideoEpisodeAdapter;
import com.android.deak.videoplayer.application.MyApplication;
import com.android.deak.videoplayer.db.VideoChildDb;
import com.android.deak.videoplayer.entity.DetailsEntity;
import com.android.deak.videoplayer.entity.VideoPlayEntity;
import com.android.deak.videoplayer.utils.HttpUrlUtils;
import com.android.deak.videoplayer.manager.SystemBarTintManager;
import com.android.deak.videoplayer.widget.MyCardGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * 播放页面
 */
public class MyVideoPlayerActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private VideoView mVideo;
    private TextView mTvTitle, mTvIntro;
    private ImageView mIvCover;
    private DetailsEntity mDetalsEntity;
    private List<DetailsEntity.DataBean.SeasonBean.EpisodeBriefBean> mDataList;
    private VideoEpisodeAdapter mAdapter;
    private MediaController mediaController;
    private boolean isCollect;
    private Button mBtCollect;
    private RotateAnimation animation;

    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(getApplicationContext());
        setContentView(R.layout.activity_video);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setTintColor(0xFF6B4EFC);
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
        }
        initView();
        getData();
    }

    private void initView() {
        MyCardGridView mGridView = (MyCardGridView) findViewById(R.id.gv_activity_video_number);
        mAdapter = new VideoEpisodeAdapter(MyVideoPlayerActivity.this);
        mGridView.setAdapter(mAdapter);
        mVideo = (VideoView) findViewById(R.id.video_view);
        mTvTitle = (TextView) findViewById(R.id.tv_video_title);
        mIvCover = (ImageView) findViewById(R.id.iv_cover);
        mTvIntro = (TextView) findViewById(R.id.tv_video_intro);
        mBtCollect = (Button) findViewById(R.id.btn_video_collect);
        mId = getIntent().getExtras().getString("id");
        initDb();
        mBtCollect.setOnClickListener(this);
        mGridView.setOnItemClickListener(this);

    }

    private void initDb() {
        DbManager db = x.getDb(MyApplication.sDaoConfig);
        try {
            VideoChildDb childDb = db.selector(VideoChildDb.class).where("id", "=", mId).findFirst();
            if (childDb != null) {
                isCollect = childDb.isCollect();
                if (isCollect) {
                    mBtCollect.setText("取消收藏");
                } else {
                    mBtCollect.setText("收藏(订阅)");
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void getData() {
        RequestParams params = new RequestParams(HttpUrlUtils.details);
        params.addBodyParameter("seasonId", mId);//传入当前电视剧的Id
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mDetalsEntity = HttpUrlUtils.parseJason(result, DetailsEntity.class);
                mTvTitle.setText(mDetalsEntity.getData().getSeason().getTitle());
                mTvIntro.setText(mDetalsEntity.getData().getSeason().getBrief());
                ImageLoader.getInstance().displayImage(mDetalsEntity.getData().getSeason().getCover()
                        , mIvCover, MyApplication.sImageOptions);
                mDataList = mDetalsEntity.getData().getSeason().getEpisode_brief();
                mAdapter.setList(mDataList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Intent intent = new Intent(MyVideoPlayerActivity.this, MyPlayerActivity.class);
        intent.putExtra("Sid",mDataList.get(position).getSid());
        intent.putExtra("mId",mId);
        intent.putExtra("position",position);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_video_collect:
                DbManager db = x.getDb(MyApplication.sDaoConfig);
                if (!isCollect) {
                    VideoChildDb childDb = new VideoChildDb();
                    childDb.setId(mId);
                    childDb.setCover(getIntent().getExtras().getString("cover"));
                    childDb.setName(getIntent().getExtras().getString("name"));
                    childDb.setUpInfo(getIntent().getExtras().getString("upInfo"));
                    childDb.setCollect(true);
                    try {
                        db.save(childDb);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    mBtCollect.setText("取消收藏");
                    isCollect = true;
                } else {
                    mBtCollect.setText("收藏(订阅)");
                    try {
                        db.deleteById(VideoChildDb.class, mId);
                        isCollect = false;
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
