package com.xlm.meishichina.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.app.AlertDialog.Builder;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.preference.PreferenceManager;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.xlm.meishichina.R;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.utils.StorageUtils;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.adapter.AppFragmentPagerAdapter;
import com.xlm.meishichina.ui.fragment.DialogsProgressDialogIndeterminateFragment;
import com.xlm.meishichina.ui.fragment.RecipeCommentFragment;
import com.xlm.meishichina.ui.fragment.RecipeDetailFragment;
import com.xlm.meishichina.ui.fragment.RecipeReportFragment;
import com.xlm.meishichina.ui.fragment.RecipeReportFragment.onActivityChangeListerner;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.viewpagerindicator.TitlePageIndicator;

public class RecipeDetailActivity extends BaseActivity implements
        onActivityChangeListerner
{

    private ViewPager mViewPager;
    private RecipePagerAdapter mPagerAdapter;
    private GestureDetector gestureDetector;
    private TitlePageIndicator mIndicator;
    /**
     * mDialogIndeterminateFragment dialog
     */
    private DialogsProgressDialogIndeterminateFragment mDialogIndeterminateFragment;
    private final static String FILE_SAVEPATH = StorageUtils.getCacheDirectory(
            MeishiApplication.getApplication()).getAbsolutePath()
            + "/upload/";
    private final static int CROP = 640;

    private Uri origUri;
    private Uri cropUri;
    private File uploadFile;
    private String uploadPath;
    private Bundle bundle = null;
    private RecipeReportFragment fragment = null;
    private RecipeDetailFragment mDetailFragment = null;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            if (mDialogIndeterminateFragment != null)
            {
                mDialogIndeterminateFragment.dismiss();
            }

            switch (msg.what)
            {
                case HANDLE_REQUEST_FAVCOLLECT_SUCCESS:
                    String fav_msg_success = (String) msg.obj;
                    Toaster.showShort(RecipeDetailActivity.this,
                            fav_msg_success);
                    break;

                case HANDLE_REQUEST_FAVCOLLECT_FAIL:
                    String fav_msg_fail = (String) msg.obj;
                    if (fav_msg_fail.equals(MeishiApplication.getApplication()
                            .getResources().getString(R.string.login_no_user)))
                    {
                        Toaster.showShort(RecipeDetailActivity.this,
                                getString(R.string.login_please_first_logon));
                        Intent intent = new Intent(RecipeDetailActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {

                        Toaster.showShort(RecipeDetailActivity.this,
                                fav_msg_fail);
                    }
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        buildViewPager();
        buildTitlePageIndicator();

        File savedir = new File(FILE_SAVEPATH);
        if (!savedir.exists())
        {
            savedir.mkdirs();
        }

        // 照片命名
        String origFileName = "meishi_upload.jpg";
        String cropFileName = "meishi_crop_upload.jpg";

        uploadPath = FILE_SAVEPATH + cropFileName;
        uploadFile = new File(uploadPath);

        origUri = Uri.fromFile(new File(FILE_SAVEPATH, origFileName));
        cropUri = Uri.fromFile(uploadFile);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void buildViewPager()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            bundle = intent.getExtras();
            bundle.putBoolean("from", true);// true代表来自RecipeDetailActivity
        }

        mViewPager = (ViewPager) this.findViewById(R.id.pager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        mViewPager.setOffscreenPageLimit(3);// 这表示你的预告加载的页面数量是3

        mPagerAdapter = new RecipePagerAdapter(getSupportFragmentManager());
        fragment = (RecipeReportFragment) mPagerAdapter.getItem(2);
        mDetailFragment = (RecipeDetailFragment) mPagerAdapter.getItem(1);
        mViewPager.setAdapter(mPagerAdapter);
        // mViewPager.setCurrentItem(1);
        // mViewPager.setOnPageChangeListener(onPageChangeListener);

        gestureDetector = new GestureDetector(RecipeDetailActivity.this,
                new MyOnGestureListener());

        mViewPager.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private void buildTitlePageIndicator()
    {
        mIndicator = (TitlePageIndicator) this.findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setSelectedBold(true);
        mIndicator.setCurrentItem(1);
        mIndicator.setOnPageChangeListener(onPageChangeListener);

        String recipeName = getIntent().getExtras().getString("recipe_name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(recipeName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        SubMenu subMenu = menu.addSubMenu(getString(R.string.app_name));
        String value = PreferenceManager.getDefaultSharedPreferences(
                MeishiApplication.getApplication()).getString(CONFIG_SP_THEME,
                "1");
        if (value.equals("1"))
        {
            subMenu.add(0, 1, 1, getString(R.string.yaoyiyao_menu_collect))
                    .setIcon(R.drawable.rating_not_important_light);
            subMenu.add(0, 2, 2, getString(R.string.yaoyiyao_menu_share))
                    .setIcon(R.drawable.social_share_light);
            subMenu.add(0, 3, 3,
                    getString(R.string.fragment_recipe_report_uponreport))
                    .setIcon(R.drawable.av_upload_light);
        }
        else if (value.equals("2"))
        {
            subMenu.add(0, 1, 1, getString(R.string.yaoyiyao_menu_collect))
                    .setIcon(R.drawable.rating_not_important_dark);
            subMenu.add(0, 2, 2, getString(R.string.yaoyiyao_menu_share))
                    .setIcon(R.drawable.social_share_dark);
            subMenu.add(0, 3, 3,
                    getString(R.string.fragment_recipe_report_uponreport))
                    .setIcon(R.drawable.av_upload_dark);
        }

        MenuItem item = subMenu.getItem();
        item.setIcon(R.drawable.base_action_bar_action_more);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
            case 1:
                if (MeishiApplication.getApplication().getUserInfo() != null
                        && MeishiApplication.getApplication().getUserInfo()
                                .getSid() != null)
                {
                    if (bundle != null)
                    {
                        mDialogIndeterminateFragment = DialogsProgressDialogIndeterminateFragment
                                .getInstance(getString(R.string.yaoyiyao_fav_collect_deal_data));
                        mDialogIndeterminateFragment
                                .show(getSupportFragmentManager());
                        HttpRequest.getInstance().favOrDelCollectRequest(
                                RecipeDetailActivity.this, mHandler,
                                (bundle.getString("recipe_id")).trim(), false);

                    }
                    else
                    {
                        Toaster.showShort(
                                RecipeDetailActivity.this,
                                getString(R.string.yaoyiyao_fav_collect_no_receipe));
                    }
                }
                else
                {
                    Toaster.showShort(RecipeDetailActivity.this,
                            getString(R.string.login_please_first_logon));
                    Intent intent = new Intent(RecipeDetailActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                }

                return true;
            case 2:

                if (bundle != null && mDetailFragment != null
                        && mDetailFragment.mRecipeDetailInfo != null)
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(
                            Intent.EXTRA_TEXT,
                            getString(R.string.yaoyiyao_share_from)
                                    + "\n"
                                    + mDetailFragment.mRecipeDetailInfo
                                            .getRecipeTitle()
                                    + "\n"
                                    + mDetailFragment.mRecipeDetailInfo
                                            .getRecipeDescr()
                                    + "\n"
                                    + mDetailFragment.mRecipeDetailInfo
                                            .getRecipeCover());
                    startActivity(Intent.createChooser(intent,
                            getString(R.string.app_name)));
                }
                return true;
            case 3:

                final Builder builder = new AlertDialog.Builder(
                        RecipeDetailActivity.this);
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
                                            RecipeDetailActivity.this,
                                            getString(R.string.login_please_first_logon));
                                    Intent intent = new Intent(
                                            RecipeDetailActivity.this,
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
                                            RecipeDetailActivity.this,
                                            MeishiApplication
                                                    .getApplication()
                                                    .getString(
                                                            R.string.fragment_recipe_report_tag));
                                    return;
                                }
                                switch (which)
                                {
                                    case 0:
                                        startActionPickCrop();
                                        break;

                                    case 1:
                                        startActionCamera();
                                        break;
                                }
                            }
                        });
                builder.create().show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RecipePagerAdapter extends AppFragmentPagerAdapter
    {
        List<Fragment> list = new ArrayList<Fragment>();
        private FragmentManager fm;

        public RecipePagerAdapter(FragmentManager fm)
        {
            super(fm);
            this.fm = fm;
            if (getRecipeCommentFragment() == null)
            {

                list.add(RecipeCommentFragment.getInstance(bundle));
            }
            else
            {
                list.add(getRecipeCommentFragment());
            }

            if (getRecipeDetailFragment() == null)
            {
                list.add(RecipeDetailFragment.getInstance(bundle));
            }
            else
            {
                list.add(getRecipeDetailFragment());
            }

            if (getRecipeReportFragment() == null)
            {
                list.add(RecipeReportFragment.getInstance(bundle));
            }
            else
            {
                list.add(getRecipeReportFragment());
            }
        }

        @Override
        public Fragment getItem(int position)
        {
            return list.get(position);
        }

        @Override
        protected String getTag(int position)
        {
            List<String> tagList = new ArrayList<String>();
            tagList.add(RecipeCommentFragment.class.getName());
            tagList.add(RecipeDetailFragment.class.getName());
            tagList.add(RecipeReportFragment.class.getName());
            return tagList.get(position);
        }

        @Override
        public int getCount()
        {
            return list.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                Object object)
        {
            switch (position)
            {
                case 1:

                    break;
            }
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return getResources().getString(
                            R.string.recipe_detail_activity_title_comment);
                case 1:
                    return getResources().getString(
                            R.string.recipe_detail_activity_title_detail);

                case 2:
                    return getResources().getString(
                            R.string.recipe_detail_activity_title_report);
            }
            return getResources().getString(
                    R.string.recipe_detail_activity_title_detail);
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        public void setFragments(List fragments)
        {
            if (this.list != null)
            {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : this.list)
                {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fm.executePendingTransactions();
            }
            this.list = fragments;
            notifyDataSetChanged();
        }
    }

    ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener()
    {
        public void onPageSelected(int position)
        {
            Logmeishi.d("--------onPageSelected-----" + position);
        };

    };

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener
    {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY)
        {

            if (velocityX > 100 && mViewPager.getCurrentItem() == 0)
            {
                RecipeDetailActivity.this.finish();
                return true;
            }
            return false;
        }
    }

    public RecipeDetailFragment getRecipeDetailFragment()
    {
        return (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeDetailFragment.class.getName());
    }

    public RecipeCommentFragment getRecipeCommentFragment()
    {
        return (RecipeCommentFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeCommentFragment.class.getName());
    }

    public RecipeReportFragment getRecipeReportFragment()
    {
        return (RecipeReportFragment) getSupportFragmentManager()
                .findFragmentByTag(RecipeReportFragment.class.getName());
    }

    @Override
    protected void onActivityResult(final int requestCode,
            final int resultCode, final Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case MeishiConfig.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop();
                break;
            case MeishiConfig.REQUEST_CODE_GETIMAGE_BYSDCARD:
            case MeishiConfig.REQUEST_CODE_GETIMAGE_BYCROP:
                Uri uri = data.getData();
                Logmeishi.d("uri--------->" + uri);
                Intent intent = new Intent(RecipeDetailActivity.this,
                        UploadReportActivity.class);
                intent.putExtra("PATH", uploadPath);
                intent.putExtra("recipe_id", bundle.getString("recipe_id"));
                startActivityForResult(intent,
                        MeishiConfig.REQUEST_CODE_UPLOAD_SUCCESS);
                break;
            case MeishiConfig.REQUEST_CODE_UPLOAD_SUCCESS:
                if (fragment != null)
                {
                    Logmeishi
                            .d("==========REQUEST_CODE_UPLOAD_SUCCESS=========");
                    fragment.requestTryAgain();
                }
                break;
        }
    }

    /**
     * 选择图片裁剪
     * 
     * @param output
     */
    public void startActionPickCrop()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("output", cropUri);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        PackageManager pkManager = getPackageManager();
        List<ResolveInfo> activities = pkManager.queryIntentActivities(intent,
                0);
        if (activities.size() > 1)
        {
            startActivityForResult(Intent.createChooser(
                    intent,
                    MeishiApplication.getApplication().getString(
                            R.string.fragment_recipe_report_choose_pic)),
                    MeishiConfig.REQUEST_CODE_GETIMAGE_BYSDCARD);
        }
        else
        {
            startActivityForResult(intent,
                    MeishiConfig.REQUEST_CODE_GETIMAGE_BYSDCARD);
        }
    }

    /**
     * 相机拍照
     * 
     * @param output
     */
    public void startActionCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, origUri);
        startActivityForResult(intent,
                MeishiConfig.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    /**
     * 拍照后裁剪
     * 
     * @param data
     *            原始图片
     * @param output
     *            裁剪后图片
     */
    private void startActionCrop()
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(origUri, "image/*");
        intent.putExtra("output", cropUri);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent,
                MeishiConfig.REQUEST_CODE_GETIMAGE_BYCROP);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        Intent intent2 = getIntent();
        String recipeName = "";
        if (intent2 != null)
        {
            Logmeishi.d("onNewIntent------------");
            bundle = intent.getExtras();
            bundle.putBoolean("from", true);// true代表来自RecipeDetailActivity
            recipeName = bundle.getString("recipe_name");
        }
        List<Fragment> list = new ArrayList<Fragment>();

        list.add(RecipeCommentFragment.getInstance(bundle));
        list.add(RecipeDetailFragment.getInstance(bundle));
        list.add(RecipeReportFragment.getInstance(bundle));

        mPagerAdapter.setFragments(list);
        mIndicator.setCurrentItem(1);
        getSupportActionBar().setTitle(recipeName);

        fragment = (RecipeReportFragment) mPagerAdapter.getItem(2);
        mDetailFragment = (RecipeDetailFragment) mPagerAdapter.getItem(1);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

}
