package com.aarontseng.myviewpagertab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;

import com.aarontseng.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyViewPagerTab extends FragmentActivity {

    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;

    private List<String> mTitles= Arrays.asList("电话","联系人","通话记录","收藏","短信","设置");
    private List<MyFragment> mMyFragments =new ArrayList<MyFragment>();
    private FragmentPagerAdapter mAapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_viewpager_indicator2);

        intViews();
        intDatas();

        mIndicator.setTabVisibleCount(4);
        mIndicator.setTabItemTitls(mTitles);

        mViewPager.setAdapter(mAapter);
        mIndicator.setViewPager(mViewPager,0);
    }

    /**
     * 初始化数据
     */
    private void intDatas() {
        for(String title:mTitles){
            MyFragment fragment= MyFragment.newInstance(title);
            mMyFragments.add(fragment);
        }

        mAapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mMyFragments.get(position);
            }

            @Override
            public int getCount() {
                return mMyFragments.size();
            }
        };
    }

    /**
     * 初始化控件
     */
    private void intViews() {
        mIndicator= (ViewPagerIndicator) findViewById(R.id.id_indicator);
        mViewPager= (ViewPager) findViewById(R.id.id_viewpager);
    }
}
