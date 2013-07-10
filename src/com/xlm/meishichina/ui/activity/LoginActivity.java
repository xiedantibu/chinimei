package com.xlm.meishichina.ui.activity;

import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.CheckBox;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.LinearLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.fragment.DialogsProgressDialogIndeterminateFragment;
import com.xlm.meishichina.util.CyptoUtils;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;

public class LoginActivity extends BaseActivity implements OnClickListener,
        OnGestureListener
{

    private ActionBar mActionBar;
    /**
     * mEditTextUserName £ºÓÃ»§Ãû mEditTextPassword £ºÃÜÂë
     */
    private EditText mEditTextUserName, mEditTextPassword;
    /**
     * mCheckBoxSavePassword ±£´æÃÜÂë
     */
    private CheckBox mCheckBoxSavePassword;
    /**
     * mButtonRegist ×¢²á mButtonLogon µÇÂ¼
     */
    private Button mButtonRegist, mButtonLogon;
    /**
     * mInputMethodManager Èí¼üÅÌ¹ÜÀí
     */
    private InputMethodManager mInputMethodManager;

    /**
     * mDialogIndeterminateFragment dialog
     */
    private DialogsProgressDialogIndeterminateFragment mDialogIndeterminateFragment;

    // private LinearLayout mLinearLayoutRoot;
    private GestureDetector gestureDetector;
    private boolean isFromSetting = false;
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
                case MeishiConfig.HANDLE_REQUEST_LOGIN_SUCCESS:
                    Toaster.showShort(LoginActivity.this,
                            getString(R.string.login_msg_success));
                    SharedPreferences preference = MeishiApplication
                            .getApplication().getSharedPreferences();
                    SharedPreferences.Editor edit = preference.edit();
                    if (mCheckBoxSavePassword.isChecked())
                    {
                        edit.putString(MeishiConfig.CONFIG_SP_LOGIN_NAME,
                                CyptoUtils.encode(
                                        MeishiConfig.CONFIG_SP_LOGIN_NAME,
                                        mEditTextUserName.getText().toString()
                                                .trim()));
                        edit.putString(MeishiConfig.CONFIG_SP_LOGIN_PWD,
                                CyptoUtils.encode(
                                        MeishiConfig.CONFIG_SP_LOGIN_PWD,
                                        mEditTextPassword.getText().toString()
                                                .trim()));
                        edit.putBoolean(
                                MeishiConfig.CONFIG_SP_LOGIN_IS_REMEMBER_PWD,
                                true);
                    }
                    else
                    {
                        edit.putString(MeishiConfig.CONFIG_SP_LOGIN_NAME, "");
                        edit.putString(MeishiConfig.CONFIG_SP_LOGIN_PWD, "");
                        edit.putBoolean(
                                MeishiConfig.CONFIG_SP_LOGIN_IS_REMEMBER_PWD,
                                false);
                    }
                    edit.commit();
                    if (isFromSetting)
                    {
                        setResult(RESULT_OK);
                        isFromSetting = false;
                    }
                    LoginActivity.this.finish();

                    break;
                case MeishiConfig.HANDLE_REQUEST_LOGIN_FAIL:
                    Toaster.showShort(LoginActivity.this, (String) msg.obj);
                    MeishiApplication.getApplication().clearUser();
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init()
    {
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);

        // mLinearLayoutRoot = (LinearLayout) findViewById(R.id.loginRoot);
        mEditTextUserName = (EditText) findViewById(R.id.loginusername);
        mEditTextPassword = (EditText) findViewById(R.id.loginpassword);
        mCheckBoxSavePassword = (CheckBox) findViewById(R.id.switch_password);
        mButtonRegist = (Button) findViewById(R.id.do_register_text);
        mButtonLogon = (Button) findViewById(R.id.do_login_button);

        isFromSetting = getIntent().getBooleanExtra("from", false);

        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        gestureDetector = new GestureDetector(this);
        SharedPreferences sharedPreferences = MeishiApplication
                .getApplication().getSharedPreferences();
        if (sharedPreferences.getBoolean(
                MeishiConfig.CONFIG_SP_LOGIN_IS_REMEMBER_PWD, false))
        {
            mEditTextUserName.setText(CyptoUtils.decode(
                    MeishiConfig.CONFIG_SP_LOGIN_NAME, sharedPreferences
                            .getString(MeishiConfig.CONFIG_SP_LOGIN_NAME, "")));
            mEditTextPassword.setText(CyptoUtils.decode(
                    MeishiConfig.CONFIG_SP_LOGIN_PWD, sharedPreferences
                            .getString(MeishiConfig.CONFIG_SP_LOGIN_PWD, "")));
        }

        mButtonLogon.setOnClickListener(this);
        mButtonRegist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.do_register_text:
                Intent intent = new Intent(LoginActivity.this,
                        RegistActivity.class);
                startActivityForResult(intent,
                        MeishiConfig.HANDLE_REQUEST_LOGIN_SUCCESS);
                break;
            case R.id.do_login_button:

                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                        0);
                if (mEditTextUserName.getText().toString().trim().equals(""))
                {
                    mEditTextUserName.requestFocus();
                    Toaster.showShort(this,
                            getString(R.string.login_please_input_name));
                }
                else if (mEditTextPassword.getText().toString().trim()
                        .equals(""))
                {
                    mEditTextPassword.requestFocus();
                    Toaster.showShort(this,
                            getString(R.string.login_please_input_pwd));

                }
                else
                {
                    mDialogIndeterminateFragment = DialogsProgressDialogIndeterminateFragment
                            .getInstance(getString(R.string.login_now_ing));
                    mDialogIndeterminateFragment
                            .show(getSupportFragmentManager());
                    HttpRequest.getInstance().loginRequest(LoginActivity.this,
                            mHandler,
                            (mEditTextUserName.getText()).toString().trim(),
                            (mEditTextPassword.getText()).toString().trim());
                }
                break;
        }
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
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent paramMotionEvent)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent paramMotionEvent)
    {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent paramMotionEvent)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent paramMotionEvent1,
            MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent paramMotionEvent)
    {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY)
    {

        if (e2.getX() - e1.getX() > 200 && Math.abs(velocityX) > 100)
        {
            LoginActivity.this.finish();
            return true;
        }
        return false;
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
            case MeishiConfig.HANDLE_REQUEST_LOGIN_SUCCESS:
                if (isFromSetting)
                {
                    setResult(RESULT_OK);
                    isFromSetting = false;
                }
                LoginActivity.this.finish();
                break;
        }
    }
}
