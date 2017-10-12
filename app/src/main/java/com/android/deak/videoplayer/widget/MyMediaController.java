package com.android.deak.videoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import io.vov.vitamio.widget.MediaController;

/**
 * 自定义控制器
 */
public class MyMediaController extends MediaController {
    private Context mContext;
    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMediaController(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View makeControllerView() {
        return ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(getResources().getIdentifier("mymedia_contrller", "layout", mContext.getPackageName()), this);
    }
}
