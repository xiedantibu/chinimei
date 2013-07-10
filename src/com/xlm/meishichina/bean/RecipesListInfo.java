package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class RecipesListInfo extends Entity
{

    private String id;
    private String total;
    private String idx;
    private String size;
    private String count;
    private List<RecipeItem> mRecipeItems = new ArrayList<RecipeItem>();

    public static class RecipeItem implements Serializable
    {

        private String id;
        private String uid;
        private String author;
        private String recipetitle;
        private String descr;
        private String mainingredient;
        private String cover;

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

        public String getRecipetitle()
        {
            return recipetitle;
        }

        public void setRecipetitle(String recipetitle)
        {
            this.recipetitle = recipetitle;
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

    public List<RecipeItem> getmRecipeItems()
    {
        return mRecipeItems;
    }

    public void setmRecipeItems(List<RecipeItem> mRecipeItems)
    {
        this.mRecipeItems = mRecipeItems;
    }

    public static RecipesListInfo parse(InputStream mInputStream)
            throws IOException
    {
        RecipesListInfo favRecipesList = null;
        RecipeItem RecipeItem = null;
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
                            favRecipesList = new RecipesListInfo();
                        }
                        else if (null != favRecipesList)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                favRecipesList.setError_code(xmlParser
                                        .nextText());

                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                favRecipesList.setError_descr(xmlParser
                                        .nextText());
                            }
                            else if (favRecipesList.getError_code()
                                    .equalsIgnoreCase("1"))
                            {
                                if (tag.equalsIgnoreCase("id")
                                        && RecipeItem == null)
                                {
                                    favRecipesList.setId(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("total"))
                                {
                                    favRecipesList.setTotal(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("idx"))
                                {
                                    favRecipesList.setIdx(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("size"))
                                {
                                    favRecipesList
                                            .setSize(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("count"))
                                {
                                    favRecipesList.setCount(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("item"))
                                {
                                    RecipeItem = new RecipeItem();
                                }
                                else if (RecipeItem != null)
                                {
                                    if (tag.equalsIgnoreCase("id"))
                                    {
                                        RecipeItem.setId(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("uid"))
                                    {
                                        RecipeItem.setUid(xmlParser.nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("author"))
                                    {
                                        RecipeItem.setAuthor(xmlParser
                                                .nextText());
                                    }
                                    else if (tag
                                            .equalsIgnoreCase("recipetitle"))
                                    {
                                        RecipeItem.setRecipetitle(xmlParser
                                                .nextText());

                                    }
                                    else if (tag.equalsIgnoreCase("descr"))
                                    {
                                        RecipeItem.setDescr(xmlParser
                                                .nextText());
                                    }
                                    else if (tag
                                            .equalsIgnoreCase("mainingredient"))
                                    {
                                        RecipeItem.setMainingredient(xmlParser
                                                .nextText());
                                    }
                                    else if (tag.equalsIgnoreCase("cover"))
                                    {
                                        RecipeItem.setCover(xmlParser
                                                .nextText());
                                    }
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tag.equalsIgnoreCase("item") && RecipeItem != null
                                && favRecipesList != null)
                        {
                            favRecipesList.getmRecipeItems().add(RecipeItem);
                            RecipeItem = null;
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

        return favRecipesList;

    }
}
