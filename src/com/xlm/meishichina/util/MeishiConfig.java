package com.xlm.meishichina.util;

import com.xlm.meishichina.image.core.ImageLoader;

public interface MeishiConfig
{

    /**
     * 设置debug开关
     */
    public static final boolean IS_DEBUG = true;
    /**
     * data/data路径下config文件名称 通过property保存
     */
    public static final String CONFIG = "meishichina";

    /**
     * 缓存失效时间
     */
    public static final int CONFIG_CACHE_TIME = 60 * 60000;
    /**
     * 手机的 app uniqueid
     */
    public final static String CONFIG_UNIQUEID = "uniqueid";

    /**
     * bundle 传递的title key值
     */
    public final static String CONFIG_TITLE_FLAG = "title";

    /**
     * CONFIG_FRAGMENT_DIALOG dialogFragment 传递bundle的key
     */
    public static String CONFIG_FRAGMENT_DIALOG = "dialog";

    // ****************以下为sharedPreference*********************//

    /**
     * CONFIG_SP_LOGIN_NAME sharedPreference中name的键
     */
    public static String CONFIG_SP_LOGIN_NAME = "deskeyofname";

    /**
     * CONFIG_SP_LOGIN_PWD sharedPreference中password的键
     */
    public static String CONFIG_SP_LOGIN_PWD = "deskeyofpassword";

    /**
     * CONFIG_SP_LOGIN_IS_REMEMBER_PWD sharedPreference中isremember的键
     */
    public static String CONFIG_SP_LOGIN_IS_REMEMBER_PWD = "isremember";

    /**
     * CONFIG_SP_UPGRADE sharedPreference中更新
     */
    public static String CONFIG_SP_UPGRADE = "checkupgrade";

    /**
     * CONFIG_SP_UPDATE_MESSAGE sharedPreference中更新日志
     */
    public static String CONFIG_SP_UPDATE_MESSAGE = "update_message";

    /**
     * CONFIG_SP_RECOMMEND_APP sharedPreference中推荐应用
     */
    public static String CONFIG_SP_RECOMMEND_APP = "recommend_app";

    /**
     * CONFIG_SP_FEEDBACK sharedPreference中反馈
     */
    public static String CONFIG_SP_FEEDBACK = "feedback";

    /**
     * CONFIG_SP_UPGRADE sharedPreference中WIFE更新提醒开关
     */
    public static String CONFIG_SP_UPGRADE_SWITCH = "checkupgrade_switch";
    /**
     * CONFIG_SP_UPLOAD_PIC sharedPreference中上传图片质量的键
     */
    public static String CONFIG_SP_UPLOAD_PIC = "uploadpicquality";

    /**
     * CONFIG_SP_DOWN_PIC sharedPreference中是否显示图片
     */
    public static String CONFIG_SP_DOWN_PIC = "download_pictures";

    /**
     * CONFIG_SP_THEME sharedPreference中主题
     */
    public static String CONFIG_SP_THEME = "theme";

    /**
     * CONFIG_SP_LOGIN sharedPreference中登录
     */
    public static String CONFIG_SP_LOGIN = "login";
    /**
     * CONFIG_SP_LOGIN sharedPreference中登录
     */
    public static String CONFIG_SP_LOCAL_CACHE = "deletelocalcache";
    // ****************以上为sharedPreference*********************//

    // ****************以下为图片请求*********************//

    /** 请求相册 */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /** 请求相机 */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
    /** 请求裁剪 */
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
    /** 上传成功 */
    public static final int REQUEST_CODE_UPLOAD_SUCCESS = 3;

    /** 登录 */
    public static final int REQUEST_CODE_LOGING_FROM_SETTING = 4;

    // ****************以下为handler请求*********************//
    public final static int HANDLE_REQUEST_YAOYIYAO_SUCCESS = 0x001;
    public final static int HANDLE_REQUEST_YAOYIYAO_FAIL = 0x002;
    public final static int HANDLE_REQUEST_LOGIN_SUCCESS = 0x003;
    public final static int HANDLE_REQUEST_LOGIN_FAIL = 0x004;
    public final static int HANDLE_REQUEST_FAVCOLLECT_SUCCESS = 0x005;
    public final static int HANDLE_REQUEST_FAVCOLLECT_FAIL = 0x006;
    public final static int HANDLE_REQUEST_RECIPEDETAIL_SUCCESS = 0x007;
    public final static int HANDLE_REQUEST_RECIPEDETAIL_FAIL = 0x008;
    public final static int HANDLE_REQUEST_RECIPECOMMENTLIST_SUCCESS = 0x009;
    public final static int HANDLE_REQUEST_RECIPECOMMENTLIST_FAIL = 0x010;
    public final static int HANDLE_REQUEST_RECIPECOMMENTREPLY_SUCCESS = 0x011;
    public final static int HANDLE_REQUEST_RECIPECOMMENTREPLY_FAIL = 0x012;
    public final static int HANDLE_REQUEST_RECIPEREPORTLIST_SUCCESS = 0x013;
    public final static int HANDLE_REQUEST_RECIPEREPORTLIST_FAIL = 0x014;
    public final static int HANDLE_REQUEST_RECIPEREPUPLOADREPORT_SUCCESS = 0x015;
    public final static int HANDLE_REQUEST_RECIPEREPUPLOADREPORT_FAIL = 0x016;
    public final static int HANDLE_REQUEST_RECIPE_SPREAD_SUCCESS = 0x017;
    public final static int HANDLE_REQUEST_RECIPE_SPREAD_FAIL = 0x018;
    public final static int HANDLE_REQUEST_USER_DETAIL_SUCCESS = 0x019;
    public final static int HANDLE_REQUEST_USER_DETAIL_FAIL = 0x020;
    public final static int HANDLE_REQUEST_FAV_RECIPES_LIST_SUCCESS = 0x021;
    public final static int HANDLE_REQUEST_FAV_RECIPES_LIST_FAIL = 0x022;
    public final static int HANDLE_REQUEST_REPOT_LIST_SUCCESS = 0x023;
    public final static int HANDLE_REQUEST_REPOR_LIST_FAIL = 0x024;
    public final static int HANDLE_REQUEST_DELETE_REPORT_SUCCESS = 0x025;
    public final static int HANDLE_REQUEST_DELETE_REPORT_FAIL = 0x026;
    public final static int HANDLE_REQUEST_REPORT_DETAIL_SUCCESS = 0x026;
    public final static int HANDLE_REQUEST_REPORT_DETAIL_FAIL = 0x027;
    public final static int HANDLE_REQUEST_RECIPE_COLLECT_LIST_SUCCESS = 0x028;
    public final static int HANDLE_REQUEST_RECIPE_COLLECT_LIST_FAIL = 0x029;
    public final static int HANDLE_REQUEST_COLLECT_DATEBASE_SUCCESS = 0x030;
    public final static int HANDLE_REQUEST_COLLECT_DATEBASE_FAIL = 0x031;
    public final static int HANDLE_REQUEST_RECIPE_LIST_SUCCESS = 0x032;
    public final static int HANDLE_REQUEST_RECIPE_LIST_FAIL = 0x033;
    public final static int HANDLE_REQUEST_TYPE_ROW_SUCCESS = 0x034;
    public final static int HANDLE_REQUEST_TYPE_ROW_FAIL = 0x035;
    public final static int HANDLE_REQUEST_REGIST_SUCCESS = 0x036;
    public final static int HANDLE_REQUEST_REGIST_FAIL = 0x037;

    /**
     * mImageLoader 公共mImageLoader
     */
    public ImageLoader mImageLoader = ImageLoader.getInstance();

    /**
     * base url
     */
    public static String URL_BASE = "http://home.meishichina.com/apps/client/sprite/ic.php?";
    /**
     * URL_LOGIN 登录 url
     */
    public static String URL_LOGIN = URL_BASE + "ac=user&op=login&device=";

    /**
     * URL_LOGIN 注册 url
     */
    public static String URL_REGIST = URL_BASE + "ac=user&op=reg&device=%1$s";
    /**
     * URL_YAOYIYAO 摇一摇
     */
    public static String URL_YAOYIYAO = URL_BASE
            + "ac=recipe&op=rand&level=%1$d&cuisine=%2$d&during=%3$d&xingzuo=%4$d";

    /**
     * URL_FAV_RECIPE 收藏菜谱
     */
    public static String URL_FAV_RECIPE = URL_BASE
            + "ac=recipe&op=fav&sid=%1$s&id=%2$s";

    /**
     * URL_DELFAV_RECIPE 取消菜谱收藏
     */
    public static String URL_DELFAV_RECIPE = URL_BASE
            + "ac=recipe&op=delfav&sid=%1$s&id=%2$s";

    /**
     * URL_RECIPE_DETAIL 菜谱详情
     */
    public static String URL_RECIPE_DETAIL = URL_BASE
            + "ac=recipe&op=detail&id=%1$s&sid=%2$s";

    /**
     * URL_RECIPE_COMMENT_LIST 菜谱详情评论
     */
    public static String URL_RECIPE_COMMENT_LIST = URL_BASE
            + "ac=recipe&op=coms&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_RECIPE_COMMENT_REPLY 菜谱详情中的评论
     */
    public static String URL_RECIPE_COMMENT_REPLY = URL_BASE
            + "ac=recipe&op=com&sid=%1$s&id=%2$s";

    /**
     * URL_RECIPE_REPORT_LIST 菜谱详情报告
     */
    public static String URL_RECIPE_REPORT_LIST = URL_BASE
            + "ac=recipe&op=reports&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_RECIPE_UPLOADREPORT 上传作品
     */
    public static String URL_RECIPE_UPLOADREPORT = URL_BASE
            + "ac=recipe&op=report&sid=%1$s&id=%2$s";

    /**
     * URL_SPREAD 美食专题
     */
    public static String URL_SPREAD = URL_BASE + "ac=recipe&op=spread";

    /**
     * URL_USER_DETAIL 个人详情
     */
    public static String URL_USER_DETAIL = URL_BASE + "ac=user&op=info&id=%1$s";

    /**
     * URL_FAV_RECIPE_LIST 个人收藏菜谱列表
     */
    public static String URL_FAV_RECIPE_LIST = URL_BASE
            + "ac=user&op=fav_recipe&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_REPORT_LIST 个人报告列表
     */
    public static String URL_REPORT_LIST = URL_BASE
            + "ac=user&op=reports&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_ORGINAL_RECIPE_LIST 个人原创菜谱列表
     */
    public static String URL_ORGINAL_RECIPE_LIST = URL_BASE
            + "ac=user&op=recipe&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_DELETE_REPORT 删除个人报告
     */
    public static String URL_DELETE_REPORT = URL_BASE
            + "ac=recipe&op=delreport&sid=%1$s&id=%2$s";

    /**
     * URL_REPORT_DETAIL 报告详情
     */
    public static String URL_REPORT_DETAIL = URL_BASE
            + "ac=recipe&op=reportdetail&id=%1$s";

    /**
     * URL_REPORT_COMMENT_LIST 报告评论列表
     */
    public static String URL_REPORT_COMMENT_LIST = URL_BASE
            + "ac=recipe&op=reportcoms&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_REPORT_COMMENT_REPLY 报告中的评论回复
     */
    public static String URL_REPORT_COMMENT_REPLY = URL_BASE
            + "ac=recipe&op=comreport&sid=%1$s";

    /**
     * URL_RECIPE_COLLECTS_LIST 菜单专题
     */
    public static String URL_RECIPE_COLLECTS_LIST = URL_BASE
            + "ac=recipe&op=collects&idx=%1$d&size=%2$d";

    /**
     * URL_RECIEP_SEARCH 搜索
     */
    public static String URL_RECIEP_SEARCH = URL_BASE
            + "ac=search&q=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_RECIEP_CUISINE 菜品口味
     */
    public static String URL_RECIEP_CUISINE = URL_BASE
            + "ac=type&op=list&type=cuisine&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_RECIEP_INGREDIENT 主要食材
     */
    public static String URL_RECIEP_INGREDIENT = URL_BASE
            + "ac=type&op=list&type=ingredient&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_RECIEP_COLLECT 家常菜谱 环球美食 特殊人群 食疗菜谱 菜单专题
     */
    public static String URL_RECIEP_COLLECT = URL_BASE
            + "ac=recipe&op=collect&id=%1$s&idx=%2$d&size=%3$d";

    /**
     * URL_TYPE_ROW 按发表时间 按收藏数 按评论数 按阅读数
     */
    public static String URL_TYPE_ROW = "http://home.meishichina.com/ic2android.php?ac=type&do=all&view=%1$d&page=%2$d";
}
