package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.xlm.meishichina.util.Logmeishi;

import android.R.xml;
import android.util.Xml;

public class CommentInfo extends Entity
{

    private String id;
    private String total;
    private String idx;
    /**
     * size 代表当前请求数据条数
     */
    private String size;
    /**
     * count 代表当前请求返回数据条数
     */
    private String count;
    /**
     * order 返回的数据按时间返回
     */
    private String order;

    private List<CommentItem> mCommentItems = new ArrayList<CommentInfo.CommentItem>();

    public static class CommentItem implements Serializable
    {
        private String id;
        private String uId;
        private String author;
        private String ucover;
        private String message;
        private String time;
        private String befrom;

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
            return uId;
        }

        public void setUid(String uId)
        {
            this.uId = uId;
        }

        public String getAuthor()
        {
            return author;
        }

        public void setAuthor(String author)
        {
            this.author = author;
        }

        public String getUcover()
        {
            return ucover;
        }

        public void setUcover(String ucover)
        {
            this.ucover = ucover;
        }

        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }

        public String getTime()
        {
            return time;
        }

        public void setTime(String time)
        {
            this.time = time;
        }

        public String getBefrom()
        {
            return befrom;
        }

        public void setBefrom(String befrom)
        {
            this.befrom = befrom;
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

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public List<CommentItem> getmCommentItems()
    {
        return mCommentItems;
    }

    public void setmCommentItems(List<CommentItem> mCommentItems)
    {
        this.mCommentItems = mCommentItems;
    }

    public static CommentInfo parse(InputStream inputStream) throws IOException
    {
        CommentInfo mCommentInfo = null;
        CommentItem mCommentItem = null;

        XmlPullParser xmlParser = Xml.newPullParser();

        try
        {
            xmlParser.setInput(inputStream, UTF_8);
            int evtType = xmlParser.getEventType();

            while (evtType != XmlPullParser.END_DOCUMENT)
            {
                String tag = xmlParser.getName();
                switch (evtType)
                {
                    case XmlPullParser.START_TAG:
                        if (tag.equalsIgnoreCase("infos"))
                        {
                            mCommentInfo = new CommentInfo();
                        }
                        else if (null != mCommentInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                mCommentInfo
                                        .setError_code(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {

                                mCommentInfo.setError_descr(xmlParser
                                        .nextText());
                            }
                            else if (mCommentInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {

                                if (null == mCommentItem
                                        && tag.equalsIgnoreCase("id"))
                                {
                                    mCommentInfo.setId(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("total"))
                                {
                                    mCommentInfo.setTotal(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("idx"))
                                {
                                    mCommentInfo.setIdx(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("size"))
                                {
                                    mCommentInfo.setSize(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("count"))
                                {
                                    mCommentInfo.setCount(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("order"))
                                {
                                    mCommentInfo.setOrder(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("item"))
                                {
                                    mCommentItem = new CommentItem();
                                }

                                else if (null != mCommentItem
                                        && tag.equalsIgnoreCase("id"))
                                {
                                    mCommentItem.setId(xmlParser.nextText());
                                }
                                else if (null != mCommentItem
                                        && tag.equalsIgnoreCase("uid"))
                                {
                                    mCommentItem.setUid(xmlParser.nextText());
                                }
                                else if (null != mCommentItem
                                        && tag.equalsIgnoreCase("author"))
                                {
                                    mCommentItem
                                            .setAuthor(xmlParser.nextText());
                                }
                                else if (null != mCommentItem
                                        && tag.equalsIgnoreCase("ucover"))
                                {
                                    mCommentItem
                                            .setUcover(xmlParser.nextText());
                                }
                                else if (null != mCommentItem
                                        && tag.equalsIgnoreCase("message"))
                                {
                                    mCommentItem.setMessage(xmlParser
                                            .nextText());
                                }
                                else if (null != mCommentItem
                                        && tag.equalsIgnoreCase("time"))
                                {
                                    mCommentItem.setTime(xmlParser.nextText());
                                }
                                else if (null != mCommentItem
                                        && tag.equalsIgnoreCase("befrom"))
                                {
                                    mCommentItem
                                            .setBefrom(xmlParser.nextText());
                                }
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase("item")
                                && mCommentInfo != null && mCommentItem != null)
                        {
                            mCommentInfo.getmCommentItems().add(mCommentItem);
                            mCommentItem = null;
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
            inputStream.close();
        }
        return mCommentInfo;

    }
}
