package com.xlm.meishichina.ui.activity;

import org.holoeverywhere.ThemeManager.SuperStartActivity;
import org.holoeverywhere.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.waps.AppConnect;
import cn.waps.AppLog;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.xlm.meishichina.R;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.fragment.LeftMenuFragment;
import com.xlm.meishichina.ui.fragment.LeftMenuFragment.ChangeFragment;
import com.xlm.meishichina.ui.fragment.SettingsFragment;
import com.xlm.meishichina.ui.fragment.YaoyiyaoFragment;
import com.xlm.meishichina.util.ActivityManager;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.slidmenu.SlidingActivity;
import com.xlm.meishichina.widget.slidmenu.SlidingMenu;
import com.xlm.meishichina.widget.slidmenu.SlidingMenu.OnCloseListener;
import com.xlm.meishichina.widget.slidmenu.SlidingMenu.OnOpenListener;

public class MainActivity extends SlidingActivity implements ChangeFragment,
        MeishiConfig, SuperStartActivity
{
    private LeftMenuFragment leftMenuFragment;
    /**
     * mCurrentFragment 当前的fragment
     */
    private Fragment mCurrentFragment = null;
    /**
     * doubleBackToExitPressedOnce 返回键的标志位 默认false
     */
    private boolean doubleBackToExitPressedOnce = false;

    private String title = "";
    private SlidingMenu sm = null;
    /**
     * mCurrentFlag 该标志位代表是哪个fragment
     */
    private int mCurrentFlag = 0;
    /**
     * 该标志位是标识slidingmenu是否open
     */
    private boolean mflag = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        if (MeishiApplication.getApplication().getSharedPreferences()
                .getBoolean(MeishiConfig.CONFIG_SP_UPGRADE_SWITCH, false))
        {
            // 如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
            UmengUpdateAgent.setUpdateOnlyWifi(false);
            // 如果您同时使用了手动更新和自动检查更新，为了避免更新回调被多次调用，请加上下面这句代码
            UmengUpdateAgent.setOnDownloadListener(null);
            UmengUpdateAgent.update(this);

        }
        AppLog.enableLogging(IS_DEBUG);
        AppConnect.getInstance(MainActivity.this);

        ActivityManager.getActivityManager().addActivity(this);

        /**
         * bundle 此bundle来自主题设置
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mCurrentFragment = new SettingsFragment();
            mCurrentFlag = 4;
            getSupportActionBar().setTitle(
                    getResources().getString(R.string.left_menu_setting));
        }
        else
        {
            if (savedInstanceState != null)
            {
                mCurrentFragment = (Fragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, "content");
                mCurrentFlag = savedInstanceState.getInt("fragment");
            }
            if (mCurrentFragment == null)
            {
                mCurrentFragment = new YaoyiyaoFragment();
                mCurrentFlag = 0;
            }
            getSupportActionBar().setTitle(
                    getResources().getString(R.string.left_menu_yaoyiyao));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        setContentView(R.layout.activity_main_content);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, mCurrentFragment).commit();

        leftMenuFragment = LeftMenuFragment.getInstance(null);

        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, leftMenuFragment).commit();

        sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        sm.setOnOpenListener(new OnOpenListener()
        {

            @Override
            public void onOpen()
            {
                mflag = true;
                Logmeishi.d("---------onOpen------" + mflag);
                View view = getWindow().peekDecorView();
                if (view != null)
                {

                    InputMethodManager inputmanger = (InputMethodManager) getSystemService("input_method");
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
                            0);
                }
            }
        });

        sm.setOnCloseListener(new OnCloseListener()
        {

            @Override
            public void onClose()
            {
                mflag = false;
                Logmeishi.d("---------onClose------" + false);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "content",
                mCurrentFragment);
        outState.putInt("fragment", mCurrentFlag);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        AppConnect.getInstance(this).finalize();
        ActivityManager.getActivityManager().finishActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void switchContent(Fragment index, Bundle mBundle)
    {
        doubleBackToExitPressedOnce = false;
        mCurrentFragment = index;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, mCurrentFragment).commit();
        mCurrentFlag = mBundle.getInt("fragment");
        getSlidingMenu().showContent();
        title = mBundle.getString(MeishiConfig.CONFIG_TITLE_FLAG);
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onActivityResult(final int requestCode,
            final int resultCode, final Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        ((SettingsFragment) mCurrentFragment).buildSummary();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MobclickAgent.onResume(this);
        doubleBackToExitPressedOnce = false;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed()
    {
        Logmeishi.d("onBackPressed" + mflag);
        if (mflag)
        {
            Bundle mBundle = new Bundle();
            Fragment mFragment = null;
            mBundle.putString(MeishiConfig.CONFIG_TITLE_FLAG,
                    getString(R.string.left_menu_yaoyiyao));
            mBundle.putInt("fragment", 0);
            mFragment = new YaoyiyaoFragment();
            switchContent(mFragment, mBundle);
            return;
        }
        else if (!mflag && mCurrentFlag > 0)
        {
            toggle();
        }
        else
        {
            if (doubleBackToExitPressedOnce)
            {
                ActivityManager.getActivityManager().AppExit(
                        getApplicationContext());
                mImageLoader.stop();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toaster.showShort(MainActivity.this,
                    getString(R.string.mainactivity_backpress_double_tag));
        }

    }

    @Override
    public void superStartActivity(Intent intent, int requestCode,
            Bundle options)
    {
        intent.putExtra("from", "theme");
        super.superStartActivity(intent, requestCode, options);

    }

}
