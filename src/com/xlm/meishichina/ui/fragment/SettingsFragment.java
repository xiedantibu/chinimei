package com.xlm.meishichina.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.preference.Preference;
import org.holoeverywhere.preference.Preference.OnPreferenceClickListener;
import org.holoeverywhere.preference.PreferenceFragment;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.Toast;

import cn.waps.AppConnect;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.UMFeedbackService;
import com.umeng.fb.util.FeedBackListener;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.xlm.meishichina.R;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.LoginActivity;
import com.xlm.meishichina.util.ChangeLogDialog;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, MeishiConfig
{

    private Preference mPreferenceTheme = null;
    private Preference mPreferenceLogin = null;
    private Preference mPreferenceLocalCache = null;
    private Preference mPreferencePicQuality = null;
    private Preference mPreferenceUpdate = null;
    private Preference mPreferenceFeedback = null;
    private Preference mPreferenceUpdateMessage = null;
    private Preference mPreferenceApp = null;
    private DialogsProgressDialogIndeterminateFragment mDialogIndeterminateFragment = null;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            if (mDialogIndeterminateFragment != null)
            {
                mDialogIndeterminateFragment.dismiss();
            }
            switch (msg.what)
            {
                case 0:
                    Toaster.showShort(
                            getSupportActivity(),
                            MeishiApplication.getApplication().getString(
                                    R.string.fragment_setting_every_no));
                    break;

                default:
                    break;
            }

        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        mPreferenceTheme = findPreference(CONFIG_SP_THEME);
        mPreferenceLogin = findPreference(CONFIG_SP_LOGIN);
        mPreferenceLocalCache = findPreference(CONFIG_SP_LOCAL_CACHE);
        mPreferencePicQuality = findPreference(CONFIG_SP_UPLOAD_PIC);
        mPreferenceUpdate = findPreference(CONFIG_SP_UPGRADE);
        mPreferenceFeedback = findPreference(CONFIG_SP_FEEDBACK);
        mPreferenceUpdateMessage = findPreference(CONFIG_SP_UPDATE_MESSAGE);
        mPreferenceApp = findPreference(CONFIG_SP_RECOMMEND_APP);
        onPreferenceClick();
        buildSummary();
    }

    public void onPreferenceClick()
    {
        mPreferenceLogin
                .setOnPreferenceClickListener(new OnPreferenceClickListener()
                {

                    @Override
                    public boolean onPreferenceClick(Preference arg0)
                    {
                        if ((MeishiApplication.getApplication().getUserInfo() != null && MeishiApplication
                                .getApplication().getUserInfo().getSid() != null))
                        {
                            MeishiApplication.getApplication().getUserInfo()
                                    .clearUserInfo();
                            mPreferenceLogin.setTitle(MeishiApplication
                                    .getApplication().getString(
                                            R.string.fragment_setting_loging));
                            mPreferenceLogin.setSummary("");
                        }
                        else
                        {
                            Intent intent = new Intent(getSupportActivity(),
                                    LoginActivity.class);
                            intent.putExtra("from", true);
                            startActivityForResult(intent,
                                    REQUEST_CODE_LOGING_FROM_SETTING);
                        }
                        return false;
                    }
                });

        mPreferenceLocalCache
                .setOnPreferenceClickListener(new OnPreferenceClickListener()
                {

                    @Override
                    public boolean onPreferenceClick(Preference arg0)
                    {

                        mDialogIndeterminateFragment = DialogsProgressDialogIndeterminateFragment
                                .getInstance(MeishiApplication
                                        .getApplication()
                                        .getString(
                                                R.string.fragment_setting_deleting));
                        mDialogIndeterminateFragment
                                .show(getSupportActivity());
                        Thread thread = new Thread(clearCache);
                        thread.start();
                        return true;
                    }
                });

        mPreferenceUpdate
                .setOnPreferenceClickListener(new OnPreferenceClickListener()
                {

                    @Override
                    public boolean onPreferenceClick(Preference arg0)
                    {

                        com.umeng.common.Log.LOG = MeishiConfig.IS_DEBUG;
                        UmengUpdateAgent.setUpdateOnlyWifi(false);
                        // 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
                        UmengUpdateAgent.setUpdateAutoPopup(false);
                        UmengUpdateAgent.setUpdateListener(updateListener);

                        UmengUpdateAgent
                                .setOnDownloadListener(new UmengDownloadListener()
                                {

                                    @Override
                                    public void OnDownloadEnd(int result)
                                    {
                                    }

                                });

                        UmengUpdateAgent.update(getSupportActivity());
                        return true;
                    }
                });

        mPreferenceFeedback
                .setOnPreferenceClickListener(new OnPreferenceClickListener()
                {

                    @Override
                    public boolean onPreferenceClick(Preference arg0)
                    {
                        FeedBackListener listener = new FeedBackListener()
                        {

                            @Override
                            public void onSubmitFB(Activity activity)
                            {
                                EditText emailText = (EditText) activity
                                        .findViewById(R.id.feedback_email);
                                Map<String, String> remarkMap = new HashMap<String, String>();
                                remarkMap.put("email", emailText.getText()
                                        .toString());
                                UMFeedbackService.setRemarkMap(remarkMap);

                            }

                            @Override
                            public void onResetFB(Activity activity,
                                    Map<String, String> contactMap,
                                    Map<String, String> remarkMap)
                            {
                                EditText emailText = (EditText) activity
                                        .findViewById(R.id.feedback_email);
                                if (remarkMap != null)
                                {
                                    emailText.setText(remarkMap.get("email"));
                                }
                            }
                        };
                        UMFeedbackService.setFeedBackListener(listener);
                        UMFeedbackService.setGoBackButtonVisible();
                        UMFeedbackService
                                .openUmengFeedbackSDK(getSupportActivity());
                        return true;
                    }
                });

        mPreferenceUpdateMessage
                .setOnPreferenceClickListener(new OnPreferenceClickListener()
                {

                    @Override
                    public boolean onPreferenceClick(Preference arg0)
                    {
                        ChangeLogDialog changeLogDialog = new ChangeLogDialog(
                                getSupportActivity());
                        changeLogDialog.show();
                        return true;
                    }
                });

        mPreferenceApp
                .setOnPreferenceClickListener(new OnPreferenceClickListener()
                {

                    @Override
                    public boolean onPreferenceClick(Preference arg0)
                    {
                        String showAd = MobclickAgent.getConfigParams(
                                getSupportActivity(), "showAd");
                        if (showAd.equals("true"))
                        {
                            AppConnect.getInstance(getSupportActivity())
                                    .showOffers(getSupportActivity());
                        }
                        return true;
                    }
                });

    }

    Runnable clearCache = new Runnable()
    {

        @Override
        public void run()
        {

            try
            {
                MeishiApplication.getApplication().clearAppCache();
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(0);
        }
    };

    UmengUpdateListener updateListener = new UmengUpdateListener()
    {

        @Override
        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo)
        {
            switch (updateStatus)
            {
                case 0: // has update
                    UmengUpdateAgent.showUpdateDialog(getSupportActivity(),
                            updateInfo);
                    break;
                case 1: // has no update
                    Toast.makeText(
                            getSupportActivity(),
                            MeishiApplication.getApplication().getString(
                                    R.string.update_no), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 2: // none wifi
                    Toast.makeText(
                            getSupportActivity(),
                            MeishiApplication.getApplication().getString(
                                    R.string.update_no_wife),
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3: // time out
                    Toast.makeText(
                            getSupportActivity(),
                            MeishiApplication.getApplication().getString(
                                    R.string.update_request_time_out),
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    };

    public void buildSummary()
    {
        String value = PreferenceManager.getDefaultSharedPreferences(
                getSupportActivity()).getString(CONFIG_SP_THEME, "1");
        if (value.equals("1"))
        {
            mPreferenceTheme.setSummary(MeishiApplication.getApplication()
                    .getString(R.string.fragment_setting_theme_red));
        }
        else if (value.equals("2"))
        {
            mPreferenceTheme.setSummary(MeishiApplication.getApplication()
                    .getString(R.string.fragment_setting_theme_black));
        }

        if ((MeishiApplication.getApplication().getUserInfo() != null && MeishiApplication
                .getApplication().getUserInfo().getSid() != null))
        {
            mPreferenceLogin.setTitle(MeishiApplication.getApplication()
                    .getString(R.string.fragment_setting_unloging));
            mPreferenceLogin.setSummary(MeishiApplication.getApplication()
                    .getUserInfo().getUserName().trim());
        }
        else
        {
            mPreferenceLogin.setTitle(MeishiApplication.getApplication()
                    .getString(R.string.fragment_setting_loging));
            mPreferenceLogin.setSummary("");
        }
        value = PreferenceManager.getDefaultSharedPreferences(
                getSupportActivity()).getString(CONFIG_SP_UPLOAD_PIC, "4");

        if (value.equals("1"))
        {
            mPreferencePicQuality
                    .setSummary(MeishiApplication
                            .getApplication()
                            .getString(
                                    R.string.fragment_setting_upload_pic_quality_no_press));
        }
        else if (value.equals("2"))
        {
            mPreferencePicQuality
                    .setSummary(MeishiApplication
                            .getApplication()
                            .getString(
                                    R.string.fragment_setting_upload_pic_quality_half_press));
        }
        else if (value.equals("3"))
        {
            mPreferencePicQuality
                    .setSummary(MeishiApplication
                            .getApplication()
                            .getString(
                                    R.string.fragment_setting_upload_pic_quality_half_and_press));
        }
        else
        {
            mPreferencePicQuality.setSummary(MeishiApplication.getApplication()
                    .getString(
                            R.string.fragment_setting_upload_pic_quality_auto));
        }

        try
        {
            mPreferenceUpdate.setSummary(MeishiApplication.getApplication()
                    .getString(R.string.fragment_setting_currentbanben)
                    + getVersionName());
        }
        catch (Exception e)
        {
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1)
    {

        if (arg1.equals(CONFIG_SP_THEME))
        {
            String value = PreferenceManager.getDefaultSharedPreferences(
                    getSupportActivity()).getString(CONFIG_SP_THEME, "1");
            if (value.equals("1"))
            {
                ThemeManager.restartWithTheme(getSupportActivity(),
                        ThemeManager.LIGHT);
                ThemeManager.setDefaultTheme(ThemeManager.LIGHT);
                mPreferenceTheme.setSummary(MeishiApplication.getApplication()
                        .getString(R.string.fragment_setting_theme_red));
            }
            else if (value.equals("2"))
            {
                ThemeManager.restartWithTheme(getSupportActivity(),
                        ThemeManager.DARK);
                ThemeManager.setDefaultTheme(ThemeManager.DARK);
                mPreferenceTheme.setSummary(MeishiApplication.getApplication()
                        .getString(R.string.fragment_setting_theme_black));
            }
        }
        else if (arg1.equals(CONFIG_SP_UPLOAD_PIC))
        {
            String value = PreferenceManager.getDefaultSharedPreferences(
                    getSupportActivity()).getString(CONFIG_SP_UPLOAD_PIC, "4");
            if (value.equals("1"))
            {
                mPreferencePicQuality
                        .setSummary(MeishiApplication
                                .getApplication()
                                .getString(
                                        R.string.fragment_setting_upload_pic_quality_no_press));
            }
            else if (value.equals("2"))
            {
                mPreferencePicQuality
                        .setSummary(MeishiApplication
                                .getApplication()
                                .getString(
                                        R.string.fragment_setting_upload_pic_quality_half_press));
            }
            else if (value.equals("3"))
            {
                mPreferencePicQuality
                        .setSummary(MeishiApplication
                                .getApplication()
                                .getString(
                                        R.string.fragment_setting_upload_pic_quality_half_and_press));
            }
            else
            {
                mPreferencePicQuality
                        .setSummary(MeishiApplication
                                .getApplication()
                                .getString(
                                        R.string.fragment_setting_upload_pic_quality_auto));
            }
        }
    }

    private String getVersionName()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = MeishiApplication.getApplication()
                .getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        String version = "";
        try
        {
            packInfo = packageManager.getPackageInfo(MeishiApplication
                    .getApplication().getPackageName(), 0);
            version = packInfo.versionName;
        }
        catch (NameNotFoundException e)
        {
            version = "1.0";
        }
        return version;
    }

}
