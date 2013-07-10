package com.xlm.meishichina.ui.fragment;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.ReportDetailInfo;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.PersonalActivity;
import com.xlm.meishichina.ui.activity.RecipeDetailActivity;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ReportDetailFragment extends Fragment implements MeishiConfig,
        OnClickListener
{
    private ImageView mImageViewCover;
    private ImageView mImageViewUserCover;
    private TextView mTextViewUserName;
    private TextView mTextViewTime;
    private TextView mTextViewDesc;
    private LinearLayout mLinearLayout;
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
    private String name = "";

    private ReportDetailInfo mReportDetailInfo = null;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);

            switch (msg.what)
            {
                case HANDLE_REQUEST_REPORT_DETAIL_SUCCESS:
                    if (mLinearLayout == null)
                    {

                        mViewStub
                                .setLayoutResource(R.layout.fragment_report_detail_item);
                        mLinearLayout = (LinearLayout) mViewStub.inflate();

                        mImageViewCover = (ImageView) mLinearLayout
                                .findViewById(R.id.fragment_report_detail_item_imageview);
                        mImageViewUserCover = (ImageView) mLinearLayout
                                .findViewById(R.id.frament_report_detail_item_userCover);
                        mTextViewUserName = (TextView) mLinearLayout
                                .findViewById(R.id.frament_report_detail_item_userName);
                        mTextViewTime = (TextView) mLinearLayout
                                .findViewById(R.id.frament_report_detail_item_time);
                        mTextViewDesc = (TextView) mLinearLayout
                                .findViewById(R.id.frament_report_detail_item_desc);

                        mImageViewCover
                                .setOnClickListener(ReportDetailFragment.this);
                        mImageViewUserCover
                                .setOnClickListener(ReportDetailFragment.this);

                    }

                    mLinearLayout.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                    mReportDetailInfo = (ReportDetailInfo) msg.obj;

                    mImageLoader.displayImage(mReportDetailInfo.getAvator()
                            .trim(), mImageViewUserCover);
                    mImageLoader.displayImage(
                            mReportDetailInfo.getPic().trim(), mImageViewCover);
                    mTextViewUserName.setText(mReportDetailInfo.getAuthor()
                            .trim());
                    mTextViewTime.setText(mReportDetailInfo.getTime().trim());
                    mTextViewDesc.setText(mReportDetailInfo.getDescr()
                            .replace("<br />", "").replace("&nbsp;", ""));
                    break;

                case HANDLE_REQUEST_REPORT_DETAIL_FAIL:

                    if (mLinearLayout != null)
                    {
                        mLinearLayout.setVisibility(View.GONE);
                    }
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                    String fav_msg_fail = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), fav_msg_fail);
                    break;
            }
        };
    };

    public static ReportDetailFragment getInstance(Bundle bundle)
    {
        ReportDetailFragment fragment = new ReportDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
            id = (bundle.getString("id")).trim();
            name = (bundle.getString("name")).trim();
            if (!"".equals(id))
            {
                HttpRequest.getInstance().requestReportDetail(
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
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.emptys:
                if (!"".equals(id))
                {
                    HttpRequest.getInstance().requestReportDetail(
                            getSupportActivity(), mHandler, id);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                    if (mLinearLayout != null)
                        mLinearLayout.setVisibility(View.GONE);
                }
                else
                {
                    mLinearLayoutProgress.setVisibility(View.GONE);
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.frament_report_detail_item_userCover:
                if (mReportDetailInfo != null)
                {
                    Intent intent = new Intent(getSupportActivity(),
                            PersonalActivity.class);
                    if (MeishiApplication.getApplication().getUserInfo() != null
                            && MeishiApplication.getApplication().getUserInfo()
                                    .getSid() != null
                            && MeishiApplication.getApplication().getUserInfo()
                                    .getuId().trim()
                                    .equals(mReportDetailInfo.getUid().trim()))
                    {
                        intent.putExtra("ismyself", true);
                    }
                    else
                    {
                        intent.putExtra("ismyself", false);
                    }
                    intent.putExtra("uid", mReportDetailInfo.getUid().trim());
                    intent.putExtra("name", mTextViewUserName.getText()
                            .toString());

                    startActivity(intent);
                    getSupportActivity().finish();
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_back_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_report_detail_back:
                if (mReportDetailInfo != null)
                {

                    Intent intent = new Intent(getSupportActivity(),
                            RecipeDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("recipe_id",
                            mReportDetailInfo.getRecipeid());
                    bundle.putString("recipe_name", name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getSupportActivity().finish();
                }
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
