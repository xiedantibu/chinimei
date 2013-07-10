package com.xlm.meishichina.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.GridView;
import org.holoeverywhere.widget.LinearLayout;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RecipesCollectListInfo;
import com.xlm.meishichina.bean.RecipesCollectListInfo.RecipesCollectItem;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.RecipeListActivity;
import com.xlm.meishichina.ui.adapter.RecipeCollectsAdapter;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshGridView;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

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

/**
 * 
 * @ClassName: RecipeCollectsFragment
 * @Description: ²ËÆ××¨Ìâ
 * @author xlm
 * @date 2013-3-11 ÉÏÎç10:24:10
 */
public class RecipeCollectsFragment extends BaseFragment implements
        MeishiConfig, OnClickListener
{

    private PullToRefreshGridView mPullToRefreshGridView;
    private GridView mGridView;
    private RecipeCollectsAdapter mAdapter;
    private RecipesCollectListInfo mRecipesCollectListInfo = null;
    private List<RecipesCollectItem> mList = new ArrayList<RecipesCollectListInfo.RecipesCollectItem>();
    private boolean mRefreshFlag = false;
    private int currentIdx = 1;

    private int currentItemNum = 0;

    private int defaultItemNum = 20;

    private int totalItemNum = 0;

    private int requestItemNum = 0;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case HANDLE_REQUEST_RECIPE_COLLECT_LIST_SUCCESS:
                    if (mPullToRefreshGridView == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.fragment_recipe_collects_layout);
                        mPullToRefreshGridView = (PullToRefreshGridView) mViewStub
                                .inflate();
                        mGridView = mPullToRefreshGridView.getRefreshableView();
                        mAdapter = new RecipeCollectsAdapter(
                                getSupportActivity(), mList);
                        mGridView.setAdapter(mAdapter);
                        mGridView
                                .setOnItemClickListener(new OnItemClickListener()
                                {

                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> paramAdapterView,
                                            View paramView, int paramInt,
                                            long paramLong)
                                    {

                                        if (mList != null && mList.size() > 0)
                                        {
                                            Intent intent = new Intent(
                                                    getSupportActivity(),
                                                    RecipeListActivity.class);
                                            intent.putExtra("type", 3);
                                            intent.putExtra("id",
                                                    mList.get(paramInt).getId()
                                                            .trim());
                                            intent.putExtra("name",
                                                    mList.get(paramInt)
                                                            .getTitle().trim());
                                            startActivity(intent);
                                        }
                                    }
                                });
                        mPullToRefreshGridView
                                .setOnRefreshListener(new OnRefreshListener2<GridView>()
                                {

                                    @Override
                                    public void onPullDownToRefresh(
                                            PullToRefreshBase<GridView> refreshView)
                                    {
                                        HttpRequest.getInstance()
                                                .requestRecipeCollect(
                                                        getSupportActivity(),
                                                        mHandler, 1,
                                                        defaultItemNum, false);
                                        mRefreshFlag = true;
                                    }

                                    @Override
                                    public void onPullUpToRefresh(
                                            PullToRefreshBase<GridView> refreshView)
                                    {
                                        if (requestItemNum != 0)
                                        {
                                            HttpRequest
                                                    .getInstance()
                                                    .requestRecipeCollect(
                                                            getSupportActivity(),
                                                            mHandler,
                                                            currentIdx + 1,
                                                            defaultItemNum,
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
                                            mPullToRefreshGridView
                                                    .onRefreshComplete();

                                        }

                                    }
                                });

                    }

                    mRecipesCollectListInfo = (RecipesCollectListInfo) msg.obj;

                    if (mRecipesCollectListInfo != null)
                    {
                        if (mRefreshFlag)
                        {
                            mList.clear();
                        }
                        mPullToRefreshGridView.setVisibility(View.VISIBLE);
                        mImageViewEmpty.setVisibility(View.GONE);

                        currentIdx = StringUtil.toInt(mRecipesCollectListInfo
                                .getIdx().trim());
                        currentItemNum = StringUtil
                                .toInt(mRecipesCollectListInfo.getCount()
                                        .trim());
                        totalItemNum = StringUtil.toInt(mRecipesCollectListInfo
                                .getTotal().trim());
                        if (currentItemNum == defaultItemNum)
                        {

                            if (currentIdx * currentItemNum < totalItemNum)
                            {
                                int num = (totalItemNum - currentIdx
                                        * currentItemNum);
                                requestItemNum = ((num >= 20) ? 20 : num);
                            }
                            else
                            {
                                requestItemNum = 0;
                            }

                        }
                        else
                        {
                            requestItemNum = 0;
                        }
                    }
                    mList.addAll(mRecipesCollectListInfo
                            .getmRecipesCollectItems());
                    mAdapter.notifyDataSetChanged();
                    mPullToRefreshGridView.onRefreshComplete();
                    if (mRefreshFlag)
                    {
                        mRefreshFlag = false;
                        mGridView.setSelection(1);
                    }
                    if (mList.size() == 0)
                    {
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                        mPullToRefreshGridView.setVisibility(View.GONE);
                    }

                    break;
                case HANDLE_REQUEST_RECIPE_COLLECT_LIST_FAIL:
                    if (mPullToRefreshGridView != null)
                    {

                        if (mRefreshFlag)
                        {
                            mRefreshFlag = false;
                        }
                        mPullToRefreshGridView.onRefreshComplete();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_loading_content, null);
        mLinearLayoutProgress = (LinearLayout) view
                .findViewById(R.id.progressContainer);
        mImageViewEmpty = (ImageView) view.findViewById(R.id.emptys);
        mViewStub = (ViewStub) view.findViewById(R.id.loader_content);
        mImageViewEmpty.setOnClickListener(this);

        HttpRequest.getInstance().requestRecipeCollect(getSupportActivity(),
                mHandler, currentIdx, defaultItemNum, true);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.emptys:

                if (mPullToRefreshGridView != null)
                {
                    mPullToRefreshGridView.setVisibility(View.GONE);
                }
                mImageViewEmpty.setVisibility(View.GONE);
                mLinearLayoutProgress.setVisibility(View.VISIBLE);

                HttpRequest.getInstance().requestRecipeCollect(
                        getSupportActivity(), mHandler, currentIdx,
                        defaultItemNum, true);
                break;

            default:
                break;
        }

    }

}
