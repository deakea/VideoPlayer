package com.android.deak.videoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.util.ArrayList;

/**
 *
 * Created by deak on 2016/6/1.
 */
public class PullLayout extends FrameLayout {
    private static final int STATE_IDLE = 1;//空闲状态
    private static final int STATE_EXPAND = 2;//展开状态
    private static final int STATE_COLLAPSE = 3;//收缩状态

    private ViewGroup mContentView;
    private ViewGroup mPullView;


    private int mPullMaxWidth;
    private int mCurPullRange;//右拉的距离
    private int mTmpCurPullRange;
//    private float mTouchY;
    private float mTouchX;
    private float mTmpTouchX;
    private Scroller mScroller;
    private ArrayList<IPullListener> listeners = new ArrayList<>();
    private float threshold;
    private int mCurState = STATE_IDLE;
    private boolean isChange;
    private int mPage;

    public boolean isOpen;

    public PullLayout(Context context) {
        this(context,null);
    }

    public PullLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context, new LinearInterpolator());
        threshold = getResources().getDisplayMetrics().widthPixels / 5;
    }

    /**
     * 将空间添加到屏幕
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPullView = (ViewGroup) getChildAt(0);//后面被遮挡的内容
        mContentView = (ViewGroup) getChildAt(1);//显示出来的内容
        setPullViewWidth(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mPullMaxWidth = (int) ((right - left) * 0.8);
        mContentView.layout(left + mCurPullRange, top, right, bottom);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                mTouchX = mTmpTouchX = ev.getX();
//                if (mCurPullRange != 0 && mCurPullRange != mPullMaxWidth) {
//                    mScroller.forceFinished(true);
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                /**
//                 * 如果不是第一页就不滑出Menu
//                 * */
//                if (mPage != 0){
//                    break;
//                }
//
//                //未右滑过
//                if (mCurPullRange == 0) {
//                    return ev.getX() - mTouchX > 0;
//                } else {
//                    return mCurPullRange != mPullMaxWidth || ev.getX() - mTouchX < 0;
//                }
//
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                mCurState = STATE_IDLE;
//                mTmpCurPullRange = mCurPullRange;
//                isChange = false;
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                float curX = event.getX();
//                if (Math.abs(curX - mTouchX) < threshold) {
//                    mTmpTouchX = curX;
//                    break;
//                }
//                float dx = curX - mTmpTouchX;
//                if (dx < 0) {
//                    mCurState = STATE_COLLAPSE;//收起
//                } else {
//                    mCurState = STATE_EXPAND;//展开
//                }
//                mCurPullRange += (int) (curX - mTmpTouchX);
//                mTmpTouchX = curX;
//                adjustCurPullRange();//校准移动的最大值
//                if (!isChange && mCurPullRange != mTmpCurPullRange) {
//                    isChange = true;
//                }
//                if (!isChange) break;
//                setPullViewWidth(mPullMaxWidth);
//                requestLayout();
//                notifyPullChanged();
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                if (!isChange){
//                    if (mCurPullRange != 0 && mCurPullRange != mPullMaxWidth){
//                        collapse();
//                    }
//                    break;
//                }
//                switch (mCurState) {
//                    case STATE_EXPAND:
//                        expand();
//                        break;
//                    case STATE_COLLAPSE:
//                        collapse();
//                        break;
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    private void expand() {
        startScroll(mPullMaxWidth - mCurPullRange);
        isOpen = true;
    }

    private void collapse() {
        startScroll(-mCurPullRange);
        isOpen = false;
    }

    private void startScroll(int x) {
        mScroller.startScroll(mCurPullRange,0,x,0,500);
//        invalidate();
        requestLayout();
    }

    private void adjustCurPullRange() {
        mCurPullRange = Math.max(0, Math.min(mCurPullRange, mPullMaxWidth));
    }

    /**
     * 设置后面空间的宽度
     */
    public void setPullViewWidth(int pullViewWidth) {
        ViewGroup.LayoutParams params = mPullView.getLayoutParams();
        params.width = pullViewWidth;
        mPullView.setLayoutParams(params);
    }
    /**
     * 传入页数
     * */
    public void setCurrPager(int currPage){
        mPage = currPage;
    }
    /**
     * 每次移动都通知改变
     * */
    private void notifyPullChanged() {
        for (IPullListener listener : listeners) {
            if (listener != null) {
                listener.onPullChange(mCurPullRange, mPullMaxWidth);
            }
        }
    }

    /**
     * 滚动计算
     * */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            mCurPullRange = mScroller.getCurrX();
            setPullViewWidth(mPullMaxWidth);
            requestLayout();

            notifyPullChanged();
            if (mCurPullRange == 0) {//完全收起来了
                mScroller.forceFinished(true);
                notifyCollapse();
                mCurState = STATE_IDLE;
            } else if (mCurPullRange == mPullMaxWidth) {
                mScroller.forceFinished(true);
                notifyExpanded();
                mCurState = STATE_IDLE;
            }
        }
//        super.computeScroll();
    }
    public void open(){
        startScroll(mPullMaxWidth);
        isOpen = true;
    }
    public void closed(){
        startScroll(-mPullMaxWidth);
        isOpen = false;
    }
//    public void
    private void notifyExpanded() {//通知展开
        for (IPullListener listener : listeners) {//遍历接口
            if (listener != null) {
                listener.onExpanded();
            }
        }
    }

    private void notifyCollapse() {
        for (IPullListener listener : listeners) {
            if (listener != null) {
                listener.onCollapsed();
            }
        }
    }
    public void addOnPullListener(IPullListener listener) {
        listeners.add(listener);
    }

    public interface IPullListener {
        void onExpanded();//完全被展开

        void onPullChange(int cur, int max);//正在拉动

        void onCollapsed();//完全被遮住
    }
}
