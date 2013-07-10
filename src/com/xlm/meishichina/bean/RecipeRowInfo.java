package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class RecipeRowInfo extends Entity
{

    private String cur;
    private String pages;
    private String count;
    private List<RecipeRowItem> mRowItems = new ArrayList<RecipeRowInfo.RecipeRowItem>();

    public static class RecipeRowItem implements Serializable
    {

        private String subject;
        private String link;
        private String cover;
        private String mainingredient;
        private String dateline;
        private String replynum;
        private String viewnum;
        private String collnum;

        public String getSubject()
        {
            return subject;
        }

        public void setSubject(String subject)
        {
            this.subject = subject;
        }

        public String getLink()
        {
            return link;
        }

        public void setLink(String link)
        {
            this.link = link;
        }

        public String getCover()
        {
            return cover;
        }

        public void setCover(String cover)
        {
            this.cover = cover;
        }

        public String getMainingredient()
        {
            return mainingredient;
        }

        public void setMainingredient(String mainingredient)
        {
            this.mainingredient = mainingredient;
        }

        public String getDateline()
        {
            return dateline;
        }

        public void setDateline(String dateline)
        {
            this.dateline = dateline;
        }

        public String getReplynum()
        {
            return replynum;
        }

        public void setReplynum(String replynum)
        {
            this.replynum = replynum;
        }

        public String getViewnum()
        {
            return viewnum;
        }

        public void setViewnum(String viewnum)
        {
            this.viewnum = viewnum;
        }

        public String getCollnum()
        {
            return collnum;
        }

        public void setCollnum(String collnum)
        {
            this.collnum = collnum;
        }

    }

    public String getCur()
    {
        return cur;
    }

    public void setCur(String cur)
    {
        this.cur = cur;
    }

    public String getPages()
    {
        return pages;
    }

    public void setPages(String pages)
    {
        this.pages = pages;
    }

    public String getCount()
    {
        return count;
    }

    public void setCount(String count)
    {
        this.count = count;
    }

    public List<RecipeRowItem> getmRowItems()
    {
        return mRowItems;
    }

    public void setmRowItems(List<RecipeRowItem> mRowItems)
    {
        this.mRowItems = mRowItems;
    }

    public static RecipeRowInfo parse(InputStream mInputStream)
            throws IOException
    {

        RecipeRowInfo mRecipeRowInfo = null;
        RecipeRowItem mRowItem = null;
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
                        if (tag.equalsIgnoreCase("recipelist"))
                        {
                            mRecipeRowInfo = new RecipeRowInfo();
                        }
                        else if (null != mRecipeRowInfo)
                        {
                            if (tag.equalsIgnoreCase("cur"))
                            {
                                mRecipeRowInfo.setCur(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("pages"))
                            {
                                mRecipeRowInfo.setPages(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("count"))
                            {
                                mRecipeRowInfo.setCount(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("item"))
                            {
                                mRowItem = new RecipeRowItem();
                            }
                            else if (mRowItem != null)
                            {
                                if (tag.equalsIgnoreCase("subject"))
                                {
                                    mRowItem.setSubject(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("link"))
                                {
                                    mRowItem.setLink(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("cover120"))
                                {
                                    mRowItem.setCover(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("mainingredient"))
                                {
                                    mRowItem.setMainingredient(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("collnum"))
                                {
                                    mRowItem.setCollnum(xmlParser.nextText());

                                }
                                else if (tag.equalsIgnoreCase("viewnum"))
                                {
                                    mRowItem.setViewnum(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("replynum"))
                                {
                                    mRowItem.setReplynum(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("dateline"))
                                {
                                    mRowItem.setDateline(xmlParser.nextText());

                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase("item")
                                && mRecipeRowInfo != null && mRowItem != null)
                        {
                            mRecipeRowInfo.getmRowItems().add(mRowItem);
                            mRowItem = null;
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
        return mRecipeRowInfo;
    }

};
