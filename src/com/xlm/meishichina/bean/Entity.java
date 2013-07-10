package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class Entity implements Serializable
{
    /**
     * @Fields serialVersionUID : TODO
     */

    private static final long serialVersionUID = 1L;

    public static final String UTF_8 = "utf-8";
    /**
     * error_code ：1 代表正确 -1 代表错误
     */
    protected String error_code = "0";
    /**
     * error_descr success:代表正确
     */
    protected String error_descr;

    public String getError_code()
    {
        return error_code;
    }

    public void setError_code(String error_code)
    {
        this.error_code = error_code;
    }

    public String getError_descr()
    {
        return error_descr;
    }

    public void setError_descr(String error_descr)
    {
        this.error_descr = error_descr;
    }

    public static Entity parseEntity(InputStream mInputStream)
            throws IOException
    {
        Entity entity = null;
        XmlPullParser xmlParser = Xml.newPullParser();
        try
        {
            xmlParser.setInput(mInputStream, UTF_8);
            int evtType = xmlParser.getEventType();
            while (evtType != XmlPullParser.END_DOCUMENT)
            {
                String tag = xmlParser.getName();
                switch (evtType)
                {
                    case XmlPullParser.START_TAG:
                        if (tag.equalsIgnoreCase("infos") && null == entity)
                        {
                            entity = new Entity();

                        }
                        else if (null != entity)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                entity.setError_code(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                entity.setError_descr(xmlParser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 如果xml没有结束，则导航到下一个节点
                evtType = xmlParser.next();
            }
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        finally
        {
            mInputStream.close();
        }

        return entity;
    }

}
