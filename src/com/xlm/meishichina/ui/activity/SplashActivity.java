package com.xlm.meishichina.ui.activity;

import org.holoeverywhere.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;
import com.xlm.meishichina.R;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.ViewUtils;

public class SplashActivity extends Activity
{

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MobclickAgent.onError(SplashActivity.this);
        super.onCreate(savedInstanceState);
        MobclickAgent.updateOnlineConfig(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        setImageLayoutParams();

        UMFeedbackService.enableNewReplyNotification(this,
                NotificationType.NotificationBar);
    }

    private void setImageLayoutParams()
    {
        int heightPixels = ViewUtils.heightPixels(MeishiApplication
                .getApplication());
        float localDisplayMetrics = ViewUtils.localDisplayMetrics(
                MeishiApplication.getApplication(), 52);
        int j = (int) (0.3d * heightPixels - localDisplayMetrics);
        mImageView = (ImageView) findViewById(R.id.splash_logo);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        localLayoutParams.setMargins(0, j, 0, 0);
        this.mImageView.setLayoutParams(localLayoutParams);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        MobclickAgent.onResume(this);
        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                mImageView.setImageDrawable(MeishiApplication.getApplication()
                        .getResources().getDrawable(R.drawable.splash_logo));
                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(2000);
                mImageView.startAnimation(animation);
            }
        }, 100);

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
