package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class UserDetailInfo extends Entity
{
    private String id;
    private String name;
    private String reg_time;
    private String avatar_big;
    private String fav_recipe_num;
    private String report_num;
    private String recipe_num;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getReg_time()
    {
        return reg_time;
    }

    public void setReg_time(String reg_time)
    {
        this.reg_time = reg_time;
    }

    public String getAvatar_big()
    {
        return avatar_big;
    }

    public void setAvatar_big(String avatar_big)
    {
        this.avatar_big = avatar_big;
    }

    public String getFav_recipe_num()
    {
        return fav_recipe_num;
    }

    public void setFav_recipe_num(String fav_recipe_num)
    {
        this.fav_recipe_num = fav_recipe_num;
    }

    public String getReport_num()
    {
        return report_num;
    }

    public void setReport_num(String report_num)
    {
        this.report_num = report_num;
    }

    public String getRecipe_num()
    {
        return recipe_num;
    }

    public void setRecipe_num(String recipe_num)
    {
        this.recipe_num = recipe_num;
    }

    public static UserDetailInfo parse(InputStream mInputStream)
            throws IOException
    {
        UserDetailInfo mUserDetailInfo = null;

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

                            mUserDetailInfo = new UserDetailInfo();
                        }
                        else if (null != mUserDetailInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                mUserDetailInfo.setError_code(xmlParser
                                        .nextText());

                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                mUserDetailInfo.setError_descr(xmlParser
                                        .nextText());
                            }
                            else if (mUserDetailInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {
                                if (tag.equalsIgnoreCase("id"))
                                {
                                    mUserDetailInfo.setId(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("name"))
                                {
                                    mUserDetailInfo.setName(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("reg_time"))
                                {
                                    mUserDetailInfo.setReg_time(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("avatar_big"))
                                {
                                    mUserDetailInfo.setAvatar_big(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("recipe_num"))
                                {
                                    mUserDetailInfo.setRecipe_num(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("fav_recipe_num"))
                                {
                                    mUserDetailInfo.setFav_recipe_num(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("report_num"))
                                {
                                    mUserDetailInfo.setReport_num(xmlParser
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            mInputStream.close();
        }

        return mUserDetailInfo;
    }
}
