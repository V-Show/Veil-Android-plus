package com.veiljoy.veil.activity;


import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

import com.veiljoy.veil.R;
import com.veiljoy.veil.android.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqihong on 15/5/8.
 */
public class ActivityGuide extends BaseActivity  {

    private static final int FLING_MIN_DISTANCE = 100;
    private ViewFlipper flipper;
    private GestureDetector detector;
    private ViewPager mPager;//页卡内容
    private List<View> mListViews; // Tab页面列表
    private int currIndex = 0;// 当前页卡编号
    LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initViews();
        initEvents();
        init();

    }

    private void init() {


    }

    private void initEvents() {
        mPager.setAdapter(new GuidePagerAdapter(mListViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new GuideOnPageChangeListener());
    }

    private void initViews() {


        mPager = (ViewPager) findViewById(R.id.activity_guide_paper);
        mListViews = new ArrayList<View>();
        mInflater = getLayoutInflater();
        mListViews.add(mInflater.inflate(R.layout.include_guide_baseinfo, null));
        mListViews.add(mInflater.inflate(R.layout.include_guide_character, null));
        mListViews.add(mInflater.inflate(R.layout.include_guide_voice, null));

    }





    public class GuidePagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public GuidePagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }


    public class GuideOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {

                    } else if (currIndex == 2) {

                    }
                    break;
                case 1:
                    if (currIndex == 0) {

                    } else if (currIndex == 2) {

                    }
                    break;
                case 2:
                    if (currIndex == 0) {

                    } else if (currIndex == 1) {

                    }
                    break;
            }
            currIndex = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
