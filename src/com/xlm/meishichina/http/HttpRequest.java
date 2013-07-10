package com.xlm.meishichina.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.CommentInfo;
import com.xlm.meishichina.bean.Entity;
import com.xlm.meishichina.bean.RecipeCusineInfo;
import com.xlm.meishichina.bean.RecipeRowInfo;
import com.xlm.meishichina.bean.RecipesCollectListInfo;
import com.xlm.meishichina.bean.RecipesListInfo;
import com.xlm.meishichina.bean.PersonalReportListInfo;
import com.xlm.meishichina.bean.RandomRecipeInfo;
import com.xlm.meishichina.bean.RecipeDetailInfo;
import com.xlm.meishichina.bean.ReportDetailInfo;
import com.xlm.meishichina.bean.ReportListInfo;
import com.xlm.meishichina.bean.SpreadInfo;
import com.xlm.meishichina.bean.UserDetailInfo;
import com.xlm.meishichina.bean.UserInfo;
import com.xlm.meishichina.image.cache.disc.naming.Md5FileNameGenerator;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.util.MeishiConfig;

public class HttpRequest implements MeishiConfig
{

    public AsyncHttpClient httpClient = new AsyncHttpClient();
    private static HttpRequest mRequest = null;

    private HttpRequest()
    {

    }

    /**
     * 
     * @Title: loginRequest
     * @Description: 登录请求
     * @param @param context
     * @param @param mHandler
     * @param @param username
     * @param @param password
     * @return void
     */
    public void loginRequest(final Context context, final Handler mHandler,
            String username, String password)
    {
        String url = URL_LOGIN
                + URLEncoder.encode(MeishiApplication.getApplication()
                        .getTelphoneModels());
        Logmeishi.d("------url-----------" + url);

        if (!isNetworkConnected(context, mHandler, HANDLE_REQUEST_LOGIN_FAIL))
        {
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", username);
        requestParams.put("password", password);
        httpClient.post(context, url.trim(), requestParams,
                new AsyncHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(String content)
                    {
                        super.onSuccess(content);
                        Logmeishi.d("login", "login--------->" + content);
                        try
                        {
                            UserInfo.praseOfLogin(new ByteArrayInputStream(
                                    content.getBytes()), MeishiApplication
                                    .getApplication().getUserInfo());
                            if (MeishiApplication.getApplication()
                                    .getUserInfo() != null
                                    && MeishiApplication.getApplication()
                                            .getUserInfo().getSid() != null
                                    && MeishiApplication.getApplication()
                                            .getUserInfo().getError_code()
                                            .equalsIgnoreCase("1"))
                            {
                                mHandler.sendEmptyMessage(HANDLE_REQUEST_LOGIN_SUCCESS);
                            }
                            else
                            {
                                Message msg = new Message();
                                if (MeishiApplication.getApplication()
                                        .getUserInfo() != null
                                        && MeishiApplication.getApplication()
                                                .getUserInfo().getSid() != null)
                                {

                                    msg.obj = MeishiApplication
                                            .getApplication().getUserInfo()
                                            .getError_descr();
                                }
                                else
                                {
                                    msg.obj = context
                                            .getString(R.string.http_request_data_fail);
                                }
                                msg.what = HANDLE_REQUEST_LOGIN_FAIL;
                                mHandler.sendMessage(msg);
                            }

                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable error)
                    {
                        Message msg = new Message();
                        msg.obj = context.getResources().getString(
                                R.string.http_request_data_fail);
                        msg.what = HANDLE_REQUEST_LOGIN_FAIL;
                        mHandler.sendMessage(msg);
                    }
                });

    }

    public void registRequest(final Context context, final Handler mHandler,
            String username, String password, String mail)
    {
        String url = String
                .format(URL_REGIST, URLEncoder.encode(MeishiApplication
                        .getApplication().getTelphoneModels()));
        Logmeishi.d("------url------" + url);
        if (!isNetworkConnected(context, mHandler, HANDLE_REQUEST_REGIST_FAIL))
        {
            return;
        }
        RequestParams localHashMap = new RequestParams();
        localHashMap.put("username", username.trim());
        localHashMap.put("password", password.trim());
        localHashMap.put("email", mail.trim());
        localHashMap.put("befrom", "android_sprite");
        httpClient.post(url, localHashMap, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                Logmeishi.d("registRequest-----content--->" + content);

                try
                {
                    MeishiApplication.getApplication().setUser(null);
                    UserInfo.praseOfLogin(
                            new ByteArrayInputStream(content.getBytes()),
                            MeishiApplication.getApplication().getUserInfo());
                    if (MeishiApplication.getApplication().getUserInfo() != null
                            && MeishiApplication.getApplication().getUserInfo()
                                    .getSid() != null
                            && MeishiApplication.getApplication().getUserInfo()
                                    .getError_code().equalsIgnoreCase("1"))
                    {
                        mHandler.sendEmptyMessage(HANDLE_REQUEST_REGIST_SUCCESS);
                    }
                    else
                    {
                        Message msg = new Message();
                        if (MeishiApplication.getApplication().getUserInfo() != null
                                && MeishiApplication.getApplication()
                                        .getUserInfo().getSid() != null)
                        {

                            msg.obj = MeishiApplication.getApplication()
                                    .getUserInfo().getError_descr();
                        }
                        else
                        {
                            msg.obj = context
                                    .getString(R.string.http_request_data_fail);
                        }
                        msg.what = HANDLE_REQUEST_REGIST_FAIL;
                        mHandler.sendMessage(msg);
                    }

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = context.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_REGIST_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void yaoyiyaoRequest(final Context context, final Handler mHandler,
            int level, int cuisine, int during, int xingzuo)
    {
        final String url = String.format(URL_YAOYIYAO, level, cuisine, during,
                xingzuo);
        Logmeishi.d("url---------->" + url);
        if (!isNetworkConnected(context, mHandler, HANDLE_REQUEST_YAOYIYAO_FAIL))
        {
            return;
        }
        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {

                try
                {
                    Logmeishi.d("----------content-------" + content);
                    RandomRecipeInfo randomRecipeInfo = RandomRecipeInfo
                            .pares(new ByteArrayInputStream(content.getBytes()));
                    if (randomRecipeInfo != null
                            && randomRecipeInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                    {

                        MeishiApplication.getApplication().getFinalDb()
                                .save(randomRecipeInfo);
                        String cacheKey = new Md5FileNameGenerator()
                                .generate(url);
                        MeishiApplication.getApplication().saveObject(content,
                                cacheKey);
                        Message msg = new Message();
                        msg.obj = randomRecipeInfo;
                        msg.what = HANDLE_REQUEST_YAOYIYAO_SUCCESS;
                        mHandler.sendMessage(msg);
                    }
                    else
                    {
                        Message msg = new Message();
                        if (randomRecipeInfo != null)
                        {

                            msg.obj = randomRecipeInfo.getError_descr();
                        }
                        else
                        {
                            msg.obj = context
                                    .getString(R.string.http_request_data_fail);
                        }
                        msg.what = HANDLE_REQUEST_YAOYIYAO_FAIL;
                        mHandler.sendMessage(msg);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {

                Message msg = new Message();
                msg.obj = context.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_YAOYIYAO_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 
     * @Title: yaoyiyaoRequestCache
     * @Description: 默认第一次登录请求
     * @param @param context
     * @param @param mHandler
     * @param @param level
     * @param @param cuisine
     * @param @param during
     * @param @param xingzuo
     * @return void
     */
    public void yaoyiyaoRequestCache(final Context context,
            final Handler mHandler, int level, int cuisine, int during,
            int xingzuo)
    {
        final String url = String.format(URL_YAOYIYAO, level, cuisine, during,
                xingzuo);
        String cacheKey = new Md5FileNameGenerator().generate(url);
        Logmeishi.d("url-------cache--->" + url + " cacheKey----" + cacheKey);
        String content = (String) MeishiApplication.getApplication()
                .readObject(cacheKey);
        if (content == null)
        {
            yaoyiyaoRequest(context, mHandler, level, cuisine, during, xingzuo);
        }
        else
        {
            RandomRecipeInfo randomRecipeInfo;
            try
            {
                randomRecipeInfo = RandomRecipeInfo
                        .pares(new ByteArrayInputStream(content.getBytes()));
                if (randomRecipeInfo != null
                        && randomRecipeInfo.getError_code().equalsIgnoreCase(
                                "1"))
                {
                    Logmeishi.d("cache===============");
                    Message msg = new Message();
                    msg.obj = randomRecipeInfo;
                    msg.what = HANDLE_REQUEST_YAOYIYAO_SUCCESS;
                    mHandler.sendMessage(msg);
                }
                else
                {
                    Message msg = new Message();
                    if (randomRecipeInfo != null)
                    {

                        msg.obj = randomRecipeInfo.getError_descr();
                    }
                    else
                    {
                        msg.obj = context
                                .getString(R.string.http_request_data_fail);
                    }
                    msg.what = HANDLE_REQUEST_YAOYIYAO_FAIL;
                    mHandler.sendMessage(msg);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void favOrDelCollectRequest(final Context mContext,
            final Handler mHandler, String id, boolean flag)
    {
        String url = String.format((flag) ? URL_DELFAV_RECIPE : URL_FAV_RECIPE,
                MeishiApplication.getApplication().getUserInfo().getSid(), id)
                .trim();
        Logmeishi.d("-----url----" + url);
        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_FAVCOLLECT_FAIL))
        {
            return;
        }
        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                Logmeishi.d("content-------->" + content);
                try
                {
                    Entity entity = Entity
                            .parseEntity(new ByteArrayInputStream(content
                                    .getBytes()));
                    Message msg = new Message();
                    if (entity != null)
                    {
                        if (entity != null
                                && entity.getError_code().equalsIgnoreCase("1"))
                        {
                            msg.what = HANDLE_REQUEST_FAVCOLLECT_SUCCESS;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_FAVCOLLECT_FAIL;
                        }
                        msg.obj = entity.getError_descr();
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_FAVCOLLECT_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {

                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_FAVCOLLECT_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void recipeDetailRequest(final Context mContext,
            final Handler mHandler, String id)
    {
        String sid = "";
        String url = "";
        if (MeishiApplication.getApplication().getUserInfo() != null
                && MeishiApplication.getApplication().getUserInfo().getSid() != null)
        {
            sid = MeishiApplication.getApplication().getUserInfo().getSid()
                    .trim();
        }
        url = String.format(URL_RECIPE_DETAIL, id, sid);
        Logmeishi.d("------url------------" + url);

        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_RECIPEDETAIL_FAIL))
        {
            return;
        }
        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                Logmeishi.d("------content-----" + content);
                try
                {
                    RecipeDetailInfo mRecipeDetailInfo = RecipeDetailInfo
                            .prase(new ByteArrayInputStream(content.getBytes()));
                    Message msg = new Message();
                    if (mRecipeDetailInfo != null)
                    {

                        if (mRecipeDetailInfo.getError_code().equalsIgnoreCase(
                                "1"))
                        {
                            msg.what = HANDLE_REQUEST_RECIPEDETAIL_SUCCESS;
                            msg.obj = mRecipeDetailInfo;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_RECIPEDETAIL_FAIL;
                            msg.obj = mRecipeDetailInfo.getError_descr();
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_RECIPEDETAIL_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }
                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_RECIPEDETAIL_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void recipeCommentListRequest(final Context mContext,
            final Handler mHandler, String id, int idx, int pageSize,
            boolean from)
    {
        String url = String.format(((from) ? URL_RECIPE_COMMENT_LIST
                : URL_REPORT_COMMENT_LIST), id.trim(), idx, pageSize);
        Logmeishi.d("-----url------" + url);

        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_RECIPECOMMENTLIST_FAIL))
        {
            return;
        }

        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                Logmeishi.d("content----------->" + content);
                try
                {
                    CommentInfo mCommentInfo = CommentInfo
                            .parse(new ByteArrayInputStream(content.getBytes()));
                    Message msg = new Message();
                    if (mCommentInfo != null)
                    {
                        if (mCommentInfo.getError_code().equalsIgnoreCase("1"))
                        {
                            msg.obj = mCommentInfo;
                            msg.what = HANDLE_REQUEST_RECIPECOMMENTLIST_SUCCESS;
                        }
                        else
                        {
                            msg.obj = mCommentInfo.getError_descr();
                            msg.what = HANDLE_REQUEST_RECIPECOMMENTLIST_FAIL;
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_RECIPECOMMENTLIST_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_RECIPECOMMENTLIST_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void recipeCommentReplyRequest(final Context mContext,
            final Handler mHandler, String id, String messageTxt, boolean from)
    {

        String url = "";
        if (from)
        {

            url = String.format(URL_RECIPE_COMMENT_REPLY, MeishiApplication
                    .getApplication().getUserInfo().getSid().trim(), id.trim());
        }
        else
        {
            url = String.format(URL_REPORT_COMMENT_REPLY, MeishiApplication
                    .getApplication().getUserInfo().getSid().trim());
        }
        Logmeishi.d("========url======" + url);

        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_RECIPECOMMENTREPLY_FAIL))
        {
            return;
        }

        RequestParams localRequestParams = new RequestParams();
        localRequestParams.put("befrom", "android_sprite");
        localRequestParams.put("message", messageTxt.trim());
        if (!from)
        {
            localRequestParams.put("id", id.trim());
        }
        httpClient.post(url, localRequestParams, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                super.onSuccess(content);
                Logmeishi.d("----content-------" + content);

                try
                {
                    Message msg = new Message();
                    Entity entity = Entity
                            .parseEntity(new ByteArrayInputStream(content
                                    .getBytes()));
                    if (entity != null)
                    {
                        if (entity.getError_code().equalsIgnoreCase("1"))
                        {
                            msg.what = HANDLE_REQUEST_RECIPECOMMENTREPLY_SUCCESS;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_RECIPECOMMENTREPLY_FAIL;
                            msg.obj = entity.getError_descr();
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_RECIPECOMMENTREPLY_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_RECIPECOMMENTREPLY_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    {

    }

    public void recipeReportsRequest(final Context mContext,
            final Handler mHandler, String id, int idx, int pageSize)
    {
        String url = String.format(URL_RECIPE_REPORT_LIST, id.trim(), idx,
                pageSize);
        Logmeishi.d("-----url------" + url);

        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_RECIPEREPORTLIST_FAIL))
        {
            return;
        }
        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                Logmeishi.d("-----content-----" + content);
                try
                {
                    Message msg = new Message();
                    ReportListInfo mReportListInfo = ReportListInfo
                            .parse(new ByteArrayInputStream(content.getBytes()));
                    if (mReportListInfo != null)
                    {
                        if (mReportListInfo.getError_code().equalsIgnoreCase(
                                "1"))
                        {
                            msg.what = HANDLE_REQUEST_RECIPEREPORTLIST_SUCCESS;
                            msg.obj = mReportListInfo;
                            Logmeishi.d("--recipeReportsRequest--");
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_RECIPEREPORTLIST_FAIL;
                            msg.obj = mReportListInfo.getError_descr();
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_RECIPEREPORTLIST_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_RECIPEREPORTLIST_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void recipeRequestUploadReport(final Context mContext,
            final Handler mHandler, String id, String msg, String uploadPath)
    {
        try
        {
            String url = "";
            url = String.format(URL_RECIPE_UPLOADREPORT, MeishiApplication
                    .getApplication().getUserInfo().getSid().trim(), id.trim());
            Logmeishi.d("========url======" + url);

            if (!isNetworkConnected(mContext, mHandler,
                    HANDLE_REQUEST_RECIPEREPUPLOADREPORT_FAIL))
            {
                return;
            }

            RequestParams localRequestParams = new RequestParams();
            localRequestParams.put("befrom", "android_sprite");
            localRequestParams.put("message", msg.trim());
            localRequestParams.put("file", new File(uploadPath));
            Logmeishi.d("path", uploadPath);
            httpClient.post(url, localRequestParams,
                    new AsyncHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(String content)
                        {
                            super.onSuccess(content);
                            Logmeishi.d("=======content============" + content);

                            try
                            {
                                Message msg = new Message();
                                Entity entity;
                                entity = Entity
                                        .parseEntity(new ByteArrayInputStream(
                                                content.getBytes()));
                                if (entity != null)
                                {
                                    if (entity.getError_code()
                                            .equalsIgnoreCase("1"))
                                    {
                                        msg.what = HANDLE_REQUEST_RECIPEREPUPLOADREPORT_SUCCESS;
                                    }
                                    else
                                    {
                                        msg.what = HANDLE_REQUEST_RECIPEREPUPLOADREPORT_FAIL;
                                        msg.obj = entity.getError_descr();
                                    }
                                }
                                else
                                {
                                    msg.what = HANDLE_REQUEST_RECIPEREPUPLOADREPORT_FAIL;
                                    msg.obj = mContext
                                            .getString(R.string.http_request_data_xml_fail);
                                }

                                mHandler.sendMessage(msg);
                            }
                            catch (IOException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Throwable error)
                        {
                            Message msg = new Message();
                            msg.obj = mContext.getResources().getString(
                                    R.string.http_request_data_fail);
                            msg.what = HANDLE_REQUEST_RECIPEREPUPLOADREPORT_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    });
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void requestSpread(final Context mContext, final Handler mHandler,
            String idx, String size)
    {
        String url = "";
        url = URL_SPREAD;

        if (!MeishiApplication.getApplication().isNetworkConnected())
        {
            return;
        }

        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                Logmeishi.d("-------content-------" + content);
                try
                {
                    Message msg = new Message();
                    SpreadInfo mInfo;
                    mInfo = SpreadInfo.parse(new ByteArrayInputStream(content
                            .getBytes()));
                    if (mInfo != null)
                    {
                        if (mInfo.getError_code().equalsIgnoreCase("1"))
                        {
                            msg.what = HANDLE_REQUEST_RECIPE_SPREAD_SUCCESS;
                            msg.obj = mInfo;
                        }
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {

            }
        });
    }

    public void requestUserDetail(final Context mContext,
            final Handler mHandler, String id)
    {

        String url = "";
        url = String.format(URL_USER_DETAIL, id.trim());

        Logmeishi.d("------url------" + url);
        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_USER_DETAIL_FAIL))
        {
            return;
        }

        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {

                Logmeishi.d("=====requestUserDetail====content=========="
                        + content);

                try
                {
                    Message msg = new Message();
                    UserDetailInfo mUserDetailInfo = UserDetailInfo
                            .parse(new ByteArrayInputStream(content.getBytes()));
                    if (mUserDetailInfo != null)
                    {
                        if (mUserDetailInfo.getError_code().equalsIgnoreCase(
                                "1"))
                        {
                            msg.what = HANDLE_REQUEST_USER_DETAIL_SUCCESS;
                            msg.obj = mUserDetailInfo;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_USER_DETAIL_FAIL;
                            msg.obj = mUserDetailInfo.getError_descr();
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_USER_DETAIL_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_USER_DETAIL_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void requestFavRecipesList(final Context mContext,
            final Handler mHandler, String id, int idx, int size, boolean isFav)
    {

        String url = "";
        url = String.format(((isFav) ? URL_FAV_RECIPE_LIST
                : URL_ORGINAL_RECIPE_LIST), id.trim(), idx, size);
        Logmeishi.d("------url--------" + url);

        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_FAV_RECIPES_LIST_FAIL))
        {
            return;
        }

        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                super.onSuccess(content);
                Logmeishi.d("==requestFavRecipesList===content======="
                        + content);

                try
                {
                    Message msg = new Message();
                    RecipesListInfo mPersonalFavRecipesList = RecipesListInfo
                            .parse(new ByteArrayInputStream(content.getBytes()));
                    if (mPersonalFavRecipesList != null)
                    {
                        if (mPersonalFavRecipesList.getError_code()
                                .equalsIgnoreCase("1"))
                        {
                            msg.what = HANDLE_REQUEST_FAV_RECIPES_LIST_SUCCESS;
                            msg.obj = mPersonalFavRecipesList;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_FAV_RECIPES_LIST_FAIL;
                            msg.obj = mPersonalFavRecipesList.getError_descr();
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_FAV_RECIPES_LIST_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_FAV_RECIPES_LIST_FAIL;
                mHandler.sendMessage(msg);

            }
        });
    }

    public void requestReportList(final Context mContext,
            final Handler mHandler, String id, int idx, int size)
    {

        String url = "";
        url = String.format(URL_REPORT_LIST, id.trim(), idx, size);
        Logmeishi.d("------url--------" + url);

        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_REPOR_LIST_FAIL))
        {
            return;
        }

        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                super.onSuccess(content);
                Logmeishi.d("==requestReportList===content=======" + content);

                try
                {
                    Message msg = new Message();
                    PersonalReportListInfo mPersonalReportListInfo = PersonalReportListInfo
                            .parse(new ByteArrayInputStream(content.getBytes()));
                    if (mPersonalReportListInfo != null)
                    {
                        if (mPersonalReportListInfo.getError_code()
                                .equalsIgnoreCase("1"))
                        {
                            msg.what = HANDLE_REQUEST_REPOT_LIST_SUCCESS;
                            msg.obj = mPersonalReportListInfo;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_REPOR_LIST_FAIL;
                            msg.obj = mPersonalReportListInfo.getError_descr();
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_REPOR_LIST_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_REPOR_LIST_FAIL;
                mHandler.sendMessage(msg);

            }
        });
    }

    public void requestDeleteReport(final Context mContext,
            final Handler mHandler, String id)
    {
        String url = "";
        url = String.format(URL_DELETE_REPORT, MeishiApplication
                .getApplication().getUserInfo().getSid().trim(), id.trim());
        Logmeishi.d("========url======" + url);

        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_DELETE_REPORT_FAIL))
        {
            return;
        }

        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                Logmeishi.d("requestDeleteReport--------content-------->"
                        + content);
                try
                {
                    Entity entity = Entity
                            .parseEntity(new ByteArrayInputStream(content
                                    .getBytes()));
                    Message msg = new Message();
                    if (entity != null)
                    {
                        if (entity != null
                                && entity.getError_code().equalsIgnoreCase("1"))
                        {
                            msg.what = HANDLE_REQUEST_DELETE_REPORT_SUCCESS;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_DELETE_REPORT_FAIL;
                        }
                        msg.obj = entity.getError_descr();
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_DELETE_REPORT_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {

                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_DELETE_REPORT_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void requestReportDetail(final Context mContext,
            final Handler mHandler, String id)
    {

        String url = "";
        url = String.format(URL_REPORT_DETAIL, id.trim());

        Logmeishi.d("========url======" + url);

        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_REPORT_DETAIL_FAIL))
        {
            return;
        }

        httpClient.get(url, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {
                Logmeishi.d("requestReportDetail----------content===="
                        + content);

                try
                {
                    Message msg = new Message();
                    ReportDetailInfo mReportDetailInfo = ReportDetailInfo
                            .prase(new ByteArrayInputStream(content.getBytes()));

                    if (mReportDetailInfo != null)
                    {
                        if (mReportDetailInfo.getError_code().equalsIgnoreCase(
                                "1"))
                        {
                            msg.what = HANDLE_REQUEST_REPORT_DETAIL_SUCCESS;
                            msg.obj = mReportDetailInfo;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_REPORT_DETAIL_FAIL;
                            msg.obj = mReportDetailInfo.getError_descr();
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_REPORT_DETAIL_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {

                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_REPORT_DETAIL_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void requestRecipeCollect(final Context mContext,
            final Handler mHandler, final int idx, int size,
            boolean isCahceRequest)
    {
        final String url = String.format(URL_RECIPE_COLLECTS_LIST, idx, size);
        String cacheKey = new Md5FileNameGenerator().generate(url);
        Logmeishi.d("url-------cache--->" + url + " cacheKey----" + cacheKey);
        String content = (String) MeishiApplication.getApplication()
                .readObject(cacheKey);
        if (content != null && isCahceRequest)
        {
            Message msg = new Message();
            try
            {
                RecipesCollectListInfo mRecipesCollectListInfo = RecipesCollectListInfo
                        .parse(new ByteArrayInputStream(content.getBytes()));
                msg.what = HANDLE_REQUEST_RECIPE_COLLECT_LIST_SUCCESS;
                msg.obj = mRecipesCollectListInfo;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            mHandler.sendMessage(msg);
        }
        else
        {
            if (!isNetworkConnected(mContext, mHandler,
                    HANDLE_REQUEST_RECIPE_COLLECT_LIST_FAIL))
            {
                return;
            }

            httpClient.get(url, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(String content)
                {
                    Logmeishi.d("requestRecipeCollect----------content===="
                            + content);

                    try
                    {
                        Message msg = new Message();
                        RecipesCollectListInfo mRecipesCollectListInfo = RecipesCollectListInfo
                                .parse(new ByteArrayInputStream(content
                                        .getBytes()));

                        if (mRecipesCollectListInfo != null)
                        {
                            if (mRecipesCollectListInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {
                                if (idx == 1)
                                {
                                    String cacheKey = new Md5FileNameGenerator()
                                            .generate(url);
                                    MeishiApplication.getApplication()
                                            .saveObject(content, cacheKey);
                                }
                                msg.what = HANDLE_REQUEST_RECIPE_COLLECT_LIST_SUCCESS;
                                msg.obj = mRecipesCollectListInfo;
                            }
                            else
                            {
                                msg.what = HANDLE_REQUEST_RECIPE_COLLECT_LIST_FAIL;
                                msg.obj = mRecipesCollectListInfo
                                        .getError_descr();
                            }
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_RECIPE_COLLECT_LIST_FAIL;
                            msg.obj = mContext
                                    .getString(R.string.http_request_data_xml_fail);
                        }

                        mHandler.sendMessage(msg);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable error)
                {
                    Message msg = new Message();
                    msg.obj = mContext.getResources().getString(
                            R.string.http_request_data_fail);
                    msg.what = HANDLE_REQUEST_RECIPE_COLLECT_LIST_FAIL;
                    mHandler.sendMessage(msg);
                }
            });
        }

    }

    public void requestRecipeList(final Context mContext,
            final Handler mHandler, int type, String id, int idx, int size)
    {
        String finalUrl = "";
        String url = "";
        switch (type)
        {
            case 1:
                url = URL_RECIEP_INGREDIENT;
                break;

            case 2:
                url = URL_RECIEP_CUISINE;
                break;
            case 3:
                url = URL_RECIEP_COLLECT;
                break;
            case 4:
                url = URL_RECIEP_SEARCH;
                try
                {
                    id = URLEncoder.encode(id, "utf-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                break;
        }

        finalUrl = String.format(url, id, idx, size);
        Logmeishi.d("----------finalUrl-----" + finalUrl);
        if (!isNetworkConnected(mContext, mHandler,
                HANDLE_REQUEST_RECIPE_LIST_FAIL))
        {
            return;
        }
        httpClient.get(finalUrl, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String content)
            {

                Logmeishi.d("-----requestRecipeList--content--" + content);

                try
                {
                    Message msg = new Message();
                    RecipeCusineInfo mRecipeCusineInfo = RecipeCusineInfo
                            .parse(new ByteArrayInputStream(content.getBytes()));

                    if (mRecipeCusineInfo != null)
                    {
                        if (mRecipeCusineInfo.getError_code().equalsIgnoreCase(
                                "1"))
                        {
                            msg.what = HANDLE_REQUEST_RECIPE_LIST_SUCCESS;
                            msg.obj = mRecipeCusineInfo;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_RECIPE_LIST_FAIL;
                            msg.obj = mContext.getResources().getString(
                                    R.string.http_request_data_fail);
                        }
                    }
                    else
                    {
                        msg.what = HANDLE_REQUEST_RECIPE_LIST_FAIL;
                        msg.obj = mContext
                                .getString(R.string.http_request_data_xml_fail);
                    }

                    mHandler.sendMessage(msg);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error)
            {
                Message msg = new Message();
                msg.obj = mContext.getResources().getString(
                        R.string.http_request_data_fail);
                msg.what = HANDLE_REQUEST_RECIPE_LIST_FAIL;
                mHandler.sendMessage(msg);
            }
        });
    }

    public void requestRow(final Context mContext, final Handler mHandler,
            final int type, final int page, boolean isCahceRequest)
    {
        final String url = String.format(URL_TYPE_ROW, type, page);
        String cacheKey = new Md5FileNameGenerator().generate(url);
        Logmeishi.d("url-------cache--->" + url + " cacheKey----" + cacheKey);
        String content = (String) MeishiApplication.getApplication()
                .readObject(cacheKey);
        Logmeishi.d("content---------isCahceRequest---1---");
        if (content != null && isCahceRequest)
        {
            Message msg = new Message();
            try
            {
                Logmeishi.d("content---------isCahceRequest--2----"
                        + isCahceRequest);
                RecipeRowInfo mRecipeRowInfo = RecipeRowInfo
                        .parse(new ByteArrayInputStream(content.getBytes()));
                msg.what = HANDLE_REQUEST_TYPE_ROW_SUCCESS;
                msg.obj = mRecipeRowInfo;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            mHandler.sendMessage(msg);
        }
        else
        {
            if (!isNetworkConnected(mContext, mHandler,
                    HANDLE_REQUEST_TYPE_ROW_FAIL))
            {
                return;
            }
            httpClient.get(url, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(String content)
                {
                    Logmeishi.d("-----requestRow----content---" + content);
                    try
                    {
                        Message msg = new Message();
                        RecipeRowInfo mRecipeRowInfo = RecipeRowInfo
                                .parse(new ByteArrayInputStream(content
                                        .getBytes()));

                        if (mRecipeRowInfo != null)
                        {
                            Logmeishi.d("content---------page---1---");
                            if (type == 1 && page == 1)
                            {
                                Logmeishi.d("content---------page--2----");
                                String cacheKey = new Md5FileNameGenerator()
                                        .generate(url);
                                MeishiApplication.getApplication().saveObject(
                                        content, cacheKey);
                            }
                            msg.what = HANDLE_REQUEST_TYPE_ROW_SUCCESS;
                            msg.obj = mRecipeRowInfo;
                        }
                        else
                        {
                            msg.what = HANDLE_REQUEST_TYPE_ROW_FAIL;
                            msg.obj = mContext
                                    .getString(R.string.http_request_data_xml_fail);
                        }

                        mHandler.sendMessage(msg);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable error)
                {
                    Message msg = new Message();
                    msg.obj = mContext.getResources().getString(
                            R.string.http_request_data_fail);
                    msg.what = HANDLE_REQUEST_TYPE_ROW_FAIL;
                    mHandler.sendMessage(msg);
                }
            });
        }

    }

    private boolean isNetworkConnected(Context mContext, Handler mHandler,
            int msgOfWhat)
    {
        if (!MeishiApplication.getApplication().isNetworkConnected())
        {
            Message msg = new Message();
            msg.obj = mContext.getResources().getString(
                    R.string.http_request_net_fail);
            msg.what = msgOfWhat;
            mHandler.sendMessage(msg);
            return false;
        }
        else
        {
            return true;
        }
    }

    public static HttpRequest getInstance()
    {
        if (null == mRequest)
        {
            mRequest = new HttpRequest();
        }
        return mRequest;
    }

}
