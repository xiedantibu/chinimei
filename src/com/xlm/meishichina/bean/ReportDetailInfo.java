package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class ReportDetailInfo extends Entity
{

    private String recipeid;
    private String uid;
    private String author;
    private String avator;
    private String descr;
    private String pic;
    private String time;

    public String getRecipeid()
    {
        return recipeid;
    }

    public void setRecipeid(String recipeid)
    {
        this.recipeid = recipeid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getAvator()
    {
        return avator;
    }

    public void setAvator(String avator)
    {
        this.avator = avator;
    }

    public String getDescr()
    {
        return descr;
    }

    public void setDescr(String descr)
    {
        this.descr = descr;
    }

    public String getPic()
    {
        return pic;
    }

    public void setPic(String pic)
    {
        this.pic = pic;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public static ReportDetailInfo prase(InputStream mInputStream)
            throws IOException
    {
        ReportDetailInfo mReportDetailInfo = null;

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
                        if (tag.equalsIgnoreCase("infos"))
                        {

                            mReportDetailInfo = new ReportDetailInfo();
                        }
                        else if (null != mReportDetailInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                mReportDetailInfo.setError_code(xmlParser
                                        .nextText());

                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                mReportDetailInfo.setError_descr(xmlParser
                                        .nextText());
                            }
                            else if (mReportDetailInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {

                                if (tag.equalsIgnoreCase("recipeid"))
                                {
                                    mReportDetailInfo.setRecipeid(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("uid"))
                                {
                                    mReportDetailInfo.setUid(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("author"))
                                {
                                    mReportDetailInfo.setAuthor(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("descr"))
                                {
                                    mReportDetailInfo.setDescr(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("avatar"))
                                {
                                    mReportDetailInfo.setAvator(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("pic"))
                                {
                                    mReportDetailInfo.setPic(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("time"))
                                {
                                    mReportDetailInfo.setTime(xmlParser
                                            .nextText());
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
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

        return mReportDetailInfo;
    }
}
