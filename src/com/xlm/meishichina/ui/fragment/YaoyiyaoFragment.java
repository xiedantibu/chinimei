package com.xlm.meishichina.ui.fragment;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.LinearLayout;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RandomRecipeInfo;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.DisplayImageOptions;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.HistoryActivity;
import com.xlm.meishichina.ui.activity.LoginActivity;
import com.xlm.meishichina.ui.activity.RecipeDetailActivity;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshScrollView;

public class YaoyiyaoFragment extends BaseFragment implements MeishiConfig,
        OnClickListener
{
    /**
     * mImageViewRecipe 菜谱图片
     */
    private ImageView mImageViewRecipe;
    /**
     * mTextViewRecipeName 菜谱名字
     */
    private TextView mTextViewRecipeName;
    /*
     * mTextViewRecipeDescription 菜谱描述
     */
    private TextView mTextViewRecipeDescription;
    /**
     * mOptions
     */
    private DisplayImageOptions mOptions;
    /**
     * mRandomRecipeInfo 摇一摇菜谱类
     */
    private RandomRecipeInfo mRandomRecipeInfo;
    /**
     * mIsCollected 是否收藏 备注：由于接口原因，第一次进来 都将菜谱确定为没有收藏
     */
    private boolean mIsCollected = false;
    /**
     * mDialogIndeterminateFragment dialog
     */
    private DialogsProgressDialogIndeterminateFragment mDialogIndeterminateFragment;

    private PullToRefreshScrollView mPullToRefreshScrollView;

    private ScrollView mScrollView = null;
    private boolean isPullRefresh = false;
    private AlertDialog mDialog = null;
    private RadioGroup mRadioGroup01, mRadioGroup02, mRadioGroup03,
            mRadioGroup04;
    private Handler mHandler = new Handler()
    {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void handleMessage(android.os.Message msg)
        {
            if (mDialogIndeterminateFragment != null)
            {
                mDialogIndeterminateFragment.dismiss();
            }
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case HANDLE_REQUEST_YAOYIYAO_SUCCESS:
                    mRandomRecipeInfo = (RandomRecipeInfo) msg.obj;
                    if (mPullToRefreshScrollView == null)
                    {
                        mViewStub.setLayoutResource(R.layout.fragment_yaoyiyao);
                        mPullToRefreshScrollView = (PullToRefreshScrollView) mViewStub
                                .inflate();
                        mTextViewRecipeDescription = (TextView) mPullToRefreshScrollView
                                .findViewById(R.id.home_recipe_description);
                        mTextViewRecipeName = (TextView) mPullToRefreshScrollView
                                .findViewById(R.id.home_recipe_name);
                        mImageViewRecipe = (ImageView) mPullToRefreshScrollView
                                .findViewById(R.id.home_recipe_image);
                        mPullToRefreshScrollView
                                .setOnRefreshListener(new OnRefreshListener<ScrollView>()
                                {

                                    @Override
                                    public void onRefresh(
                                            PullToRefreshBase<ScrollView> refreshView)
                                    {

                                        isPullRefresh = true;
                                        HttpRequest.getInstance()
                                                .yaoyiyaoRequest(
                                                        getSupportActivity(),
                                                        mHandler, 0, 0, 0, 0);
                                    }
                                });

                        mImageViewRecipe
                                .setOnClickListener(new OnClickListener()
                                {

                                    @Override
                                    public void onClick(View v)
                                    {
                                        if (mRandomRecipeInfo != null)
                                        {

                                            Intent intent = new Intent(
                                                    getSupportActivity(),
                                                    RecipeDetailActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("recipe_id",
                                                    mRandomRecipeInfo.getId());
                                            bundle.putString("recipe_name",
                                                    mRandomRecipeInfo
                                                            .getRecipeTitle()
                                                            .trim());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }
                                });

                    }
                    else
                    {
                        mIsCollected = false;

                        if (Build.VERSION.SDK_INT >= 11)
                        {

                            getSupportActivity().invalidateOptionsMenu();
                        }
                    }

                    if (isPullRefresh)
                    {
                        mPullToRefreshScrollView.onRefreshComplete();
                        isPullRefresh = false;
                    }
                    mPullToRefreshScrollView.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                    mTextViewRecipeName.setText(mRandomRecipeInfo
                            .getRecipeTitle());
                    mTextViewRecipeDescription.setText(mRandomRecipeInfo
                            .getRecipeDescr());
                    mImageLoader.displayImage(MeishiApplication
                            .getApplication().isShowPic() ? mRandomRecipeInfo
                            .getRecipeCover().trim() : null, mImageViewRecipe,
                            mOptions);
                    break;

                case HANDLE_REQUEST_YAOYIYAO_FAIL:
                    mRandomRecipeInfo = null;
                    if (mPullToRefreshScrollView != null)
                    {

                        mPullToRefreshScrollView.setVisibility(View.GONE);
                    }
                    if (isPullRefresh)
                    {
                        mPullToRefreshScrollView.onRefreshComplete();
                        isPullRefresh = false;
                    }
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                    String message = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), message);
                    break;

                case HANDLE_REQUEST_FAVCOLLECT_SUCCESS:
                    mIsCollected = !mIsCollected;
                    if (Build.VERSION.SDK_INT >= 11)
                    {

                        getSupportActivity().invalidateOptionsMenu();
                    }
                    String fav_msg_success = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), fav_msg_success);
                    break;
                case HANDLE_REQUEST_FAVCOLLECT_FAIL:
                    String fav_msg_fail = (String) msg.obj;
                    if (fav_msg_fail.equals(MeishiApplication.getApplication()
                            .getResources().getString(R.string.login_no_user)))
                    {
                        Toaster.showShort(getSupportActivity(),
                                getString(R.string.login_please_first_logon));
                        Intent intent = new Intent(getSupportActivity(),
                                LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {

                        Toaster.showShort(getSupportActivity(), fav_msg_fail);
                    }
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        SubMenu subMenu = menu
                .addSubMenu(getString(R.string.left_menu_yaoyiyao));
        Logmeishi.d("mIsCollected-----onCreateOptionsMenu----" + mIsCollected);
        String value = PreferenceManager.getDefaultSharedPreferences(
                MeishiApplication.getApplication()).getString(CONFIG_SP_THEME,
                "1");
        if (value.equals("1"))
        {
            if (Build.VERSION.SDK_INT >= 11)
            {

                subMenu.add(
                        0,
                        1,
                        1,
                        (mIsCollected) ? getString(R.string.yaoyiyao_menu_collect_no)
                                : getString(R.string.yaoyiyao_menu_collect))
                        .setIcon(
                                (mIsCollected) ? R.drawable.rating_important_light
                                        : R.drawable.rating_not_important_light);
            }
            else
            {
                subMenu.add(0, 1, 1, getString(R.string.yaoyiyao_menu_collect))
                        .setIcon(R.drawable.rating_not_important_light);
            }

            subMenu.add(0, 2, 2, getString(R.string.yaoyiyao_menu_share))
                    .setIcon(R.drawable.social_share_light);

            subMenu.add(0, 3, 3, getString(R.string.yaoyiyao_menu_refresh))
                    .setIcon(R.drawable.navigation_refresh_light);
            subMenu.add(0, 4, 4, getString(R.string.yaoyiyao_menu_history))
                    .setIcon(R.drawable.device_access_data_usage_light);
        }
        else if (value.equals("2"))
        {
            if (Build.VERSION.SDK_INT >= 11)
            {

                subMenu.add(
                        0,
                        1,
                        1,
                        (mIsCollected) ? getString(R.string.yaoyiyao_menu_collect_no)
                                : getString(R.string.yaoyiyao_menu_collect))
                        .setIcon(
                                (mIsCollected) ? R.drawable.rating_important_dark
                                        : R.drawable.rating_not_important_dark);
            }
            else
            {
                subMenu.add(0, 1, 1, getString(R.string.yaoyiyao_menu_collect))
                        .setIcon(R.drawable.rating_not_important_dark);
            }

            subMenu.add(0, 2, 2, getString(R.string.yaoyiyao_menu_share))
                    .setIcon(R.drawable.social_share_dark);

            subMenu.add(0, 3, 3, getString(R.string.yaoyiyao_menu_refresh))
                    .setIcon(R.drawable.navigation_refresh_dark);
            subMenu.add(0, 4, 4, getString(R.string.yaoyiyao_menu_history))
                    .setIcon(R.drawable.device_access_data_usage_dark);
        }
        MenuItem item = subMenu.getItem();
        item.setIcon(R.drawable.base_action_bar_action_more);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        Logmeishi.d("------onPrepareOptionsMenu-----" + mIsCollected);
        String value = PreferenceManager.getDefaultSharedPreferences(
                MeishiApplication.getApplication()).getString(CONFIG_SP_THEME,
                "1");
        if (value.equals("1"))
        {
            if (Build.VERSION.SDK_INT >= 11)
            {
                menu.findItem(1)
                        .setIcon(
                                (mIsCollected) ? R.drawable.rating_important_light
                                        : R.drawable.rating_not_important_light)
                        .setTitle(
                                (mIsCollected) ? getString(R.string.yaoyiyao_menu_collect_no)
                                        : getString(R.string.yaoyiyao_menu_collect));

            }
        }
        else if (value.equals("2"))
        {
            if (Build.VERSION.SDK_INT >= 11)
            {
                menu.findItem(1)
                        .setIcon(
                                (mIsCollected) ? R.drawable.rating_important_dark
                                        : R.drawable.rating_not_important_dark)
                        .setTitle(
                                (mIsCollected) ? getString(R.string.yaoyiyao_menu_collect_no)
                                        : getString(R.string.yaoyiyao_menu_collect));

            }
        }

        // menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 1:
                if (MeishiApplication.getApplication().getUserInfo() != null
                        && MeishiApplication.getApplication().getUserInfo()
                                .getSid() != null)
                {
                    if (mRandomRecipeInfo != null)
                    {
                        mDialogIndeterminateFragment = DialogsProgressDialogIndeterminateFragment
                                .getInstance(getString(R.string.yaoyiyao_fav_collect_deal_data));
                        mDialogIndeterminateFragment
                                .show(getSupportActivity());

                        HttpRequest.getInstance().favOrDelCollectRequest(
                                getSupportActivity(), mHandler,
                                mRandomRecipeInfo.getId(), mIsCollected);

                    }
                    else
                    {
                        Toaster.showShort(
                                getSupportActivity(),
                                getString(R.string.yaoyiyao_fav_collect_no_receipe));
                    }
                }
                else
                {
                    Toaster.showShort(getSupportActivity(),
                            getString(R.string.login_please_first_logon));
                    Intent intent = new Intent(getSupportActivity(),
                            LoginActivity.class);
                    startActivity(intent);
                }
                return true;
            case 2:
                if (mRandomRecipeInfo != null)
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.yaoyiyao_share_from) + "\n"
                                    + mRandomRecipeInfo.getRecipeTitle() + "\n"
                                    + mRandomRecipeInfo.getRecipeDescr() + "\n"
                                    + mRandomRecipeInfo.getRecipeCover());
                    startActivity(Intent.createChooser(intent,
                            getString(R.string.app_name)));
                }

                return true;

            case 3:
                if (mLinearLayoutProgress.getVisibility() != View.VISIBLE)
                {
                    mDialog.show();
                }
                return true;

            case 4:
                Intent intent = new Intent(getSupportActivity(),
                        HistoryActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void createDialog()
    {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(
                getSupportActivity())
                .setTitle(
                        MeishiApplication.getApplication().getString(
                                R.string.select_condition_banner_name))
                .setView(mScrollView)
                .setPositiveButton(
                        MeishiApplication.getApplication().getString(
                                R.string.fragment_setting_yes),
                        new Dialog.OnClickListener()
                        {
                            public void onClick(
                                    DialogInterface dialogInterface, int i)
                            {
                                if (mPullToRefreshScrollView != null)
                                {

                                    mPullToRefreshScrollView
                                            .setVisibility(View.GONE);
                                }
                                int difficulty = 0, flavor = 0, time = 0, constellation = 0;
                                switch (mRadioGroup01.getCheckedRadioButtonId())
                                {
                                    case R.id.select_condition_difficulty01:
                                        difficulty = 0;
                                        break;
                                    case R.id.select_condition_difficulty02:
                                        difficulty = 1;
                                        break;
                                    case R.id.select_condition_difficulty03:
                                        difficulty = 2;
                                        break;
                                    case R.id.select_condition_difficulty04:
                                        difficulty = 3;
                                        break;

                                }
                                switch (mRadioGroup02.getCheckedRadioButtonId())
                                {
                                    case R.id.select_condition_flavor_01:
                                        flavor = 0;
                                        break;
                                    case R.id.select_condition_flavor_02:
                                        flavor = 1;
                                        break;
                                    case R.id.select_condition_flavor_03:
                                        flavor = 2;
                                        break;
                                    case R.id.select_condition_flavor_04:
                                        flavor = 3;
                                        break;

                                }
                                switch (mRadioGroup03.getCheckedRadioButtonId())
                                {
                                    case R.id.select_condition_time_01:
                                        time = 0;
                                        break;
                                    case R.id.select_condition_time_02:
                                        time = 1;
                                        break;
                                    case R.id.select_condition_time_03:
                                        time = 2;
                                        break;
                                    case R.id.select_condition_time_04:
                                        time = 3;
                                        break;

                                }
                                switch (mRadioGroup04.getCheckedRadioButtonId())
                                {
                                    case R.id.select_condition_constellation_01:
                                        constellation = 0;
                                        break;
                                    case R.id.select_condition_constellation_02:
                                        constellation = 1;
                                        break;
                                    case R.id.select_condition_constellation_03:
                                        constellation = 2;
                                        break;
                                    case R.id.select_condition_constellation_04:
                                        constellation = 3;
                                        break;

                                }
                                mImageViewEmpty.setVisibility(View.GONE);
                                mLinearLayoutProgress
                                        .setVisibility(View.VISIBLE);
                                Logmeishi.d("onclick----->" + difficulty + " "
                                        + flavor + " ");
                                HttpRequest.getInstance()
                                        .yaoyiyaoRequest(getSupportActivity(),
                                                mHandler, difficulty, flavor,
                                                time, constellation);
                            }
                        });
        mDialog = mbuilder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_loading_content, null);
        mScrollView = (ScrollView) inflater.inflate(R.layout.select_condition,
                null);
        mRadioGroup01 = (RadioGroup) mScrollView
                .findViewById(R.id.select_condition_difficulty_layout);
        mRadioGroup02 = (RadioGroup) mScrollView
                .findViewById(R.id.select_condition_flavor_layout);
        mRadioGroup03 = (RadioGroup) mScrollView
                .findViewById(R.id.select_condition_time_layout);
        mRadioGroup04 = (RadioGroup) mScrollView
                .findViewById(R.id.select_condition_constellation_layout);

        mLinearLayoutProgress = (LinearLayout) view
                .findViewById(R.id.progressContainer);
        mImageViewEmpty = (ImageView) view.findViewById(R.id.emptys);
        mViewStub = (ViewStub) view.findViewById(R.id.loader_content);
        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.yaoyiyao_pic_background)
                .showImageForEmptyUri(R.drawable.yaoyiyao_pic_background)
                .cacheInMemory().cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        createDialog();
        mImageViewEmpty.setOnClickListener(this);
        HttpRequest.getInstance().yaoyiyaoRequestCache(getSupportActivity(),
                mHandler, 0, 0, 0, 0);
        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.emptys:
                if (MeishiApplication.getApplication().isNetworkConnected())
                {
                    if (mPullToRefreshScrollView != null)
                    {

                        mPullToRefreshScrollView.setVisibility(View.GONE);
                    }
                    mImageViewEmpty.setVisibility(View.GONE);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                    HttpRequest.getInstance().yaoyiyaoRequest(
                            getSupportActivity(), mHandler, 0, 0, 0, 0);
                }
                else
                {

                    Toaster.showShort(getSupportActivity(),
                            getString(R.string.http_request_net_fail));
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

}
