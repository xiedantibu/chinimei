package com.xlm.meishichina.ui.fragment;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.UserDetailInfo;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;

public class PersonalInfoFragment extends Fragment implements MeishiConfig,
        OnClickListener
{

    private ImageView mImageViewAvatar;
    private TextView mTextViewUserName;
    private TextView mTextViewRegistTime;
    private TextView mTextViewFavRecipeNum;
    private TextView mTextViewReportNum;
    private TextView mTextViewOriginalRecipeNum;
    private ScrollView mScrollView;
    /**
     * mLinearLayoutProgress 进度条加载layout
     */
    private LinearLayout mLinearLayoutProgress;
    /**
     * mImageViewEmpty 加载失败的时候现在的empty图片
     */
    private ImageView mImageViewEmpty;

    /**
     * mViewStub 第一次加载临时布局
     */
    private ViewStub mViewStub;

    private String id = "";
    private UserDetailInfo mUserDetailInfo = null;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case MeishiConfig.HANDLE_REQUEST_USER_DETAIL_SUCCESS:
                    if (mScrollView == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.fragment_personal_info);
                        mScrollView = (ScrollView) mViewStub.inflate();

                        mImageViewAvatar = (ImageView) mScrollView
                                .findViewById(R.id.personal_info_avater);
                        mTextViewUserName = (TextView) mScrollView
                                .findViewById(R.id.personal_info_username);
                        mTextViewRegistTime = (TextView) mScrollView
                                .findViewById(R.id.personal_regist_time_info);
                        mTextViewReportNum = (TextView) mScrollView
                                .findViewById(R.id.personal_report_num_number);
                        mTextViewFavRecipeNum = (TextView) mScrollView
                                .findViewById(R.id.personal_info_fav_recipe_num_number);
                        mTextViewOriginalRecipeNum = (TextView) mScrollView
                                .findViewById(R.id.personal_info_original_number);

                        mImageViewAvatar
                                .setOnClickListener(PersonalInfoFragment.this);
                    }
                    mUserDetailInfo = (UserDetailInfo) msg.obj;
                    mScrollView.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                    mImageLoader.displayImage(MeishiApplication
                            .getApplication().isShowPic() ? mUserDetailInfo
                            .getAvatar_big().trim() : null, mImageViewAvatar);
                    mTextViewUserName.setText(mUserDetailInfo.getName().trim());
                    mTextViewRegistTime.setText(mUserDetailInfo.getReg_time()
                            .trim());
                    mTextViewReportNum.setText(mUserDetailInfo.getReport_num()
                            .trim());
                    mTextViewFavRecipeNum.setText(mUserDetailInfo
                            .getFav_recipe_num().trim());
                    mTextViewOriginalRecipeNum.setText(mUserDetailInfo
                            .getRecipe_num().trim());
                    break;

                case MeishiConfig.HANDLE_REQUEST_USER_DETAIL_FAIL:
                    if (mScrollView != null)
                    {
                        mScrollView.setVisibility(View.GONE);
                    }
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                    String fav_msg_fail = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), fav_msg_fail);
                    break;
            }
        };
    };

    public static PersonalInfoFragment getInstance(Bundle bundle)
    {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
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

        View view = inflater.inflate(R.layout.layout_loading_content, null);
        mLinearLayoutProgress = (LinearLayout) view
                .findViewById(R.id.progressContainer);
        mImageViewEmpty = (ImageView) view.findViewById(R.id.emptys);
        mViewStub = (ViewStub) view.findViewById(R.id.loader_content);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            id = (bundle.getString("uid")).trim();
            if (!"".equals(id))
            {
                HttpRequest.getInstance().requestUserDetail(
                        getSupportActivity(), mHandler, id);
                mLinearLayoutProgress.setVisibility(View.VISIBLE);
                mImageViewEmpty.setOnClickListener(this);
            }
            else
            {
                mLinearLayoutProgress.setVisibility(View.GONE);
                mImageViewEmpty.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            mImageViewEmpty.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
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
                if (!"".equals(id))
                {
                    HttpRequest.getInstance().requestUserDetail(
                            getSupportActivity(), mHandler, id);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                    if (mScrollView != null)
                    {
                        mScrollView.setVisibility(View.GONE);
                    }
                }
                else
                {
                    mLinearLayoutProgress.setVisibility(View.GONE);
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                }
                break;

        }
    }
}
