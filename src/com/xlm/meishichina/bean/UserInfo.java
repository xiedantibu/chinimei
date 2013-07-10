package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.xlm.meishichina.ui.MeishiApplication;

import android.util.Xml;

/**
 * @ClassName: UserInfo
 * @Description: 
 *               http://home.meishichina.com/apps/client/sprite/ic.php?ac=user&op
 *               =info&id=260836
 * 
 *               http://home.meishichina.com/space-260836-do-recipe-cllection-
 *               report.html �û�ʵ����
 * @author xlm
 * @date 2013-2-4 ����11:41:04
 */
public class UserInfo extends Entity
{
    /**
     * @Fields serialVersionUID : TODO
     */

    private static final long serialVersionUID = 1L;

    /**
     * avatarBig ��ͷ��ͼƬ
     */
    private String avatarBig;
    /**
     * avatarMid ��ͷ��ͼƬ
     */
    private String avatarMid;
    /**
     * credit ����
     */
    private String credit;
    private String favCollectNum;
    /**
     * favPaiNum �ղص�����������
     */
    private String favPaiNum;
    /**
     * favRecipeNum �ղصĲ�����
     */
    private String favRecipeNum;
    /**
     * friendNum ��ע
     */
    private String friendNum;
    /**
     * paiNum �����ĵ�����
     */
    private String paiNum;
    /**
     * recipeNum ԭ������ ����
     */
    private String recipeNum;
    /**
     * regTime ע��ʱ��
     */
    private String regTime;
    /**
     * ��Ƭ���������
     */
    private String reportNum;
    /**
     * sid ��¼��session
     */
    private String sid;
    /**
     * �û���id uId
     */
    private String uId;
    /**
     * userName �û���
     */
    private String userName;
    /**
     * viewNum ��������
     */
    private String viewNum;

    public void clearUserInfo()
    {
        this.uId = null;
        this.userName = null;
        this.avatarMid = null;
        this.avatarBig = null;
        this.credit = null;
        this.regTime = null;
        this.friendNum = null;
        this.recipeNum = null;
        this.viewNum = null;
        this.paiNum = null;
        this.favRecipeNum = null;
        this.favCollectNum = null;
        this.favPaiNum = null;
        this.error_code = null;
        this.error_descr = null;
        this.sid = null;
    }

    public String getAvatarBig()
    {
        return this.avatarBig;
    }

    public String getAvatarMid()
    {
        return this.avatarMid;
    }

    public String getCredit()
    {
        return this.credit;
    }

    public String getFavCollectNum()
    {
        return this.favCollectNum;
    }

    public String getFavPaiNum()
    {
        return this.favPaiNum;
    }

    public String getFavRecipeNum()
    {
        return this.favRecipeNum;
    }

    public String getFriendNum()
    {
        return this.friendNum;
    }

    public String getPaiNum()
    {
        return this.paiNum;
    }

    public String getRecipeNum()
    {
        return this.recipeNum;
    }

    public String getRegTime()
    {
        return this.regTime;
    }

    public String getReportNum()
    {
        return this.reportNum;
    }

    public String getSid()
    {
        return this.sid;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public String getViewNum()
    {
        return this.viewNum;
    }

    public String getuId()
    {
        return this.uId;
    }

    public void setAvatarBig(String paramString)
    {
        this.avatarBig = paramString;
    }

    public void setAvatarMid(String paramString)
    {
        this.avatarMid = paramString;
    }

    public void setCredit(String paramString)
    {
        this.credit = paramString;
    }

    public void setFavCollectNum(String paramString)
    {
        this.favCollectNum = paramString;
    }

    public void setFavPaiNum(String paramString)
    {
        this.favPaiNum = paramString;
    }

    public void setFavRecipeNum(String paramString)
    {
        this.favRecipeNum = paramString;
    }

    public void setFriendNum(String paramString)
    {
        this.friendNum = paramString;
    }

    public void setPaiNum(String paramString)
    {
        this.paiNum = paramString;
    }

    public void setRecipeNum(String paramString)
    {
        this.recipeNum = paramString;
    }

    public void setRegTime(String paramString)
    {
        this.regTime = paramString;
    }

    public void setReportNum(String paramString)
    {
        this.reportNum = paramString;
    }

    public void setSid(String paramString)
    {
        this.sid = paramString;
    }

    public void setUserName(String paramString)
    {
        this.userName = paramString;
    }

    public void setViewNum(String paramString)
    {
        this.viewNum = paramString;
    }

    public void setuId(String paramString)
    {
        this.uId = paramString;
    }

    public static void parse(InputStream inputStream, UserInfo user)
            throws IOException
    {
        // UserInfo user = null;
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
                        if (tag.equalsIgnoreCase("infos") && null == user)
                        {
                            user = new UserInfo();
                        }
                        else if (null != user)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                user.setError_code(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                user.setError_descr(xmlParser.nextText());
                            }
                            else if (user.getError_code().equalsIgnoreCase("1"))
                            {
                                if (tag.equalsIgnoreCase("id"))
                                {

                                    user.setuId(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("name"))
                                {
                                    user.setUserName(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("credit"))
                                {

                                    user.setCredit(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("reg_time"))
                                {
                                    user.setRegTime(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("avatar_mid"))
                                {

                                    user.setAvatarMid(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("avatar_big"))
                                {
                                    user.setAvatarBig(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("view_num"))
                                {

                                    user.setViewNum(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("friend_num"))
                                {
                                    user.setFriendNum(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("recipe_num"))
                                {

                                    user.setRecipeNum(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("fav_recipe_num"))
                                {
                                    user.setFavRecipeNum(xmlParser.nextText());
                                }
                                else if (tag
                                        .equalsIgnoreCase("fav_collect_num"))
                                {

                                    user.setFavCollectNum(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("pai_num"))
                                {
                                    user.setPaiNum(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("fav_pai_num"))
                                {

                                    user.setFavPaiNum(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("report_num"))
                                {
                                    user.setReportNum(xmlParser.nextText());
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                // ���xmlû�н������򵼺�����һ���ڵ�
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

    }

    public static void praseOfLogin(InputStream inputStream, UserInfo user)
            throws IOException
    {
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
                        if (tag.equalsIgnoreCase("infos") && null == user)
                        {
                            user = new UserInfo();
                            MeishiApplication.getApplication().setUser(user);
                        }
                        else if (null != user)
                        {
                            if (tag.equalsIgnoreCase("sid"))
                            {
                                user.setSid(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("id"))
                            {
                                user.setuId(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("name"))
                            {
                                user.setUserName(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("avatar_mid"))
                            {
                                user.setAvatarMid(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("avatar_big"))
                            {
                                user.setAvatarBig(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("error_code"))
                            {
                                user.setError_code(xmlParser.nextText());
                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {
                                user.setError_descr(xmlParser.nextText());
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
    }
}
