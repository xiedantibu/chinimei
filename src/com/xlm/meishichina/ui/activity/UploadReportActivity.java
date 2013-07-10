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

            // ��ȥ������������������ջ���
            mEditTextMessage.removeTextChangedListener(mTextWatcher);

            // ע������ֻ��ÿ�ζ�������EditText�������󳤶ȣ����ܶ�ɾ���ĵ����ַ��󳤶�
            // ��Ϊ����Ӣ�Ļ�ϣ������ַ����ԣ�calculateLength�������᷵��1
            while (calculateLength(s.toString()) > MAX_COUNT)
            { // �������ַ������������ƵĴ�Сʱ�����нضϲ���
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // mEditText.setText(s);�����д���ע�͵��Ͳ�����ֺ�����˵�����뷨�����ֽ����Զ���ת��������������ˣ���л@ainiyidiandian������
            mEditTextMessage.setSelection(editStart);

            // �ָ�������
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
     * ����������ݵ�������һ������=����Ӣ����ĸ��һ�����ı��=����Ӣ�ı�� ע�⣺�ú����Ĳ������ڶԵ����ַ����м��㣬��Ϊ�����ַ������������1
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
     * ��ȡ�û�����ķ�����������
     * 
     * @return
     */
    private long getInputCount()
    {
        return calculateLength(mEditTextMessage.getText().toString());
    }

    /**
     * ˢ��ʣ����������,
     */
    private void setLeftCount()
    {
        mTextViewLimit.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }
    
    
}
