package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class ReportListInfo extends Entity
{

    private String total;
    private String idx;
    private String size;
    private String count;

    private List<ReportItem> mReportItems = new ArrayList<ReportItem>();

    public static class ReportItem implements Serializable
    {
        private String id;
        private String author;
        private String message;
        private String cover;
        private String time;

        public String getTime()
        {
            return time;
        }

        public void setTime(String time)
        {
            this.time = time;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getAuthor()
        {
            return author;
        }

        public void setAuthor(String author)
        {
            this.author = author;
        }

        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }

        public String getCover()
        {
            return cover;
        }

        public void setCover(String cover)
        {
            this.cover = cover;
        }

    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }

    public String getIdx()
    {
        return idx;
    }

    public void setIdx(String idx)
    {
        this.idx = idx;
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getCount()
    {
        return count;
    }

    public void setCount(String count)
    {
        this.count = count;
    }

    public List<ReportItem> getmReportItems()
    {
        return mReportItems;
    }

    public void setmReportItems(List<ReportItem> mReportItems)
    {
        this.mReportItems = mReportItems;
    }

    public static ReportListInfo parse(InputStream mInputStream)
            throws IOException
    {
        ReportListInfo mListInfo = null;
        ReportItem mItem = null;

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
                            mListInfo = new ReportListInfo();
                        }
                        else if (null != mListInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                mListInfo.setError_code(xmlParser.nextText());

                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                mListInfo.setError_descr(xmlParser.nextText());
                            }
                            else if (mListInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {
                                if (tag.equalsIgnoreCase("total"))
                                {
                                    mListInfo.setTotal(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("idx"))
                                {
                                    mListInfo.setIdx(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("size"))
                                {
                                    mListInfo.setSize(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("count"))
                                {
                                    mListInfo.setCount(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("item"))
                                {
                                    mItem = new ReportItem();
                                }
                                else if (null != mItem)
                                {
                                    if (tag.equalsIgnoreCase("id"))
                                    {
                                        mItem.setId(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("author"))
                                    {
                                        mItem.setAuthor(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("message"))
                                    {
                                        mItem.setMessage(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("cover"))
                                    {
                                        mItem.setCover(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("time"))
                                    {
                                        mItem.setTime(xmlParser.nextText());
                                    }
                                }
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase("item") && mListInfo != null
                                && mItem != null)
                        {
                            mListInfo.getmReportItems().add(mItem);
                            mItem = null;
                        }
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

        return mListInfo;

    }
}
