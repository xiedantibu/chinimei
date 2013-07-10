package com.xlm.meishichina.ui.fragment;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.waps.AdView;

import com.umeng.analytics.MobclickAgent;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.SpreadInfo;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.DisplayImageOptions;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.LoginActivity;
import com.xlm.meishichina.ui.activity.PersonalActivity;
import com.xlm.meishichina.ui.activity.RecipeListActivity;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;

public class LeftMenuFragment extends Fragment implements OnClickListener,
        MeishiConfig
{

    /**
     * mChangeFragment 为了促发改变fragment的接口
     */
    private ChangeFragment mChangeFragment;
    private TextView mTextViewYaoyiyao;
    private TextView mTextViewDelicious;
    private TextView mTextViewMyself;
    private TextView mTextViewCategory;
    private TextView mTextViewRows;
    private TextView mTextViewSetting;
    private ImageView mImageViewSpread;
    private LinearLayout mLinearLayout;
    private SpreadInfo mSpreadInfo = null;
    /**
     * mOptions
     */
    private DisplayImageOptions mOptions;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case MeishiConfig.HANDLE_REQUEST_RECIPE_SPREAD_SUCCESS:

                    mSpreadInfo = (SpreadInfo) msg.obj;
                    if (mSpreadInfo != null)
                    {
                        if (mSpreadInfo.getmSpreadItem() != null
                                && mSpreadInfo.getmSpreadItem().size() > 0)
                        {
                            mImageViewSpread
                                    .setOnClickListener(LeftMenuFragment.this);
                            mImageLoader.displayImage(MeishiApplication
                                    .getApplication().isShowPic() ? mSpreadInfo
                                    .getmSpreadItem().get(0).getCover().trim()
                                    : null, mImageViewSpread, mOptions);
                        }
                    }
                    break;

            }
        };
    };

    public static LeftMenuFragment getInstance(Bundle mBundle)
    {
        LeftMenuFragment leftMenuFragment = new LeftMenuFragment();
        leftMenuFragment.setArguments(mBundle);
        return leftMenuFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        mChangeFragment = (ChangeFragment) getSupportActivity();
        View view = inflater.inflate(R.layout.fragment_main_menu_left, null);
        init(view);
        return view;
    }

    private void init(View v)
    {
        mTextViewYaoyiyao = (TextView) v.findViewById(R.id.menu_yaoyiyao);
        mTextViewRows = (TextView) v.findViewById(R.id.menu_rows);
        mTextViewMyself = (TextView) v.findViewById(R.id.menu_myself);
        mTextViewDelicious = (TextView) v.findViewById(R.id.menu_delicious);
        mTextViewCategory = (TextView) v.findViewById(R.id.menu_category);
        mTextViewSetting = (TextView) v.findViewById(R.id.menu_setting);
        mImageViewSpread = (ImageView) v
                .findViewById(R.id.fragment_main_menu_left_topic_img);
        mLinearLayout = (LinearLayout) v.findViewById(R.id.AdLinearLayout);
        String showAd = MobclickAgent.getConfigParams(getSupportActivity(),
                "showAdhudong");
        if (showAd.equals("true"))
        {

            new AdView(getSupportActivity(), mLinearLayout).DisplayAd();
            mImageViewSpread.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            mImageViewSpread.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
        }
        mTextViewYaoyiyao.setOnClickListener(this);
        mTextViewRows.setOnClickListener(this);
        mTextViewMyself.setOnClickListener(this);
        mTextViewDelicious.setOnClickListener(this);
        mTextViewCategory.setOnClickListener(this);
        mTextViewSetting.setOnClickListener(this);

        mOptions = new DisplayImageOptions.Builder().cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        if (!showAd.equals("true"))
        {
            HttpRequest.getInstance().requestSpread(getSupportActivity(),
                    mHandler, "", "");
        }
    }

    public interface ChangeFragment
    {
        public void switchContent(Fragment index, Bundle mBundle);
    }

    @Override
    public void onClick(View v)
    {

        Bundle mBundle = new Bundle();
        Fragment mFragment = null;
        switch (v.getId())
        {
            case R.id.menu_yaoyiyao:
                mBundle.putString(MeishiConfig.CONFIG_TITLE_FLAG,
                        getString(R.string.left_menu_yaoyiyao));
                mBundle.putInt("fragment", 0);
                mFragment = new YaoyiyaoFragment();
                break;
            case R.id.menu_myself:
                if (MeishiApplication.getApplication().getUserInfo() != null
                        && MeishiApplication.getApplication().getUserInfo()
                                .getuId() != null)
                {

                    Intent intent = new Intent(getSupportActivity(),
                            PersonalActivity.class);
                    intent.putExtra("uid", MeishiApplication.getApplication()
                            .getUserInfo().getuId());
                    intent.putExtra("name", MeishiApplication.getApplication()
                            .getUserInfo().getUserName());
                    intent.putExtra("ismyself", true);
                    startActivity(intent);
                }
                else
                {
                    Toaster.showShort(getSupportActivity(),
                            getString(R.string.login_please_first_logon));
                    Intent intent = new Intent(getSupportActivity(),
                            LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.menu_delicious:
                mBundle.putString(MeishiConfig.CONFIG_TITLE_FLAG,
                        getString(R.string.left_menu_delicious));
                mBundle.putInt("fragment", 1);
                mFragment = new RecipeCollectsFragment();
                break;
            case R.id.menu_category:
                mBundle.putString(MeishiConfig.CONFIG_TITLE_FLAG,
                        getString(R.string.left_menu_category));
                mBundle.putInt("fragment", 2);
                mFragment = new CategoryFragment();
                break;
            case R.id.menu_rows:
                mBundle.putString(MeishiConfig.CONFIG_TITLE_FLAG,
                        getString(R.string.left_menu_rows));
                mBundle.putInt("fragment", 3);
                mFragment = new RecipeRowFragment();
                break;

            case R.id.menu_setting:
                mBundle.putString(MeishiConfig.CONFIG_TITLE_FLAG,
                        getString(R.string.left_menu_setting));
                mBundle.putInt("fragment", 4);
                mFragment = new SettingsFragment();
                break;
            case R.id.fragment_main_menu_left_topic_img:
                if (mSpreadInfo != null)
                {
                    Intent intent = new Intent(getSupportActivity(),
                            RecipeListActivity.class);
                    intent.putExtra("type", 3);
                    intent.putExtra("id", mSpreadInfo.getmSpreadItem().get(0)
                            .getId().trim());
                    intent.putExtra("name", mSpreadInfo.getmSpreadItem().get(0)
                            .getTitle().trim());
                    startActivity(intent);
                }

                break;

        }
        if (mFragment != null)
            mChangeFragment.switchContent(mFragment, mBundle);
    }
}
