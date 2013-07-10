package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class RecipesCollectListInfo extends Entity
{

    private String total;
    private String idx;
    private String size;
    private String count;
    private List<RecipesCollectItem> mRecipesCollectItems = new ArrayList<RecipesCollectListInfo.RecipesCollectItem>();

    public static class RecipesCollectItem implements Serializable
    {
        private String id;
        private String uid;
        private String author;
        private String title;
        private String cover;
        private String message;

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

        public String getAuthor()
        {
            return author;
        }

        public void setAuthor(String author)
        {
            this.author = author;
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

        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
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

    public List<RecipesCollectItem> getmRecipesCollectItems()
    {
        return mRecipesCollectItems;
    }

    public void setmRecipesCollectItems(
            List<RecipesCollectItem> mRecipesCollectItems)
    {
        this.mRecipesCollectItems = mRecipesCollectItems;
    }

    public static RecipesCollectListInfo parse(InputStream mInputStream)
            throws IOException
    {
        RecipesCollectListInfo mRecipesCollectListInfo = null;
        RecipesCollectItem mRecipesCollectItem = null;
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
                            mRecipesCollectListInfo = new RecipesCollectListInfo();
                        }
                        else if (null != mRecipesCollectListInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                mRecipesCollectListInfo.setError_code(xmlParser
                                        .nextText());

                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                mRecipesCollectListInfo
                                        .setError_descr(xmlParser.nextText());
                            }
                            else if (mRecipesCollectListInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {
                                if (tag.equalsIgnoreCase("total"))
                                {
                                    mRecipesCollectListInfo.setTotal(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("idx"))
                                {
                                    mRecipesCollectListInfo.setIdx(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("size"))
                                {
                                    mRecipesCollectListInfo.setSize(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("count")
                                        && mRecipesCollectItem == null)
                                {
                                    mRecipesCollectListInfo.setCount(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("item"))
                                {
                                    mRecipesCollectItem = new RecipesCollectItem();
                                }
                                else if (mRecipesCollectItem != null)
                                {

                                    if (tag.equalsIgnoreCase("id"))
                                    {
                                        mRecipesCollectItem.setId(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("uid"))
                                    {
                                        mRecipesCollectItem.setUid(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("author"))
                                    {
                                        mRecipesCollectItem.setAuthor(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("title"))
                                    {
                                        mRecipesCollectItem.setTitle(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("cover"))
                                    {
                                        mRecipesCollectItem.setCover(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("message"))
                                    {
                                        mRecipesCollectItem
                                                .setMessage(xmlParser
                                                        .nextText());
                                    }
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase("item")
                                && mRecipesCollectItem != null
                                && mRecipesCollectListInfo != null)
                        {
                            mRecipesCollectListInfo.getmRecipesCollectItems()
                                    .add(mRecipesCollectItem);
                            mRecipesCollectItem = null;
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

        return mRecipesCollectListInfo;

    }
}
