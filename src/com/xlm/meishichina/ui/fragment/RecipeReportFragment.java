package com.xlm.meishichina.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.AlertDialog.Builder;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.ReportListInfo;
import com.xlm.meishichina.bean.ReportListInfo.ReportItem;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.assist.PauseOnScrollListener;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.LoginActivity;
import com.xlm.meishichina.ui.activity.ReportDetailActivity;
import com.xlm.meishichina.ui.adapter.ReportListAdapter;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshListView;

public class RecipeReportFragment extends Fragment implements MeishiConfig,
        OnClickListener
{

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

    private RelativeLayout mRelativeLayoutViewStub;

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private RelativeLayout mRelativeLayoutLoadMore;

    private Button mButtonMore, mButtonMyReport;

    private LinearLayout mLinearLayoutLoading;

    private TextView mTextViewLoadingText;

    private TextView mTextViewErrorText;

    private List<ReportItem> mList = new ArrayList<ReportItem>();

    private ReportListAdapter mAdapter;

    private ReportListInfo mReportListInfo = null;

    private String id = "";
    private String name = "";

    private int currentIdx = 1;

    private int currentItemNum = 0;

    private int defaultItemNum = 10;

    private int totalItemNum = 0;

    private int requestItemNum = 0;

    private onActivityChangeListerner mListerner;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case HANDLE_REQUEST_RECIPEREPORTLIST_SUCCESS:

                    if (mRelativeLayoutViewStub == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.fragment_recipe_report_base);
                        mRelativeLayoutViewStub = (RelativeLayout) mViewStub
                                .inflate();
                        mPullToRefreshListView = (PullToRefreshListView) mRelativeLayoutViewStub
                                .findViewById(R.id.fragment_recipe_report_list);

                        mButtonMyReport = (Button) mRelativeLayoutViewStub
                                .findViewById(R.id.fragment_recipe_report_button_myreport);

                        mListView = mPullToRefreshListView.getRefreshableView();
                        mListView
                                .setOnScrollListener(new PauseOnScrollListener(
                                        mImageLoader, true, true));
                        mRelativeLayoutLoadMore = (RelativeLayout) LayoutInflater
                                .inflate(getSupportActivity(),
                                        R.layout.layout_loading_more);

                        mButtonMore = (Button) mRelativeLayoutLoadMore
                                .findViewById(R.id.layout_more_button);

                        mLinearLayoutLoading = (LinearLayout) mRelativeLayoutLoadMore
                                .findViewById(R.id.layout_more_loading);
                        mTextViewLoadingText = (TextView) mRelativeLayoutLoadMore
                                .findViewById(R.id.layout_more_loading_text);
                        mTextViewErrorText = (TextView) mRelativeLayoutLoadMore
                                .findViewById(R.id.layout_more_err_text);

                        mListView.addFooterView(mRelativeLayoutLoadMore);

                        mAdapter = new ReportListAdapter(getSupportActivity(),
                                mList);

                        mListView.setAdapter(mAdapter);

                        mListView
                                .setOnItemClickListener(new OnItemClickListener()
                                {

                                    @Override
                                    public void onItemClick(
                                            android.widget.AdapterView<?> paramAdapterView,
                                            View paramView, int paramInt,
                                            long paramLong)
                                    {
                                        if (paramInt <= mList.size())
                                        {
                                            Intent intent = new Intent(
                                                    getSupportActivity(),
                                                    ReportDetailActivity.class);

                                            intent.putExtra("id",
                                                    mList.get(paramInt - 1)
                                                            .getId().trim());
                                            intent.putExtra("name", name.trim());
                                            startActivity(intent);
                                        }

                                    }

                                });

                        mButtonMore
                                .setOnClickListener(RecipeReportFragment.this);
                        mTextViewErrorText
                                .setOnClickListener(RecipeReportFragment.this);

                        mButtonMyReport
                                .setOnClickListener(RecipeReportFragment.this);
                    }

                    mImageViewEmpty.setVisibility(View.GONE);
                    mButtonMyReport.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                    mRelativeLayoutViewStub.setVisibility(View.VISIBLE);

                    mReportListInfo = (ReportListInfo) msg.obj;

                    currentIdx = StringUtil.toInt(mReportListInfo.getIdx()
                            .trim());
                    currentItemNum = StringUtil.toInt(mReportListInfo
                            .getCount().trim());
                    totalItemNum = StringUtil.toInt(mReportListInfo.getTotal()
                            .trim());

                    if (currentItemNum == defaultItemNum)
                    {
                        int num = (totalItemNum - currentIdx * currentItemNum);
                        requestItemNum = ((num >= 10) ? 10 : num);
                        if (requestItemNum == 0)
                        {
                            mButtonMore
                                    .setText(MeishiApplication
                                            .getApplication()
                                            .getString(
                                                    R.string.fragment_recipe_comment_load_more_no));
                        }
                        else
                        {

                            mButtonMore
                                    .setText(String
                                            .format(MeishiApplication
                                                    .getApplication()
                                                    .getString(
                                                            R.string.fragment_recipe_comment_load_more),
                                                    requestItemNum));
                        }

                    }
                    else
                    {
                        mButtonMore
                                .setText(MeishiApplication
                                        .getApplication()
                                        .getString(
                                                R.string.fragment_recipe_comment_load_more_no));
                        requestItemNum = 0;
                    }
                    if (currentItemNum != 0)
                    {
                        mButtonMore.setVisibility(View.VISIBLE);
                        mLinearLayoutLoading.setVisibility(View.GONE);
                        mTextViewErrorText.setVisibility(View.GONE);
                        mList.addAll(mReportListInfo.getmReportItems());
                        mAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        mPullToRefreshListView.setVisibility(View.GONE);
                        mButtonMyReport.setVisibility(View.VISIBLE);
                    }
                    break;

                case HANDLE_REQUEST_RECIPEREPORTLIST_FAIL:
                    if (mRelativeLayoutViewStub != null)
                    {
                        mButtonMore.setVisibility(View.GONE);
                        mLinearLayoutLoading.setVisibility(View.GONE);
                        mTextViewErrorText.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                    }
                    Toaster.showShort(getSupportActivity(), msg.obj.toString());
                    break;
            }
        };
    };

    public static RecipeReportFragment getInstance(Bundle bundle)
    {
        RecipeReportFragment fragment = new RecipeReportFragment();
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
            id = (bundle.getString("recipe_id")).trim() /* "13945" */;
            name = bundle.getString("recipe_name");
            HttpRequest.getInstance().recipeReportsRequest(
                    getSupportActivity(), mHandler, id, currentIdx,
                    defaultItemNum);
            mLinearLayoutProgress.setVisibility(View.VISIBLE);
            mImageViewEmpty.setOnClickListener(this);
        }
        else
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            mImageViewEmpty.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.emptys:
                if (!"".equals(id))
                {
                    HttpRequest.getInstance().recipeReportsRequest(
                            getSupportActivity(), mHandler, id, currentIdx,
                            defaultItemNum);
                    mImageViewEmpty.setVisibility(View.GONE);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.layout_more_button:
                if (requestItemNum != 0)
                {

                    mButtonMore.setVisibility(View.GONE);
                    mLinearLayoutLoading.setVisibility(View.VISIBLE);
                    mTextViewLoadingText
                            .setText(String
                                    .format(MeishiApplication
                                            .getApplication()
                                            .getString(
                                                    R.string.fragment_recipe_comment_loading_more),
                                            requestItemNum));
                    HttpRequest.getInstance().recipeReportsRequest(
                            getSupportActivity(), mHandler, id, currentIdx + 1,
                            defaultItemNum);
                }

                break;
            case R.id.layout_more_err_text:
                if (requestItemNum != 0)
                {

                    HttpRequest.getInstance().recipeReportsRequest(
                            getSupportActivity(), mHandler, id, currentIdx + 1,
                            defaultItemNum);
                    mButtonMore.setVisibility(View.GONE);
                    mTextViewErrorText.setVisibility(View.GONE);
                    mLinearLayoutLoading.setVisibility(View.VISIBLE);
                    mTextViewLoadingText
                            .setText(String
                                    .format(MeishiApplication
                                            .getApplication()
                                            .getString(
                                                    R.string.fragment_recipe_comment_loading_more),
                                            requestItemNum));
                }
                break;
            case R.id.fragment_recipe_report_button_myreport:
                final Builder builder = new AlertDialog.Builder(
                        getSupportActivity());
                builder.setItems(
                        new String[] {
                                MeishiApplication
                                        .getApplication()
                                        .getString(
                                                R.string.fragment_recipe_report_user_by_pic),
                                MeishiApplication
                                        .getApplication()
                                        .getString(
                                                R.string.fragment_recipe_report_user_by_camera) },
                        new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which)
                            {
                                if (!(MeishiApplication.getApplication()
                                        .getUserInfo() != null && MeishiApplication
                                        .getApplication().getUserInfo()
                                        .getSid() != null))
                                {
                                    Toaster.showShort(
                                            getSupportActivity(),
                                            getString(R.string.login_please_first_logon));
                                    Intent intent = new Intent(
                                            getSupportActivity(),
                                            LoginActivity.class);
                                    startActivity(intent);
                                    return;
                                }
                                String storageState = Environment
                                        .getExternalStorageState();
                                if (!storageState
                                        .equals(Environment.MEDIA_MOUNTED))
                                {
                                    Toaster.showShort(
                                            getSupportActivity(),
                                            MeishiApplication
                                                    .getApplication()
                                                    .getString(
                                                            R.string.fragment_recipe_report_tag));
                                    return;
                                }
                                switch (which)
                                {
                                    case 0:
                                        mListerner.startActionPickCrop();
                                        break;

                                    case 1:
                                        mListerner.startActionCamera();
                                        break;
                                }
                            }
                        });
                builder.create().show();
                break;
        }

    }

    public interface onActivityChangeListerner
    {
        public void startActionPickCrop();

        public void startActionCamera();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mListerner = (onActivityChangeListerner) activity;
    }

    public void requestTryAgain()
    {
        HttpRequest.getInstance().recipeReportsRequest(getSupportActivity(),
                mHandler, id, currentIdx, defaultItemNum);
        if (mList != null && mList.size() > 0)
        {
            mList.clear();
            mAdapter.notifyDataSetChanged();
        }
        mLinearLayoutProgress.setVisibility(View.VISIBLE);
        mRelativeLayoutViewStub.setVisibility(View.GONE);
        mImageViewEmpty.setVisibility(View.GONE);
    }
}
