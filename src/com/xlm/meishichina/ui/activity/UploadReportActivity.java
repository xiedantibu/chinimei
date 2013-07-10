package com.xlm.meishichina.ui.activity;

import java.io.File;

import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.fragment.DialogsProgressDialogIndeterminateFragment;
import com.xlm.meishichina.util.ImageUtils;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.util.Toaster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class UploadReportActivity extends BaseActivity implements
        OnClickListener
{
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private Button mButtonReport;
    private TextView mTextViewLimit;
    private String uploadPath;
    private File uploadFile;
    private Bitmap uploadBitmap;
    private String id = "";
    private DialogsProgressDialogIndeterminateFragment mDialogIndeterminateFragment = null;
    private static final int MAX_COUNT = 140;

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
                case MeishiConfig.HANDLE_REQUEST_RECIPEREPUPLOADREPORT_SUCCESS:
                    Toaster.showShort(
                            UploadReportActivity.this,
                            getString(R.string.activity_upload_report_upload_success));
                    UploadReportActivity.this.setResult(RESULT_OK);
                    UploadReportActivity.this.finish();
                    break;

                case MeishiConfig.HANDLE_REQUEST_RECIPEREPUPLOADREPORT_FAIL:
                    if (msg.obj.toString().equals(
                            MeishiApplication.getApplication().getResources()
                                    .getString(R.string.login_no_user)))
                    {
                        Toaster.showShort(UploadReportActivity.this,
                                getString(R.string.login_please_first_logon));
                        Intent intent = new Intent(UploadReportActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {

                        Toaster.showShort(UploadReportActivity.this,
                                msg.obj.toString());
                    }
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);
        init();
    }

    private void init()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(
                getString(R.string.activity_upload_report_upload_title));

        mEditTextMessage = (EditText) findViewById(R.id.upload_report_message);
        mImageView = (ImageView) findViewById(R.id.upload_report_img);
        mButtonReport = (Button) findViewById(R.id.upload_report_button);
        mTextViewLimit = (TextView) findViewById(R.id.upload_report_text_limit);
        uploadPath = getIntent().getStringExtra("PATH");
        id = getIntent().getStringExtra("recipe_id");
        uploadFile = new File(uploadPath);
        Logmeishi.d("======uploadPath====" + uploadPath);
        if (!StringUtil.isEmpty(uploadPath) && uploadFile.exists())
        {
            uploadBitmap = ImageUtils.loadImgThumbnail(uploadPath, 640, 640);
        }
        if (uploadBitmap != null)
        {
            mImageView.setImageBitmap(uploadBitmap);
        }

        mImageView.setOnClickListener(this);
        mButtonReport.setOnClickListener(this);
        mEditTextMessage.addTextChangedListener(mTextWatcher);
        mEditTextMessage.setSelection(mEditTextMessage.length());
        setLeftCount();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (uploadBitmap != null || !uploadBitmap.isRecycled())
        {

            uploadBitmap.recycle();
            uploadBitmap = null;
            mImageView.setImageBitmap(null);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.upload_report_img:

                break;

            case R.id.upload_report_button:
                mDialogIndeterminateFragment = DialogsProgressDialogIndeterminateFragment
                        .getInstance(getString(R.string.yaoyiyao_fav_collect_deal_data));
                mDialogIndeterminateFragment.show(getSupportFragmentManager());
                uploadPath=ImageUtils.compressPic(uploadPath);
                HttpRequest.getInstance().recipeRequestUploadReport(
                        UploadReportActivity.this, mHandler, id.trim(),
                        mEditTextMessage.getText().toString().trim(),
                        uploadPath);
                break;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
    }

    private TextWatcher mTextWatcher = new TextWatcher()
    {

        private int editStart;

        private int editEnd;

        public void afterTextChanged(Editable s)
        {
            editStart = mEditTextMessage.getSelectionStart();
            editEnd = mEditTextMessage.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            mEditTextMessage.removeTextChangedListener(mTextWatcher);

            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MAX_COUNT)
            { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // mEditText.setText(s);将这行代码注释掉就不会出现后面所说的输入法在数字界面自动跳转回主界面的问题了，多谢@ainiyidiandian的提醒
            mEditTextMessage.setSelection(editStart);

            // 恢复监听器
            mEditTextMessage.addTextChangedListener(mTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                int after)
        {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                int count)
        {

        }

    };

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     * 
     * @param c
     * @return
     */
    private long calculateLength(CharSequence c)
    {
        double len = 0;
        for (int i = 0; i < c.length(); i++)
        {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127)
            {
                len += 0.5;
            }
            else
            {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 获取用户输入的分享内容字数
     * 
     * @return
     */
    private long getInputCount()
    {
        return calculateLength(mEditTextMessage.getText().toString());
    }

    /**
     * 刷新剩余输入字数,
     */
    private void setLeftCount()
    {
        mTextViewLimit.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }
    
    
}
