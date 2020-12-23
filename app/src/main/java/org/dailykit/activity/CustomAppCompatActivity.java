package org.dailykit.activity;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import org.dailykit.R;
import org.dailykit.listener.CommonListener;


public class CustomAppCompatActivity extends AppCompatActivity implements CommonListener {

    private ProgressDialog pDialog;

    @Override
    public void showDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.show();
        pDialog.setMessage(getResources().getString(R.string.please_wait));
        pDialog.setCancelable(false);
    }

    @Override
    public void dismissDialog() {
        if (null != pDialog && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    public void logout() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissDialog();
    }

}
