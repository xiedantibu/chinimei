package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class SpreadInfo extends Entity
{
    private String idx;
    private String size;
    private String count;
    private List<SpreadItem> mSpreadItems = new ArrayList<SpreadItem>();

    public static class SpreadItem implements Serializable
    {
        private String id;
        private String title;
        private String cover;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
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

    public List<SpreadItem> getmSpreadItem()
    {
        return mSpreadItems;
    }

    public void setmSpreadItem(List<SpreadItem> mSpreadItem)
    {
        this.mSpreadItems = mSpreadItem;
    }

    public static SpreadInfo parse(InputStream mInputStream) throws IOException
    {
        SpreadInfo mInfo = null;
        SpreadItem mItem = null;
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

                            mInfo = new SpreadInfo();
                        }
                        else if (null != mInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                mInfo.setError_code(xmlParser.nextText());

                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                mInfo.setError_descr(xmlParser.nextText());
                            }
                            else if (mInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {
                                if (tag.equalsIgnoreCase("idx"))
                                {
                                    mInfo.setIdx(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("size"))
                                {
                                    mInfo.setSize(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("count"))
                                {
                                    mInfo.setCount(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("item"))
                                {
                                    mItem = new SpreadItem();
                                }
                                else if (null != mItem)
                                {
                                    if (tag.equalsIgnoreCase("id"))
                                    {
                                        mItem.setId(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("title"))
                                    {
                                        mItem.setTitle(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("cover"))
                                    {
                                        mItem.setCover(xmlParser.nextText());
                                    }
                                }
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:

                        if (tag.equalsIgnoreCase("item") && mInfo != null
                                && mItem != null)
                        {
                            mInfo.getmSpreadItem().add(mItem);
                            mItem = null;
                        }
                        break;
                }
                evtType = xmlParser.next();
            }
        }
        catch (XmlPullParserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            mInputStream.close();
        }

        return mInfo;
    }
}
