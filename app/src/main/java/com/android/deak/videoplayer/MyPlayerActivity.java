package com.android.deak.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deak.videoplayer.entity.VideoPlayEntity;
import com.android.deak.videoplayer.utils.HttpUrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * 自定义播放器界面
 */
public class MyPlayerActivity extends Activity {
    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    /**
     * 当前缩放模式
     */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;

    private ImageView mIvLoading;
    private TextView mTvLoading;
    private RotateAnimation animation;
    private List<VideoPlayEntity.DataBean.EpisodeListBean> mEpisodeList;
    private int mCurrPosition;
    /**
     * 定时隐藏
     */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
//            mTitle.setVisibility(View.GONE);
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //设置全屏模式e
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.my_player_activity);
        String path = getIntent().getExtras().getString("path");
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mIvLoading = (ImageView) findViewById(R.id.iv_video_activity_loading);
        mTvLoading = (TextView) findViewById(R.id.tv_video_activity_loading);
        animation = new RotateAnimation(0, 355, 50, 50);
        animation.setDuration(1000);
        animation.setRepeatCount(10000);
        animation.setFillAfter(true);
        mIvLoading.setAnimation(animation);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (!TextUtils.isEmpty(path)) {
            getVideoLocalPlay(path);
        } else {
            mCurrPosition = getIntent().getExtras().getInt("position");
            getVideoPlay(getIntent().getExtras().getString("mId"), getIntent().getExtras().getString("Sid"));
        }
    }

    private void getVideoLocalPlay(String path) {
        mVideoView.setVideoPath(path);
        mMediaController = new MediaController(MyPlayerActivity.this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_FIT_PARENT,0);
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // optional need Vitamio 4.0
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mGestureDetector = new GestureDetector(MyPlayerActivity.this, new MyGestureListener());
    }


    private void getVideoPlay(final String mId, final String Sid) {
        mIvLoading.setVisibility(View.VISIBLE);
        mIvLoading.startAnimation(animation);
        mTvLoading.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams(HttpUrlUtils.videoAddress);
        params.addBodyParameter("quality", "high");
        params.addBodyParameter("seasonId", mId);
        params.addBodyParameter("episodeSid", Sid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                VideoPlayEntity entity = HttpUrlUtils.parseJason(result, VideoPlayEntity.class);
                if (mEpisodeList == null) {
                    mEpisodeList = entity.getData().getEpisodeList();
                }
                String URL = entity.getData().getM3u8().getUrl();
                mVideoView.setVideoPath(URL);
                mMediaController = new MediaController(MyPlayerActivity.this);
                mVideoView.setMediaController(mMediaController);
                mVideoView.requestFocus();
                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        // optional need Vitamio 4.0
                        mediaPlayer.setPlaybackSpeed(1.0f);
                        mIvLoading.clearAnimation();
                        mIvLoading.setVisibility(View.GONE);
                        mTvLoading.setVisibility(View.GONE);
                    }
                });
                mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mCurrPosition + 1 < mEpisodeList.size()) {
                            mCurrPosition++;
                            getVideoPlay(mId, mEpisodeList.get(mCurrPosition).getSid());
                        } else {
                            finish();
                        }
                        long i = mVideoView.getCurrentPosition();
                        Log.i("NEAK","当前时间："+i);
                    }
                });
                mGestureDetector = new GestureDetector(MyPlayerActivity.this, new MyGestureListener());
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
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            mOperationBg.setImageResource(R.mipmap.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            mOperationBg.setImageResource(R.mipmap.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null)
            mVideoView.setVideoLayout(mLayout, 0);
        super.onConfigurationChanged(newConfig);
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (mVideoView != null)
                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }

//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            if (isShow) {
//                mTitle.setVisibility(View.GONE);
//                isShow = false;
//            } else {
//                mTitle.setVisibility(View.VISIBLE);
//                mTitle.setText("A");
//                isShow = true;
////                TimerTask task = new TimerTask() {
////                    @Override
////                    public void run() {
////                  mTitle.setVisibility(View.GONE);
////                        isShow = false;
////                    }
////                };
////                new Timer().schedule(task,1000);
//            }
//            return true;
//        }

        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
