package com.xlm.meishichina.util;

import android.util.Log;

/**
 * Class Name: LogX.java Class Description: This class is an get the info of log
 * about info\error.... Version History: Date:2012/09/11 Issue: Description:
 * Author:
 * 
 * Copyright (c) 1994-1999 Suning.This software is the confidential and
 * proprietary information of Suning.All rights reserved.
 */

public final class Logmeishi implements MeishiConfig
{

    /**
     * log name
     */
    private static final String TAG = "meishi-debug";

    public static void e(Object object, String err)
    {
        if (IS_DEBUG)
        {
            Log.e(getPureClassName(object), err);
        }
    }

    public static void e(String tag, String err)
    {
        if (IS_DEBUG)
        {
            Log.e(tag, err);
        }
    }

    public static void e(String msg)
    {
        if (IS_DEBUG)
        {
            Log.e(TAG, msg);
        }
    }

    public static void v(String tag, String err)
    {
        if (IS_DEBUG)
        {
            Log.v(tag, err);
        }
    }

    public static void v(String msg)
    {
        if (IS_DEBUG)
        {
            Log.v(TAG, msg);
        }
    }
    
    public static void d(Object object, String debug)
    {
        if (IS_DEBUG)
        {
            Log.d(getPureClassName(object), debug);
        }
    }

    public static void d(String tag, String debug)
    {
        if (IS_DEBUG)
        {
            Log.d(tag, debug);
        }
    }

    public static void d(String tag, String debug, Throwable e)
    {
        if (IS_DEBUG)
        {
            Log.d(tag, debug, e);
        }
    }

    public static void d(String msg)
    {
        if (IS_DEBUG)
        {
            Log.d(TAG, msg);
        }
    }

    public static void i(Object object, String info)
    {
        if (IS_DEBUG)
        {
            Log.i(getPureClassName(object), info);
        }
    }

    public static void i(String msg)
    {
        if (IS_DEBUG)
        {
            Log.i(TAG, msg);
        }
    }

    public static void w(Object object, String info)
    {
        if (IS_DEBUG)
        {
            Log.w(getPureClassName(object), info);
        }
    }

    public static void w(String msg)
    {
        if (IS_DEBUG)
        {
            Log.w(TAG, msg);
        }
    }

    /**
     * 
     * @param object
     * 
     * @param tr
     * 
     */
    public static void jw(Object object, Throwable tr)
    {
        if (IS_DEBUG)
        {
            Log.w(getPureClassName(object), "", filterThrowable(tr));
        }
    }

    /**
     * 
     * @param object
     * 
     * @param tr
     * 
     */
    public static void je(Object object, Throwable tr)
    {
        if (IS_DEBUG)
        {
            Log.e(getPureClassName(object), "", filterThrowable(tr));
        }
    }

    private static Throwable filterThrowable(Throwable tr)
    {
        StackTraceElement[] ste = tr.getStackTrace();
        tr.setStackTrace(new StackTraceElement[] { ste[0] });
        return tr;
    }

    private static String getPureClassName(Object object)
    {
        String name = object.getClass().getName();
        if ("java.lang.String".equals(name))
        {
            return object.toString();
        }
        int idx = name.lastIndexOf('.');
        if (idx > 0)
        {
            return name.substring(idx + 1);
        }
        return name;
    }

}
