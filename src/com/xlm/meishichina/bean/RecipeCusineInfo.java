package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R.xml;
import android.util.Xml;

public class RecipeCusineInfo extends Entity
{

    private String total;
    private String idx;
    private String size;
    private String count;
    private List<RecipeCusineItem> mRecipeCusineInfos = new ArrayList<RecipeCusineItem>();

    public static class RecipeCusineItem implements Serializable
    {

        private String id;
        private String uid;
        private String recipetitle;
        private String author;
        private String descr;
        private String mainingredient;
        private String cover;
        private String viewnum;
        private String replynum;
        private String favnum;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getUid()
        {
            return uid;
        }

        public void setUid(String uid)
        {
            this.uid = uid;
        }

        public String getRecipetitle()
        {
            return recipetitle;
        }

        public void setRecipetitle(String recipetitle)
        {
            this.recipetitle = recipetitle;
        }

        public String getAuthor()
        {
            return author;
        }

        public void setAuthor(String author)
        {
            this.author = author;
        }

        public String getDescr()
        {
            return descr;
        }

        public void setDescr(String descr)
        {
            this.descr = descr;
        }

        public String getMainingredient()
        {
            return mainingredient;
        }

        public void setMainingredient(String mainingredient)
        {
            this.mainingredient = mainingredient;
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

        public String getFavnum()
        {
            return favnum;
        }

        public void setFavnum(String favnum)
        {
            this.favnum = favnum;
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

    public List<RecipeCusineItem> getmRecipeCusineInfos()
    {
        return mRecipeCusineInfos;
    }

    public void setmRecipeCusineInfos(List<RecipeCusineItem> mRecipeCusineInfos)
    {
        this.mRecipeCusineInfos = mRecipeCusineInfos;
    }

    public static RecipeCusineInfo parse(InputStream mInputStream)
            throws IOException
    {
        RecipeCusineInfo mRecipeCusineInfo = null;
        RecipeCusineItem mRecipeCusineItem = null;
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
                            mRecipeCusineInfo = new RecipeCusineInfo();
                        }
                        else if (null != mRecipeCusineInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                mRecipeCusineInfo.setError_code(xmlParser
                                        .nextText());

                            }
                            else if (mRecipeCusineInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {
                                if (tag.equalsIgnoreCase("total"))
                                {
                                    mRecipeCusineInfo.setTotal(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("idx"))
                                {
                                    mRecipeCusineInfo.setIdx(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("size"))
                                {
                                    mRecipeCusineInfo.setSize(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("count"))
                                {
                                    mRecipeCusineInfo.setCount(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("item"))
                                {
                                    mRecipeCusineItem = new RecipeCusineItem();
                                }
                                else if (mRecipeCusineItem != null)
                                {
                                    if (tag.equalsIgnoreCase("id"))
                                    {
                                        mRecipeCusineItem.setId(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("uid"))
                                    {
                                        mRecipeCusineItem.setUid(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("author"))
                                    {
                                        mRecipeCusineItem.setAuthor(xmlParser
                                                .nextText());
                                    }
                                    else if (tag
                                            .equalsIgnoreCase("recipetitle"))
                                    {
                                        mRecipeCusineItem
                                                .setRecipetitle(xmlParser
                                                        .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("descr"))
                                    {
                                        mRecipeCusineItem.setDescr(xmlParser
                                                .nextText());
                                    }
                                    else if (tag
                                            .equalsIgnoreCase("mainingredient"))
                                    {
                                        mRecipeCusineItem
                                                .setMainingredient(xmlParser
                                                        .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("cover"))
                                    {
                                        mRecipeCusineItem.setCover(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("viewnum"))
                                    {
                                        mRecipeCusineItem.setViewnum(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("replynum"))
                                    {
                                        mRecipeCusineItem.setReplynum(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("favnum"))
                                    {
                                        mRecipeCusineItem.setFavnum(xmlParser
                                                .nextText());
                                    }
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase("item")
                                && mRecipeCusineInfo != null
                                && mRecipeCusineItem != null)
                        {
                            mRecipeCusineInfo.getmRecipeCusineInfos().add(
                                    mRecipeCusineItem);
                            mRecipeCusineItem = null;
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
        return mRecipeCusineInfo;
    }
}
