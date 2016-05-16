package com.aarontseng.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aarontseng.myviewpagertab.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ViewPagerIndicator extends LinearLayout {
    private Paint mPaint;
    private Path mPath;

    private List<String> mTitles;
    private ViewPager mViewPager;

    private int mTriangleWidth;
    private int mTriangleHeight;
    private int mInitTranslationX;
    private int mTranslationX;
    private int mTabVisibleCount;

    private static final int COUNT_DEFAULT_TAB=4;
    private static final float RADIO_TRIANGLE_WIDTH=1/6F;
    private static final int COLOR_TEXT_NORMAL=0x77FFFFFF;
    private static final int COLOR_TEXT_HIGHLIGHT=0xFFFFFFFF;
    //三角形指示器宽度最大值
    private static final float DIMENSION_TRIANGLE_WIDTH_MAX=RADIO_TRIANGLE_WIDTH/3;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取可见Tab的数量
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisibleCount=array.getInt(R.styleable.ViewPagerIndicator_visible_tab_count,COUNT_DEFAULT_TAB);
        if(mTabVisibleCount<0){
            mTabVisibleCount=COUNT_DEFAULT_TAB;
        }
        array.recycle();

        //初始化画笔
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
//        Log.i("Test", "mPaint");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount=getChildCount();
        if(childCount==0) return;

        for(int i=0;i<childCount;i++){
            View view=getChildAt(i);
            LinearLayout.LayoutParams layoutParams=(LayoutParams)view.getLayoutParams();
            layoutParams.weight=0;
            layoutParams.width=getScreenWidth()/mTabVisibleCount;
            view.setLayoutParams(layoutParams);
        }
        setItemClickEvent();
    }

    /* 获取屏幕宽度并返回
     * @return
     */
    private int getScreenWidth() {
        WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //移动画布canvas
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

        mTriangleWidth=(int)(w/mTabVisibleCount*RADIO_TRIANGLE_WIDTH);
        mTriangleWidth=Math.min(mTriangleWidth,(int)(w*DIMENSION_TRIANGLE_WIDTH_MAX));
        mInitTranslationX=w/mTabVisibleCount/2-mTriangleWidth/2;
        initTriangle();
//
//        Log.i("Test", "w:" + w);
//        Log.i("Test", "h:"+h);
//        Log.i("Test", "oldw:"+oldw);
//        Log.i("Test", "oldh:"+oldh);
//        Log.i("Test", "onSizeChanged");
    }

    /**
     * 画出三角形
     */
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


    /**当ViewPager滑动时，三角形指示器和容器同步移动
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int tabWidth=getWidth()/mTabVisibleCount;
        //画布移动
        mTranslationX=(int)(tabWidth*(position+offset));

        //容器移动，在tab处位于最后一个时
        if(mTabVisibleCount!=1) {
            if (position >= (mTabVisibleCount - 2) && offset > 0 && getChildCount() > mTabVisibleCount) {
                this.scrollTo(((position - (mTabVisibleCount - 2)) * tabWidth) + (int) (offset * tabWidth)
                             , 0);
            }
        } else {
            this.scrollTo(position * tabWidth + (int) (offset * tabWidth)
                    , 0);
        }
        invalidate();
    }

    /**根据titles为ViewPagerIndicator对象生成tab
     * @param titles
     */
    public void setTabItemTitls(List<String> titles){
        if(titles!=null&&titles.size()>0)
        {
            this.removeAllViews();
            mTitles=titles;

            for(String title:mTitles){
                this.addView(generateTextView(title));
            }
            setItemClickEvent();
        }
    }


    /**设定可见tab的数量
     * @param count
     */
    public void setTabVisibleCount(int count) {
        mTabVisibleCount=count;
    }

    /**根据title创建tab
     * @param title
     * @return
     */
    public TextView generateTextView(String title){
        TextView textview=new TextView(getContext());
        LinearLayout.LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.width=getScreenWidth()/mTabVisibleCount;

        textview.setGravity(Gravity.CENTER);
        textview.setText(title);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        textview.setTextColor(COLOR_TEXT_NORMAL);
        textview.setLayoutParams(layoutParams);
        return textview;
    }

    /**设置指定位置的tab字体高亮
     * @param pos
     */
    private void setTextHighLight(int pos) {
        View view=this.getChildAt(pos);
        if(view instanceof TextView){
            resetTextColor();
            ((TextView)view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    /**重置tab字体亮度
     * @param
     */
    private void resetTextColor() {
        for(int i=0;i<this.getChildCount();i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 为每个tab添加点击事件
     */
    private void setItemClickEvent() {
        int count=this.getChildCount();

        for(int i=0;i<count;i++){
            View view=getChildAt(i);

            final int j=i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTextHighLight(j);
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    /**设置关联的ViewPager
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager,int pos){
        mViewPager=viewPager;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setTextHighLight(position);
                scroll(position,positionOffset);
                if(mListener!=null) {
                    mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(mListener!=null) {
                    mListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(mListener!=null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });

        setTextHighLight(pos);
        mViewPager.setCurrentItem(pos);
    }

    /**
     * 提供一个接口让用户可以使用被占用的OnPageChangeListener接口
     */
    public interface PageOnChangeListener{
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        public void onPageSelected(int position);
        public void onPageScrollStateChanged(int state);
    }

    public PageOnChangeListener mListener;

    /**提供一个方法让用户可以设定OnPageChangeListener
     * @param listener
     */
    public void setOnPageChangeListener(PageOnChangeListener listener){
        this.mListener=listener;

    }
}
