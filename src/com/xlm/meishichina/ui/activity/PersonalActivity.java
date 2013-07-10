package com.xlm.meishichina.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.ui.adapter.AppFragmentPagerAdapter;
import com.xlm.meishichina.ui.fragment.PersonalFavRecipeFragment;
import com.xlm.meishichina.ui.fragment.PersonalInfoFragment;
import com.xlm.meishichina.ui.fragment.PersonalOrginalFragment;
import com.xlm.meishichina.ui.fragment.PersonalReportFragment;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.widget.viewpagerindicator.TitlePageIndicator;

public class PersonalActivity extends BaseActivity
{
    private ViewPager mViewPager;
    private TitlePageIndicator mIndicator;
    private PersonalPagerAdapter mPagerAdapter;
    private GestureDetector gestureDetector;
    private Bundle bundle = null;
    private String name = "";
    private PersonalFavRecipeFragment mFavRecipeFragment = null;
    private PersonalReportFragment mReportFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        name = bundle.getString("name").trim();
        buildViewPager();
        buildTitlePageIndicator();

        if (savedInstanceState != null)
        {
            mViewPager.setCurrentItem(savedInstanceState.getInt("item"));
        }
        mFavRecipeFragment = (PersonalFavRecipeFragment) mPagerAdapter.list
                .get(1);
        mReportFragment = (PersonalReportFragment) mPagerAdapter.list.get(2);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void buildViewPager()
    {

        mViewPager = (ViewPager) this.findViewById(R.id.personal_pager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        mViewPager.setOffscreenPageLimit(4);
        mPagerAdapter = new PersonalPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        gestureDetector = new GestureDetector(PersonalActivity.this,
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
                .findViewById(R.id.personal_indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setSelectedBold(true);
        mIndicator.setOnPageChangeListener(onPageChangeListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        if (name.equals(""))
        {
            name = getString(R.string.app_name);
        }
        getSupportActionBar().setTitle(name.trim());
    }

    private class PersonalPagerAdapter extends AppFragmentPagerAdapter
    {
        List<Fragment> list = new ArrayList<Fragment>();
        private FragmentManager fm;

        public PersonalPagerAdapter(FragmentManager fm)
        {
            super(fm);
            this.fm = fm;
            if (getPersonalInfoFragment() == null)
            {

                list.add(PersonalInfoFragment.getInstance(bundle));
            }
            else
            {
                list.add(getPersonalInfoFragment());
            }

            if (getPersonalFavRecipeFragment() == null)
            {

                list.add(PersonalFavRecipeFragment.getInstance(bundle));
            }
            else
            {
                list.add(getPersonalFavRecipeFragment());
            }
            if (getPersonalReportFragment() == null)
            {

                list.add(PersonalReportFragment.getInstance(bundle));
            }
            else
            {
                list.add(getPersonalReportFragment());
            }

            if (getPersonalOrginalFragment() == null)
            {

                list.add(PersonalOrginalFragment.getInstance(bundle));
            }
            else
            {
                list.add(getPersonalOrginalFragment());
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
                            R.string.fragment_personal_info);
                case 1:
                    return getResources().getString(
                            R.string.fragment_personal_info_fav_recipe_num);
                case 2:
                    return getResources().getString(
                            R.string.fragment_personal_report_num);
                case 3:
                    return getResources().getString(
                            R.string.fragment_personal_original);

            }
            return getResources().getString(R.string.fragment_personal_info);
        }

        @Override
        protected String getTag(int position)
        {
            List<String> tagList = new ArrayList<String>();
            tagList.add(PersonalInfoFragment.class.getName());
            tagList.add(PersonalFavRecipeFragment.class.getName());
            tagList.add(PersonalReportFragment.class.getName());
            tagList.add(PersonalOrginalFragment.class.getName());

            return tagList.get(position);
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        public void setFragments(List fragments)
        {
            if (this.list != null)
            {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : this.list)
                {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fm.executePendingTransactions();
            }
            this.list = fragments;
            notifyDataSetChanged();
        }
    }

    ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener()
    {
        public void onPageSelected(int position)
        {
        };

        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels)
        {
            if (mFavRecipeFragment != null
                    && mFavRecipeFragment.mListView != null
                    && mFavRecipeFragment.mActionMode != null)
            {
                mFavRecipeFragment.mListView.clearChoices();
                mFavRecipeFragment.mActionMode.finish();
                mFavRecipeFragment.mActionMode = null;

            }
            else if (mReportFragment != null
                    && mReportFragment.mListView != null
                    && mReportFragment.mActionMode != null)
            {
                mReportFragment.mListView.clearChoices();
                mReportFragment.mActionMode.finish();
                mReportFragment.mActionMode = null;
            }
        };

    };

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener
    {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY)
        {

            if (velocityX > 100 && mViewPager.getCurrentItem() == 0)
            {
                PersonalActivity.this.finish();
                return true;
            }
            return false;
        }
    }

    private PersonalInfoFragment getPersonalInfoFragment()
    {
        return (PersonalInfoFragment) getSupportFragmentManager()
                .findFragmentByTag(PersonalInfoFragment.class.getName());
    }

    private PersonalOrginalFragment getPersonalOrginalFragment()
    {
        return (PersonalOrginalFragment) getSupportFragmentManager()
                .findFragmentByTag(PersonalOrginalFragment.class.getName());
    }

    private PersonalFavRecipeFragment getPersonalFavRecipeFragment()
    {
        return (PersonalFavRecipeFragment) getSupportFragmentManager()
                .findFragmentByTag(PersonalFavRecipeFragment.class.getName());
    }

    private PersonalReportFragment getPersonalReportFragment()
    {
        return (PersonalReportFragment) getSupportFragmentManager()
                .findFragmentByTag(PersonalReportFragment.class.getName());
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        Logmeishi.d("-----onPause-----");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Logmeishi.d("-----onDestroy-----");
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        Logmeishi.d("-----onResume-----");
    }

    @Override
    protected void onRestart()
    {
        // TODO Auto-generated method stub
        super.onRestart();
        Logmeishi.d("-----onRestart-----");
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
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        Intent intent2 = getIntent();
        if (intent2 != null)
        {
            Logmeishi.d("---intent2------");
            bundle = intent2.getExtras();
            name = bundle.getString("name");
            List<Fragment> list = new ArrayList<Fragment>();

            list.add(PersonalInfoFragment.getInstance(bundle));
            list.add(PersonalFavRecipeFragment.getInstance(bundle));
            list.add(PersonalReportFragment.getInstance(bundle));
            list.add(PersonalOrginalFragment.getInstance(bundle));

            mPagerAdapter.setFragments(list);
            mIndicator.setCurrentItem(0);
            if (name.equals(""))
            {
                name = getString(R.string.app_name);
            }
            getSupportActionBar().setTitle(name.trim());

            mFavRecipeFragment = (PersonalFavRecipeFragment) mPagerAdapter.list
                    .get(1);
            mReportFragment = (PersonalReportFragment) mPagerAdapter.list
                    .get(2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

}
