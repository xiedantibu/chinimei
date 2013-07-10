package com.xlm.meishichina.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import com.xlm.meishichina.util.StringUtil;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.PersonalReportListInfo;
import com.xlm.meishichina.bean.PersonalReportListInfo.PersonalReportItem;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.assist.PauseOnScrollListener;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.LoginActivity;
import com.xlm.meishichina.ui.activity.ReportDetailActivity;
import com.xlm.meishichina.ui.adapter.PersonalReportListAdapter;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshListView;

public class PersonalReportFragment extends Fragment implements MeishiConfig,
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

    private String id = "";

    private int currentIdx = 1;

    private int currentItemNum = 0;

    private int defaultItemNum = 10;

    private int totalItemNum = 0;

    private int requestItemNum = 0;

    private int currentCheckItem = 0;

    private PullToRefreshListView mPullToRefreshListView = null;

    public ListView mListView = null;

    public ActionMode mActionMode;

    private boolean isMySelf = false;

    private PersonalReportListAdapter mReportListAdapter = null;

    private PersonalReportListInfo mReportListInfo = null;

    private List<PersonalReportItem> mList = new ArrayList<PersonalReportListInfo.PersonalReportItem>();
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case HANDLE_REQUEST_REPOT_LIST_SUCCESS:
                    if (mPullToRefreshListView == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.fragment_personal_listview);
                        mPullToRefreshListView = (PullToRefreshListView) mViewStub
                                .inflate();
                        mListView = mPullToRefreshListView.getRefreshableView();

                        mReportListAdapter = new PersonalReportListAdapter(
                                getSupportActivity(), mList);

                        mListView.setAdapter(mReportListAdapter);
                        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                        mListView
                                .setOnScrollListener(new PauseOnScrollListener(
                                        mImageLoader, true, true));

                        mListView
                                .setOnItemClickListener(new OnItemClickListener()
                                {
                                    public void onItemClick(
                                            android.widget.AdapterView<?> paramAdapterView,
                                            View paramView, int paramInt,
                                            long paramLong)
                                    {
                                        if (mActionMode != null)
                                        {
                                            mListView.clearChoices();
                                            mActionMode.finish();
                                            mActionMode = null;
                                            return;
                                        }
                                        mListView.clearChoices();

                                        Intent intent = new Intent(
                                                getSupportActivity(),
                                                ReportDetailActivity.class);

                                        intent.putExtra("id",
                                                mList.get(paramInt - 1).getId()
                                                        .trim());
                                        intent.putExtra("name",
                                                mList.get(paramInt - 1)
                                                        .getRecipetitle()
                                                        .trim());
                                        startActivity(intent);
                                    };
                                });

                        mListView
                                .setOnItemLongClickListener(new OnItemLongClickListener()
                                {

                                    @Override
                                    public boolean onItemLongClick(
                                            AdapterView<?> parent, View view,
                                            int position, long id)
                                    {
                                        if (position - 1 < mList.size()
                                                && position - 1 >= 0
                                                && isMySelf)
                                        {
                                            if (mActionMode != null)
                                            {
                                                mActionMode.finish();
                                                mActionMode = null;
                                            }
                                            mListView.setItemChecked(position,
                                                    true);
                                            mReportListAdapter
                                                    .notifyDataSetChanged();
                                            mActionMode = getSupportActivity()
                                                    .startActionMode(
                                                            new PersonalReportActionMode());
                                            return true;
                                        }
                                        else
                                        {
                                            Intent intent = new Intent(
                                                    getSupportActivity(),
                                                    ReportDetailActivity.class);

                                            intent.putExtra("id",
                                                    mList.get(position - 1)
                                                            .getId().trim());
                                            intent.putExtra("name",
                                                    mList.get(position - 1)
                                                            .getRecipetitle()
                                                            .trim());
                                            startActivity(intent);
                                            return true;
                                        }

                                    }
                                });

                        mPullToRefreshListView
                                .setOnRefreshListener(new OnRefreshListener<ListView>()
                                {

                                    @Override
                                    public void onRefresh(
                                            PullToRefreshBase<ListView> refreshView)
                                    {
                                        if (requestItemNum != 0)
                                        {
                                            HttpRequest
                                                    .getInstance()
                                                    .requestReportList(
                                                            getSupportActivity(),
                                                            mHandler, id,
                                                            currentIdx + 1,
                                                            defaultItemNum);
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

                    mPullToRefreshListView.onRefreshComplete();
                    mImageViewEmpty.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);

                    mReportListInfo = (PersonalReportListInfo) msg.obj;

                    currentIdx = StringUtil.toInt(mReportListInfo.getIdx()
                            .trim());
                    currentItemNum = StringUtil.toInt(mReportListInfo
                            .getCount().trim());
                    totalItemNum = StringUtil.toInt(mReportListInfo.getTotal()
                            .trim());

                    if (currentItemNum == defaultItemNum)
                    {

                        if (currentIdx * currentItemNum < totalItemNum)
                        {
                            int num = (totalItemNum - currentIdx
                                    * currentItemNum);
                            requestItemNum = ((num >= 10) ? 10 : num);
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

                    mList.addAll(mReportListInfo.getReportItems());
                    mReportListAdapter.notifyDataSetChanged();
                    if (mList.size() == 0)
                    {
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }
                    break;

                case HANDLE_REQUEST_REPOR_LIST_FAIL:

                    if (mPullToRefreshListView != null)
                    {
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                    String fav_msg_fail = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), fav_msg_fail);
                    break;
                case HANDLE_REQUEST_DELETE_REPORT_SUCCESS:
                    String fav_msg_success = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), fav_msg_success);
                    mList.remove(currentCheckItem);
                    mReportListAdapter.notifyDataSetChanged();
                    if (mList.size() == 0)
                    {
                        if (mPullToRefreshListView != null)
                        {
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                    }
                    break;
                case HANDLE_REQUEST_DELETE_REPORT_FAIL:
                    String fav_msg_fail2 = (String) msg.obj;
                    if (fav_msg_fail2.equals(MeishiApplication.getApplication()
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

                        Toaster.showShort(getSupportActivity(), fav_msg_fail2);
                    }
                    break;
            }
        };
    };

    public static PersonalReportFragment getInstance(Bundle bundle)
    {
        PersonalReportFragment fragment = new PersonalReportFragment();
        fragment.setArguments(bundle);
        return fragment;
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
            isMySelf = bundle.getBoolean("ismyself");
            if (!"".equals(id))
            {
                HttpRequest.getInstance().requestReportList(
                        getSupportActivity(), mHandler, id, 1, 10);
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.emptys:
                if (!"".equals(id))
                {
                    HttpRequest.getInstance().requestReportList(
                            getSupportActivity(), mHandler, id, 1, 10);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                    if (mPullToRefreshListView != null)
                        mPullToRefreshListView.setVisibility(View.GONE);
                }
                else
                {
                    mLinearLayoutProgress.setVisibility(View.GONE);
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
    }

    private class PersonalReportActionMode implements ActionMode.Callback
    {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            if (mActionMode == null)
                mActionMode = mode;

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            buildMenu(mode, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.menu_remove:
                    if (isMySelf)
                    {
                        if (MeishiApplication.getApplication().getUserInfo() != null
                                && MeishiApplication.getApplication()
                                        .getUserInfo().getSid() != null)
                        {

                            if (mList != null && mList.size() > 0)
                            {
                                HttpRequest
                                        .getInstance()
                                        .requestDeleteReport(
                                                getSupportActivity(),
                                                mHandler,
                                                mList.get(
                                                        mListView
                                                                .getCheckedItemPosition() - 1)
                                                        .getId().trim());
                                currentCheckItem = mListView
                                        .getCheckedItemPosition() - 1;
                                mode.finish();
                            }
                            else
                            {
                                Toaster.showShort(
                                        getSupportActivity(),
                                        getString(R.string.login_please_first_logon));
                                Intent intent = new Intent(
                                        getSupportActivity(),
                                        LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                    }
                    break;

                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            mActionMode = null;
            mListView.clearChoices();
            mReportListAdapter.notifyDataSetChanged();
        }

        protected void buildMenu(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            menu.clear();
            if (isMySelf)
            {
                inflater.inflate(R.menu.menu_delete_and_share, menu);
                menu.findItem(R.id.menu_share).setVisible(false);
            }
            mode.setTitle(mList.get(mListView.getCheckedItemPosition() - 1)
                    .getRecipetitle().trim());

        }

    }
}
