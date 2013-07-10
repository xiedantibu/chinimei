package com.xlm.meishichina.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.ui.adapter.AppFragmentPagerAdapter;
import com.xlm.meishichina.ui.fragment.RecipeCommentFragment;
import com.xlm.meishichina.ui.fragment.ReportDetailFragment;
import com.xlm.meishichina.widget.viewpagerindicator.TitlePageIndicator;

public class ReportDetailActivity extends BaseActivity
{
    private ViewPager mViewPager;
    private TitlePageIndicator mIndicator;
    private GestureDetector gestureDetector;
    private ReportDetailAdapter mReportDetailAdapter;
    private Bundle bundle = null;
    private String name = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        bundle.putBoolean("from", false);// false代表来自ReportDetailActivity
        name = bundle.getString("name");

        buildViewPager();
        buildTitlePageIndicator();

        if (savedInstanceState != null)
        {
            mViewPager.setCurrentItem(savedInstanceState.getInt("item"));
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void buildViewPager()
    {

        mViewPager = (ViewPager) this.findViewById(R.id.report_detail_pager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        mViewPager.setOffscreenPageLimit(2);
        mReportDetailAdapter = new ReportDetailAdapter(
                getSupportFragmentManager());
        mViewPager.setAdapter(mReportDetailAdapter);

        gestureDetector = new GestureDetector(ReportDetailActivity.this,
                new MyOnGestureListener());

        mViewPager.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    private void buildTitlePageIndicator()
    {
        mIndicator = (TitlePageIndicator) this
                .findViewById(R.id.report_detail_indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setSelectedBold(true);
        mIndicator.setOnPageChangeListener(onPageChangeListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(name.trim());
    }

    private class ReportDetailAdapter extends AppFragmentPagerAdapter
    {
        List<Fragment> list = new ArrayList<Fragment>();

        public ReportDetailAdapter(FragmentManager fm)
        {
            super(fm);

            if (getReportDetailFragment() == null)
            {

                list.add(ReportDetailFragment.getInstance(bundle));
            }
            else
            {
                list.add(getReportDetailFragment());
            }

            if (getRecipeCommentFragment() == null)
            {

                list.add(RecipeCommentFragment.getInstance(bundle));
            }
            else
            {
                list.add(getRecipeCommentFragment());
            }
        }

        @Override
        public Fragment getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return getResources().getString(
                            R.string.fragment_report_detail_info);
                case 1:
                    return getResources().getString(
                            R.string.fragment_report_detail_comment);

            }
            return getResources().getString(
                    R.string.fragment_report_detail_info);
        }

        @Override
        protected String getTag(int position)
        {
            List<String> tagList = new ArrayList<String>();
            tagList.add(ReportDetailFragment.class.getName());
            tagList.add(RecipeCommentFragment.class.getName());

            return tagList.get(position);
        }

    }

    private ReportDetailFragment getReportDetailFragment()
    {
        return (ReportDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(ReportDetailFragment.class.getName());
    }

    private RecipeCommentFragment getRecipeCommentFragment()
    {
        return (RecipeCommentFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeCommentFragment.class.getName());
    }

    ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener()
    {
        public void onPageSelected(int position)
        {
        };
    };

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener
    {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY)
        {

            // if (velocityX > 100 && mViewPager.getCurrentItem() == 0)
            // {
            // ReportDetailActivity.this.finish();
            // return true;
            // }
            return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("item", mViewPager.getCurrentItem());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }
}
