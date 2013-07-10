package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class PersonalReportListInfo extends Entity
{

    private String id;
    private String total;
    private String idx;
    private String size;
    private String count;
    private List<PersonalReportItem> reportItems = new ArrayList<PersonalReportItem>();

    public static class PersonalReportItem implements Serializable
    {

        private String id;
        private String recipeid;
        private String recipetitle;
        private String cover;
        private String viewnum;
        private String replynum;
        private String time;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getRecipeid()
        {
            return recipeid;
        }

        public void setRecipeid(String recipeid)
        {
            this.recipeid = recipeid;
        }

        public String getRecipetitle()
        {
            return recipetitle;
        }

        public void setRecipetitle(String recipetitle)
        {
            this.recipetitle = recipetitle;
        }

        public String getCover()
        {
            return cover;
        }

        public void setCover(String cover)
        {
            this.cover = cover;
        }

        public String getViewnum()
        {
            return viewnum;
        }

        public void setViewnum(String viewnum)
        {
            this.viewnum = viewnum;
        }

        public String getReplynum()
        {
            return replynum;
        }

        public void setReplynum(String replynum)
        {
            this.replynum = replynum;
        }

        public String getTime()
        {
            return time;
        }

        public void setTime(String time)
        {
            this.time = time;
        }

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

    public List<PersonalReportItem> getReportItems()
    {
        return reportItems;
    }

    public void setReportItems(List<PersonalReportItem> reportItems)
    {
        this.reportItems = reportItems;
    }

    public static PersonalReportListInfo parse(InputStream mInputStream)
            throws IOException
    {
        PersonalReportListInfo mReportListInfo = null;
        PersonalReportItem mReportItem = null;

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
                            mReportListInfo = new PersonalReportListInfo();
                        }
                        else if (null != mReportListInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                mReportListInfo.setError_code(xmlParser
                                        .nextText());

                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                mReportListInfo.setError_descr(xmlParser
                                        .nextText());
                            }
                            else if (mReportListInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {
                                if (tag.equalsIgnoreCase("id")
                                        && mReportItem == null)
                                {
                                    mReportListInfo.setId(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("total"))
                                {
                                    mReportListInfo.setTotal(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("idx"))
                                {
                                    mReportListInfo
                                            .setIdx(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("size"))
                                {
                                    mReportListInfo.setSize(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("count"))
                                {
                                    mReportListInfo.setCount(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("item"))
                                {
                                    mReportItem = new PersonalReportItem();
                                }
                                else if (mReportItem != null)
                                {
                                    if (tag.equalsIgnoreCase("id"))
                                    {
                                        mReportItem.setId(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("recipeid"))
                                    {
                                        mReportItem.setRecipeid(xmlParser
                                                .nextText());
                                    }
                                    else if (tag
                                            .equalsIgnoreCase("recipetitle"))
                                    {
                                        mReportItem.setRecipetitle(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("cover"))
                                    {
                                        mReportItem.setCover(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("viewnum"))
                                    {
                                        mReportItem.setViewnum(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("replynum"))
                                    {
                                        mReportItem.setReplynum(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("time"))
                                    {
                                        mReportItem.setTime(xmlParser
                                                .nextText());
                                    }
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase("item") && mReportItem != null
                                && mReportListInfo != null)
                        {
                            mReportListInfo.getReportItems().add(mReportItem);
                            mReportItem = null;
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

        return mReportListInfo;
    }

}
