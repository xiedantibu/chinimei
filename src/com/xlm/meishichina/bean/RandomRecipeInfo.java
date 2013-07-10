package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.xlm.meishichina.db.annotation.Id;

import android.util.Xml;

public class RandomRecipeInfo extends Entity
{

    /**
     * @Fields serialVersionUID : TODO
     * 
     *         http://home.meishichina.com/apps/client/sprite/ic.php?ac=recipe&
     *         op=rand&level=0&cuisine=0&during=0&xingzuo=0
     */

    /**
     * id Ëæ»ú²ËÆ×ID
     */
    @Id(column = "id")
    private String id;
    /**
     * µ±Ç°²ËÆ×µÄ×÷Õß
     */
    private String uId;

    /**
     * recipeTitle ²ËÆ×±êÌâ
     */
    private String recipeTitle;

    /**
     * recipeAuthor ²ËÆ××÷Õß
     */
    private String recipeAuthor;

    /**
     * recipeDescr ²ËÆ×ÃèÊö
     */
    private String recipeDescr;

    /**
     * recipeCover ²ËÆ×Í¼Æ¬
     */
    private String recipeCover;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUId()
    {
        return uId;
    }

    public void setUId(String uId)
    {
        this.uId = uId;
    }

    public String getRecipeTitle()
    {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle)
    {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeAuthor()
    {
        return recipeAuthor;
    }

    public void setRecipeAuthor(String recipeAuthor)
    {
        this.recipeAuthor = recipeAuthor;
    }

    public String getRecipeDescr()
    {
        return recipeDescr;
    }

    public void setRecipeDescr(String recipeDescr)
    {
        this.recipeDescr = recipeDescr;
    }

    public String getRecipeCover()
    {
        return recipeCover;
    }

    public void setRecipeCover(String recipeCover)
    {
        this.recipeCover = recipeCover;
    }


    public static RandomRecipeInfo pares(InputStream inputStream)
            throws IOException
    {
        RandomRecipeInfo randomRecipeInfo = null;

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
                            randomRecipeInfo = new RandomRecipeInfo();
                        }
                        else if (null != randomRecipeInfo)
                        {

                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                randomRecipeInfo.setError_code(xmlParser
                                        .nextText());
                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {

                                randomRecipeInfo.setError_descr(xmlParser
                                        .nextText());
                            }
                            else if (randomRecipeInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {

                                if (tag.equalsIgnoreCase("id"))
                                {
                                    randomRecipeInfo
                                            .setId(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("uid"))
                                {

                                    randomRecipeInfo.setUId(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("title"))
                                {
                                    randomRecipeInfo.setRecipeTitle(xmlParser
                                            .nextText());

                                }
                                else if (tag.equalsIgnoreCase("author"))
                                {
                                    randomRecipeInfo.setRecipeAuthor(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("descr"))
                                {
                                    randomRecipeInfo.setRecipeDescr(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("cover"))
                                {
                                    randomRecipeInfo.setRecipeCover(xmlParser
                                            .nextText());
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
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
        return randomRecipeInfo;

    }
}
