package com.xlm.meishichina.util;

public class StringUtil
{

    /**
     * �жϸ����ַ����Ƿ�հ״��� �հ״���ָ�ɿո��Ʊ�����س��������з���ɵ��ַ��� �������ַ���Ϊnull����ַ���������true
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
     * �ַ���ת����
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
     * ����ת����
     * 
     * @param obj
     * @return ת���쳣���� 0
     */
    public static int toInt(Object obj)
    {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * ����ת����
     * 
     * @param obj
     * @return ת���쳣���� 0
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
     * �ַ���ת����ֵ
     * 
     * @param b
     * @return ת���쳣���� false
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
