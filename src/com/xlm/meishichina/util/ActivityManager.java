package com.xlm.meishichina.util;

import java.util.Stack;

import org.holoeverywhere.app.Activity;

import android.content.Context;

import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.ui.MeishiApplication;

public class ActivityManager
{
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager()
    {
    }

    public static ActivityManager getActivityManager()
    {
        if (instance == null)
        {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity)
    {
        if (activityStack == null)
        {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity()
    {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity()
    {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity)
    {
        if (activity != null)
        {
            activityStack.remove(activity);
            HttpRequest.getInstance().httpClient.cancelRequests(activity, true);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls)
    {
        for (Activity activity : activityStack)
        {
            if (activity.getClass().equals(cls))
            {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity()
    {
        for (int i = 0, size = activityStack.size(); i < size; i++)
        {
            if (null != activityStack.get(i))
            {
                HttpRequest.getInstance().httpClient.cancelRequests(
                        activityStack.get(i), true);
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context)
    {
        try
        {
            finishAllActivity();
            MeishiApplication.getApplication().getUserInfo().clearUserInfo();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
        catch (Exception e)
        {
        }
    }
}
