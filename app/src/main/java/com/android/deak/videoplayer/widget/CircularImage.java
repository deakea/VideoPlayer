package com.android.deak.videoplayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.deak.videoplayer.R;

/**
 * 自定义圆头像
 */
public class CircularImage extends ImageView {
    private Context mContext;
    private int mBorderThickness = 0;//边线的厚度
    private int mColor = 0xffffffff;//控件的颜色
    //外边线和内边线的颜色
    private int mBorderOutsideColor = 0;
    private int mBorderInsideColor = 0;
    //控件的默认宽度和高度
    private int mWidth = 0;
    private int mHeight = 0;

    public CircularImage(Context context) {
        super(context);
        mContext = context;
    }

    public CircularImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setAttrs(attrs);
    }

    public CircularImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setAttrs(attrs);
    }

    //自定义属性
    private void setAttrs(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CircularImageView);
        mBorderThickness = array.getDimensionPixelSize(R.styleable.CircularImageView_border_thickness, 0);
        mBorderOutsideColor = array.getColor(R.styleable.CircularImageView_border_outside_color, mColor);
        mBorderInsideColor = array.getColor(R.styleable.CircularImageView_border_inside_color, mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) return;
        if (getWidth() == 0 || getHeight() == 0) return;
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class) return;
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        if (mWidth == 0) {
            mWidth = getWidth();
        }
        if (mHeight == 0) {
            mHeight = getHeight();
        }
        int radius;

        if (mBorderOutsideColor != mColor && mBorderInsideColor != mColor) {
            //定义外圆变边框和内圆边框
            radius = (mWidth < mHeight ? mWidth : mHeight) / 2 - 2 * mBorderThickness;
            // 画内圆
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
            // 画外圆
            drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);
        } else if (mBorderInsideColor != mColor) {// 定义画一个边框
            radius = (mWidth < mHeight ? mWidth : mHeight) / 2 - mBorderThickness;
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
        } else if (mBorderOutsideColor != mColor) {// 定义画一个边框
            radius = (mWidth < mHeight ? mWidth : mColor) / 2 - mBorderThickness;
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);
        } else {// 没有边框
            radius = (mWidth < mHeight ? mWidth : mHeight) / 2;
        }
        Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
        canvas.drawBitmap(roundBitmap, mWidth / 2 - radius, mHeight / 2 - radius, null);
    }

    /**
     * 获取裁剪后的圆形图片
     * @param radius 半径
     */
    private Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth, squareHeight;
        int x, y;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,squareHeight);
        } else {
            squareBitmap = bmp;
        }
        if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,diameter, true);
        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2,
                scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
//        bmp = null;
//        squareBitmap = null;
//        scaledSrcBmp = null;
        return output;
    }

    /**
     * 边缘画圆
     * */
    private void drawCircleBorder(Canvas canvas, int radius, int color) {
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的　style　为STROKE：空心 */
        paint.setStyle(Paint.Style.STROKE);
        /* 设置paint的外框宽度 */
        paint.setStrokeWidth(mBorderThickness);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, paint);
    }
}
