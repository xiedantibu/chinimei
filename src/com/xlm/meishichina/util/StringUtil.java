package com.xlm.meishichina.util;

public class StringUtil
{

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * 
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input)
    {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转整数
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch (Exception e)
        {
        }
        return defValue;
    }

    /**
     * 对象转整数
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj)
    {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj)
    {
        try
        {
            return Long.parseLong(obj);
        }
        catch (Exception e)
        {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     * 
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b)
    {
        try
        {
            return Boolean.parseBoolean(b);
        }
        catch (Exception e)
        {
        }
        return false;
    }
}
