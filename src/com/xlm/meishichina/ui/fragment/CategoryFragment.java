package com.xlm.meishichina.ui.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.ExpandableListView;
import org.holoeverywhere.widget.ExpandableListView.OnChildClickListener;
import org.holoeverywhere.widget.LinearLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.CateInfo;
import com.xlm.meishichina.bean.Category;
import com.xlm.meishichina.db.FinalDb;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.RecipeListActivity;
import com.xlm.meishichina.ui.adapter.ExpandableAdapter;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;

public class CategoryFragment extends BaseFragment implements OnClickListener,
        MeishiConfig
{

    private FinalDb db;
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + "com.xlm.meishichina/databases/categories.db";

    private LinearLayout mLinearLayoutCategory;
    private ExpandableListView mExpandableListView;
    private List<Category> mCategories = new ArrayList<Category>();
    private Map<Integer, List<CateInfo>> map = new HashMap<Integer, List<CateInfo>>();
    private ExpandableAdapter mAdapter;
    private EditText mEditTextSearch;
    private ImageView mImageViewClearSearchWord;
    private Button mButtonSearch;
    /**
     * mInputMethodManager Èí¼üÅÌ¹ÜÀí
     */
    private InputMethodManager mInputMethodManager;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case HANDLE_REQUEST_COLLECT_DATEBASE_SUCCESS:
                    Logmeishi.d("HANDLE_REQUEST_COLLECT_DATEBASE_SUCCESS");
                    if (mLinearLayoutCategory == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.fragment_category_expandable_list_content);
                        mLinearLayoutCategory = (LinearLayout) mViewStub
                                .inflate();
                        mExpandableListView = (ExpandableListView) mLinearLayoutCategory
                                .findViewById(R.id.fragment_category_expandable_list);
                        mEditTextSearch = (EditText) mLinearLayoutCategory
                                .findViewById(R.id.fragment_category_expandable_search_edit);
                        mButtonSearch = (Button) mLinearLayoutCategory
                                .findViewById(R.id.fragment_category_expandable_search_submit_button);
                        mImageViewClearSearchWord = (ImageView) mLinearLayoutCategory
                                .findViewById(R.id.fragment_category_expandable_clear_search_word);
                        mImageViewClearSearchWord
                                .setOnClickListener(CategoryFragment.this);
                        mButtonSearch.setOnClickListener(CategoryFragment.this);

                        mExpandableListView
                                .setOnTouchListener(new OnTouchListener()
                                {

                                    @Override
                                    public boolean onTouch(View paramView,
                                            MotionEvent paramMotionEvent)
                                    {
                                        if (paramView != null)
                                        {

                                            InputMethodManager inputmanger = (InputMethodManager) getSystemService("input_method");
                                            inputmanger.hideSoftInputFromWindow(
                                                    paramView.getWindowToken(),
                                                    0);
                                        }
                                        return false;
                                    }
                                });

                        mExpandableListView
                                .setOnChildClickListener(new OnChildClickListener()
                                {

                                    @Override
                                    public boolean onChildClick(
                                            ExpandableListView parent, View v,
                                            int groupPosition,
                                            int childPosition, long id)
                                    {
                                        Logmeishi.d("onChildClick"
                                                + groupPosition + " "
                                                + childPosition);
                                        Intent intent = new Intent(
                                                getSupportActivity(),
                                                RecipeListActivity.class);

                                        switch (groupPosition)
                                        {
                                            case 0:
                                            case 1:
                                            case 2:
                                            case 3:
                                            case 4:
                                            case 5:
                                                intent.putExtra("type", 1);
                                                break;
                                            case 6:
                                                intent.putExtra("type", 2);
                                                break;
                                            default:
                                                intent.putExtra("type", 3);

                                        }
                                        CateInfo mCateInfo = map.get(
                                                mCategories.get(groupPosition)
                                                        .getId()).get(
                                                childPosition);
                                        Logmeishi.d("id-------->"
                                                + mCateInfo.getReq_id()
                                                + " ---name---------->"
                                                + mCateInfo.getDisplay_name());
                                        intent.putExtra("id", String
                                                .valueOf(mCateInfo.getReq_id()));
                                        intent.putExtra("name",
                                                mCateInfo.getDisplay_name());
                                        startActivity(intent);
                                        return true;
                                    }
                                });
                        mEditTextSearch
                                .addTextChangedListener(new TextWatcher()
                                {

                                    @Override
                                    public void onTextChanged(
                                            CharSequence paramCharSequence,
                                            int paramInt1, int paramInt2,
                                            int paramInt3)
                                    {
                                        if (paramCharSequence.length() > 0)
                                        {
                                            mImageViewClearSearchWord
                                                    .setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            mImageViewClearSearchWord
                                                    .setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void beforeTextChanged(
                                            CharSequence paramCharSequence,
                                            int paramInt1, int paramInt2,
                                            int paramInt3)
                                    {

                                    }

                                    @Override
                                    public void afterTextChanged(
                                            Editable paramEditable)
                                    {
                                    }
                                });
                        mAdapter = new ExpandableAdapter(getSupportActivity(),
                                mCategories, map);
                        mExpandableListView.setAdapter(mAdapter);
                    }
                    break;

                case HANDLE_REQUEST_COLLECT_DATEBASE_FAIL:
                    Logmeishi.d("HANDLE_REQUEST_COLLECT_DATEBASE_FAIL");
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
        mLinearLayoutProgress.setVisibility(View.VISIBLE);
        mImageViewEmpty.setOnClickListener(this);
        mInputMethodManager = ((InputMethodManager) MeishiApplication
                .getApplication().getSystemService("input_method"));
        mHandler.post(new Runnable()
        {

            @Override
            public void run()
            {
                InstallDatabase();
                mHandler.sendEmptyMessage(HANDLE_REQUEST_COLLECT_DATEBASE_SUCCESS);
            }
        });
        return view;
    }

    @Override
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.fragment_category_expandable_clear_search_word:
                mEditTextSearch.setText("");
                break;

            case R.id.fragment_category_expandable_search_submit_button:
                if (!"".equals(mEditTextSearch.getText().toString().trim()))
                {
                    mInputMethodManager.hideSoftInputFromWindow(
                            mEditTextSearch.getApplicationWindowToken(), 0);
                    Intent intent = new Intent(getSupportActivity(),
                            RecipeListActivity.class);
                    intent.putExtra("type", 4);
                    intent.putExtra("id", mEditTextSearch.getText().toString()
                            .trim());
                    startActivity(intent);
                }
                else
                {
                    Toaster.showLong(
                            getSupportActivity(),
                            MeishiApplication.getApplication().getString(
                                    R.string.fragment_collect_search_tips));

                }

                break;
        }

    }

    public void InstallDatabase()
    {

        File file = new File(DB_PATH);
        if (!file.exists())
        {
            InputStream inputStream;
            FileOutputStream outputStream;
            byte[] arrayOfByte;
            int readCount = 0;
            try
            {
                inputStream = MeishiApplication.getApplication().getResources()
                        .openRawResource(R.raw.categories);
                outputStream = new FileOutputStream(file);
                arrayOfByte = new byte[400000];
                while ((readCount = inputStream.read(arrayOfByte)) != -1)
                {
                    outputStream.write(arrayOfByte, 0, readCount);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

            }
            catch (FileNotFoundException e)
            {
                mHandler.sendEmptyMessage(HANDLE_REQUEST_COLLECT_DATEBASE_FAIL);
            }
            catch (IOException e)
            {
                mHandler.sendEmptyMessage(HANDLE_REQUEST_COLLECT_DATEBASE_FAIL);
            }
        }

        db = FinalDb.create(getSupportActivity(), "categories.db", IS_DEBUG, 1,
                null);
        mCategories = db.findAll(Category.class);
        if (mCategories != null && mCategories.size() > 0)
        {
            for (int i = 0; i < mCategories.size(); i++)
            {
                List<CateInfo> mList = db.findAllByWhere(CateInfo.class,
                        " cate_id = " + mCategories.get(i).getId());
                map.put(mCategories.get(i).getId(), mList);
            }
        }
    }
}
