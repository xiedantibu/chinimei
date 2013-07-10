package com.xlm.meishichina.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.RadioButton;

import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RecipeRowInfo;
import com.xlm.meishichina.bean.RecipeRowInfo.RecipeRowItem;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.RecipeDetailActivity;
import com.xlm.meishichina.ui.adapter.RecipeRowAdapter;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshListView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RecipeRowFragment extends BaseFragment implements OnClickListener,
        MeishiConfig, OnCheckedChangeListener
{

    private FrameLayout mFrameLayout;
    private RadioGroup mRadioGroup;
    private RadioButton mButtonTime, mButtonFav, mButtonComment, mButtonRead;
    private int mCurrentTag = 1;
    private int mCurrentPage = 1;
    private int mSumPage = 1;
    private boolean isRefresh = false;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private RecipeRowAdapter mAdapter;
    private RecipeRowInfo mRecipeRowInfo = null;
    private List<RecipeRowItem> mList = new ArrayList<RecipeRowInfo.RecipeRowItem>();
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case HANDLE_REQUEST_TYPE_ROW_SUCCESS:
                    if (mPullToRefreshListView == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.activity_recipe_listview);
                        mPullToRefreshListView = (PullToRefreshListView) mViewStub
                                .inflate();
                        mListView = mPullToRefreshListView.getRefreshableView();
                        mAdapter = new RecipeRowAdapter(getSupportActivity(),
                                mList);
                        mListView.setAdapter(mAdapter);
                        mListView
                                .setOnItemClickListener(new OnItemClickListener()
                                {

                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> paramAdapterView,
                                            View paramView, int paramInt,
                                            long paramLong)
                                    {

                                        if (mList.size() > 0
                                                && mList.get(paramInt - 1)
                                                        .getLink().trim()
                                                        .contains("id="))
                                        {
                                            Intent intent = new Intent(
                                                    getSupportActivity(),
                                                    RecipeDetailActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(
                                                    "recipe_id",
                                                    mList.get(paramInt - 1)
                                                            .getLink()
                                                            .trim()
                                                            .substring(
                                                                    mList.get(
                                                                            paramInt - 1)
                                                                            .getLink()
                                                                            .trim()
                                                                            .lastIndexOf(
                                                                                    "=") + 1));
                                            bundle.putString("recipe_name",
                                                    mList.get(paramInt - 1)
                                                            .getSubject()
                                                            .trim());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }
                                });
                        mPullToRefreshListView
                                .setOnRefreshListener(new OnRefreshListener2()
                                {

                                    @Override
                                    public void onPullDownToRefresh(
                                            PullToRefreshBase refreshView)
                                    {
                                        HttpRequest.getInstance().requestRow(
                                                getSupportActivity(), mHandler,
                                                mCurrentTag, 1, false);
                                        isRefresh = true;
                                    }

                                    @Override
                                    public void onPullUpToRefresh(
                                            PullToRefreshBase refreshView)
                                    {
                                        if (mCurrentPage < mSumPage)
                                        {
                                            HttpRequest
                                                    .getInstance()
                                                    .requestRow(
                                                            getSupportActivity(),
                                                            mHandler,
                                                            mCurrentTag,
                                                            mCurrentPage + 1,
                                                            false);
                                        }
                                        else
                                        {
                                            Toaster.showShort(
                                                    getSupportActivity(),
                                                    MeishiApplication
                                                            .getApplication()
                                                            .getString(
                                                                    R.string.fragment_personal_load));
                                            mPullToRefreshListView
                                                    .onRefreshComplete();

                                        }
                                    }
                                });
                    }

                    if (isRefresh)
                    {
                        mList.clear();
                    }
                    mRecipeRowInfo = (RecipeRowInfo) msg.obj;
                    if (mRecipeRowInfo != null
                            && mRecipeRowInfo.getmRowItems() != null
                            && mRecipeRowInfo.getmRowItems().size() > 0)
                    {
                        mPullToRefreshListView.setVisibility(View.VISIBLE);
                        mImageViewEmpty.setVisibility(View.GONE);
                        mCurrentPage = StringUtil.toInt(mRecipeRowInfo.getCur()
                                .trim());
                        mSumPage = StringUtil.toInt(mRecipeRowInfo.getPages()
                                .trim());
                        mList.addAll(mRecipeRowInfo.getmRowItems());
                        mAdapter.notifyDataSetChanged();
                        if (isRefresh)
                        {
                            isRefresh = false;
                            mListView.setSelection(1);
                        }
                    }
                    else
                    {
                        mPullToRefreshListView.setVisibility(View.GONE);
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                    }
                    mPullToRefreshListView.onRefreshComplete();
                    break;

                case HANDLE_REQUEST_TYPE_ROW_FAIL:
                    if (mPullToRefreshListView != null)
                    {

                        if (isRefresh)
                        {
                            isRefresh = false;
                        }
                        mPullToRefreshListView.onRefreshComplete();
                        if (mList != null && mList.size() > 0)
                        {
                            mPullToRefreshListView.setVisibility(View.VISIBLE);
                            mImageViewEmpty.setVisibility(View.GONE);
                        }
                        else
                        {
                            mPullToRefreshListView.setVisibility(View.GONE);
                            mImageViewEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                    }

                    Toaster.showShort(getSupportActivity(), msg.obj.toString());
                    break;
            }

            mButtonTime.setEnabled(true);
            mButtonComment.setEnabled(true);
            mButtonFav.setEnabled(true);
            mButtonRead.setEnabled(true);
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_recipe_row_base, null);
        mFrameLayout = (FrameLayout) view
                .findViewById(R.id.fragment_recipe_row_base_listview);
        mRadioGroup = (RadioGroup) view
                .findViewById(R.id.fragment_recipe_row_base_tab);
        mLinearLayoutProgress = (LinearLayout) mFrameLayout
                .findViewById(R.id.progressContainer);
        mImageViewEmpty = (ImageView) mFrameLayout.findViewById(R.id.emptys);
        mViewStub = (ViewStub) mFrameLayout.findViewById(R.id.loader_content);
        mButtonTime = (RadioButton) mRadioGroup
                .findViewById(R.id.fragment_recipe_row_time);
        mButtonFav = (RadioButton) mRadioGroup
                .findViewById(R.id.fragment_recipe_row_fav);
        mButtonComment = (RadioButton) mRadioGroup
                .findViewById(R.id.fragment_recipe_row_comment);
        mButtonRead = (RadioButton) mRadioGroup
                .findViewById(R.id.fragment_recipe_row_read);
        mLinearLayoutProgress.setVisibility(View.VISIBLE);
        HttpRequest.getInstance().requestRow(getSupportActivity(), mHandler,
                mCurrentTag, 1, true);
        mButtonTime.setChecked(true);
        mButtonComment.setEnabled(false);
        mButtonFav.setEnabled(false);
        mButtonRead.setEnabled(false);
        mImageViewEmpty.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.emptys:
                mLinearLayoutProgress.setVisibility(View.VISIBLE);
                mImageViewEmpty.setVisibility(View.GONE);
                HttpRequest.getInstance().requestRow(getSupportActivity(),
                        mHandler, mCurrentTag, 1, false);
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt)
    {

        switch (paramInt)
        {
            case R.id.fragment_recipe_row_time:
                mCurrentTag = 1;
                mButtonTime.setChecked(true);

                mButtonComment.setEnabled(false);
                mButtonFav.setEnabled(false);
                mButtonRead.setEnabled(false);
                break;
            case R.id.fragment_recipe_row_fav:
                mCurrentTag = 2;
                mButtonFav.setChecked(true);

                mButtonTime.setEnabled(false);
                mButtonFav.setEnabled(false);
                mButtonRead.setEnabled(false);
                break;
            case R.id.fragment_recipe_row_comment:
                mCurrentTag = 3;

                mButtonComment.setChecked(true);

                mButtonTime.setEnabled(false);
                mButtonComment.setEnabled(false);
                mButtonRead.setEnabled(false);
                break;
            case R.id.fragment_recipe_row_read:
                mCurrentTag = 4;
                mButtonTime.setEnabled(false);
                mButtonComment.setEnabled(false);
                mButtonFav.setEnabled(false);

                mButtonRead.setChecked(true);
                break;
        }
        mCurrentPage = 1;
        mSumPage = 1;
        HttpRequest.getInstance().requestRow(getSupportActivity(), mHandler,
                mCurrentTag, 1, false);
        mLinearLayoutProgress.setVisibility(View.VISIBLE);
        mImageViewEmpty.setVisibility(View.GONE);
        if (mPullToRefreshListView != null)
        {
            mPullToRefreshListView.setVisibility(View.GONE);
            mList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

}
