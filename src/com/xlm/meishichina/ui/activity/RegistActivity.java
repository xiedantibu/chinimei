package com.xlm.meishichina.ui.activity;

import java.util.regex.Pattern;

import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.fragment.DialogsProgressDialogIndeterminateFragment;
import com.xlm.meishichina.util.Toaster;

import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

public class RegistActivity extends BaseActivity implements OnClickListener,
        OnGestureListener
{
    private ActionBar mActionBar;
    private EditText mEditTextUserName;
    private EditText mEditTextPassWord;
    private EditText mEditTextMail;
    private Button mButtonRegist;
    /**
     * mInputMethodManager Èí¼üÅÌ¹ÜÀí
     */
    private InputMethodManager mInputMethodManager;
    private GestureDetector gestureDetector;
    /**
     * mDialogIndeterminateFragment dialog
     */
    private DialogsProgressDialogIndeterminateFragment mDialogIndeterminateFragment;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            if (mDialogIndeterminateFragment != null)
            {
                mDialogIndeterminateFragment.dismissAllowingStateLoss();
            }
            switch (msg.what)
            {
                case HANDLE_REQUEST_REGIST_SUCCESS:
                    Toaster.showShort(RegistActivity.this,
                            getString(R.string.regist_success));
                    RegistActivity.this.setResult(RESULT_OK);
                    RegistActivity.this.finish();
                    break;

                case HANDLE_REQUEST_REGIST_FAIL:
                    Toaster.showShort(RegistActivity.this, (String) msg.obj);
                    MeishiApplication.getApplication().clearUser();
                    break;
            }

        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registed);
        init();
    }

    private void init()
    {
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setTitle(MeishiApplication.getApplication().getString(
                R.string.regist_title));
        
        gestureDetector = new GestureDetector(this);

        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mEditTextUserName = (EditText) findViewById(R.id.registusername);
        mEditTextPassWord = (EditText) findViewById(R.id.registpassword);
        mEditTextMail = (EditText) findViewById(R.id.registmail);
        mButtonRegist = (Button) findViewById(R.id.do_register);

        mButtonRegist.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                View view = getWindow().peekDecorView();
                if (view != null)
                {

                    InputMethodManager inputmanger = (InputMethodManager) getSystemService("input_method");
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
                            0);
                }
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.do_register:
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                if (mEditTextUserName.getText().toString().trim().equals(""))
                {
                    mEditTextUserName.requestFocus();
                    Toaster.showShort(this,
                            getString(R.string.login_please_input_name));
                }
                else if (mEditTextPassWord.getText().toString().trim()
                        .equals(""))
                {
                    mEditTextPassWord.requestFocus();
                    Toaster.showShort(this,
                            getString(R.string.login_please_input_pwd));

                }
                else if (mEditTextMail.getText().toString().trim().equals(""))
                {
                    mEditTextMail.requestFocus();
                    Toaster.showShort(this, getString(R.string.regist_mail_tag));

                }
                else if (!Pattern
                        .compile(
                                "^[_\\.0-9a-zA-Z+-]+@([0-9a-zA-Z]+[0-9a-zA-Z-]*\\.)+[a-zA-Z]{2,4}$")
                        .matcher(mEditTextMail.getText().toString().trim())
                        .find())
                {
                    mEditTextMail.requestFocus();
                    Toaster.showShort(this,
                            getString(R.string.regist_mail_false_tag));
                }
                else
                {
                    mDialogIndeterminateFragment = DialogsProgressDialogIndeterminateFragment
                            .getInstance(getString(R.string.regist_ing));
                    mDialogIndeterminateFragment
                            .show(getSupportFragmentManager());
                    HttpRequest.getInstance().registRequest(
                            RegistActivity.this, mHandler,
                            mEditTextUserName.getText().toString().trim(),
                            mEditTextPassWord.getText().toString().trim(),
                            mEditTextMail.getText().toString().trim());
                }
                break;

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent arg0)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY)
    {
        if (e2.getX() - e1.getX() > 200 && Math.abs(velocityX) > 100)
        {
            RegistActivity.this.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
