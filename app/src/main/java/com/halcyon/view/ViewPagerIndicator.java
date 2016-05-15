package com.halcyon.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class ViewPagerIndicator extends LinearLayout {
    private Paint mPaint;
    private Path mPath;
    private int mTriangleWidth;
    private int mTriangleHeight;
    private int mInitTranslationX;
    private int mTranslationX;

    private static final float RADIO_TRIANGLE_WIDTH=1/6F;

    public ViewPagerIndicator(Context context) {
        this(context,null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
        Log.i("Test", "mPaint");
    }

    @Override
    protected void dispatchDraw(Canvas canvas){
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);
//        Log.i("Test", "mInitTranslationX:" + mInitTranslationX);
//        Log.i("Test", "mTranslationX:"+mTranslationX);
//        Log.i("Test", "getHeight():"+getHeight());
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth=(int)(w/3*RADIO_TRIANGLE_WIDTH);
        mInitTranslationX=w/3/2-mTriangleWidth/2;
        initTriangle();
//
//        Log.i("Test", "w:" + w);
//        Log.i("Test", "h:"+h);
//        Log.i("Test", "oldw:"+oldw);
//        Log.i("Test", "oldh:"+oldh);
//        Log.i("Test", "onSizeChanged");
    }

    private void initTriangle() {
        mTriangleHeight=mTriangleWidth/2;

        mPath=new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTranslationX, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
//        Log.i("Test", "mTriangleWidth:"+mTriangleWidth);
//
//        Log.i("Test", "mTriangleHeight:"+mTriangleHeight);
    }

}
