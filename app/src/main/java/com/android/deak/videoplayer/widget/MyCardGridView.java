package com.android.deak.videoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 *自定义自动测量的GridView
 */
public class MyCardGridView extends GridView {

    public MyCardGridView(Context context) {
        super(context);
    }

    public MyCardGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCardGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
