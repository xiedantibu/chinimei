package com.xlm.meishichina.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ViewUtils
{

    public static float localDisplayMetrics(Context paramContext, int paramInt)
    {
        DisplayMetrics localDisplayMetrics = paramContext.getResources()
                .getDisplayMetrics();
        return TypedValue.applyDimension(1, paramInt, localDisplayMetrics);
    }

    public static int densityDpi(Context paramContext)
    {
        return paramContext.getResources().getDisplayMetrics().densityDpi;
    }

    public static int heightPixels(Context paramContext)
    {
        return paramContext.getResources().getDisplayMetrics().heightPixels;
    }

    public static int widthPixels(Context paramContext)
    {
        return paramContext.getResources().getDisplayMetrics().widthPixels;
    }
}
