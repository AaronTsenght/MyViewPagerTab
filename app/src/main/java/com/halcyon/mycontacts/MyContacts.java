package com.halcyon.mycontacts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;

import com.halcyon.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyContacts extends FragmentActivity {

    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;

    private List<String> mTitles= Arrays.asList("电话","联系人","短信");
    private List<SmsFragment> mSmsFragments=new ArrayList<SmsFragment>();
    private FragmentPagerAdapter mAapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_gallery);

        intViews();
        intDatas();

        mViewPager.setAdapter(mAapter);
    }

    private void intDatas() {
        for(String title:mTitles){
            SmsFragment fragment=SmsFragment.newInstance(title);
            mSmsFragments.add(fragment);
        }

        mAapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mSmsFragments.get(position);
            }

            @Override
            public int getCount() {
                return mSmsFragments.size();
            }
        };
    }

    private void intViews() {
        mIndicator= (ViewPagerIndicator) findViewById(R.id.id_indicator);
        mViewPager= (ViewPager) findViewById(R.id.id_viewpager);
    }
}
