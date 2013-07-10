package com.xlm.meishichina.ui.fragment;

import org.holoeverywhere.app.Dialog;
import org.holoeverywhere.app.DialogFragment;
import org.holoeverywhere.app.ProgressDialog;

import com.xlm.meishichina.util.MeishiConfig;

import android.os.Bundle;

public class DialogsProgressDialogIndeterminateFragment extends DialogFragment
{
    public static DialogsProgressDialogIndeterminateFragment getInstance(
            String tag)
    {
        DialogsProgressDialogIndeterminateFragment fragment = new DialogsProgressDialogIndeterminateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MeishiConfig.CONFIG_FRAGMENT_DIALOG, tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String tag = (String) getArguments().get(
                MeishiConfig.CONFIG_FRAGMENT_DIALOG);
        ProgressDialog dialog = new ProgressDialog(getSupportActivity(),
                getTheme());
        dialog.setIndeterminate(true);
        // dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(tag);
        DialogsProgressDialogIndeterminateFragment.this.setCancelable(false);
        return dialog;
    }

}
