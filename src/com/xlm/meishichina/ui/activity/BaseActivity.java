package com.xlm.meishichina.ui.activity;

import org.holoeverywhere.app.Activity;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.xlm.meishichina.util.ActivityManager;
import com.xlm.meishichina.util.MeishiConfig;

public class BaseActivity extends Activity implements MeishiConfig
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityManager.getActivityManager().finishActivity(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
