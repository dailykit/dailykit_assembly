package org.dailykit.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import org.dailykit.R;
import org.dailykit.listener.LoginListener;
import org.dailykit.util.MarshMallowPermission;
import org.dailykit.viewmodel.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginActivity extends CustomAppCompatActivity implements LoginListener {

    public static final int REQUEST_CAMERA = 1;
    private static final String TAG = "LoginActivity";
    @BindView(R.id.login_name)
    TextInputEditText loginName;
    @BindView(R.id.login_password)
    TextInputEditText loginPassword;
    @BindView(R.id.login_button)
    Button loginButton;
    private ProgressDialog pDialog;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        pDialog = new ProgressDialog(this);
        int MyVersion = Build.VERSION.SDK_INT;
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (!marshMallowPermission.checkPermissionForCamera()) {
                requestPermission();
            } else if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                requestPermission();
            } else {
                Timber.e( "Permission already granted");
            }
        }
    }

    private void requestPermission() {
        Timber.e( "Requesting Permission");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
    }


    @Override
    public void showDialog() {
        pDialog.show();
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);
    }

    @Override
    public void dismissDialog() {
        if (null != pDialog && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    public void moveToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.login_button)
    public void onViewClicked() {
        if(loginName.getText().toString().isEmpty() || loginPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter Username and Password to login", Toast.LENGTH_SHORT).show();
        }
        else{
            loginViewModel.login(this,loginName.getText().toString(),loginPassword.getText().toString());
        }
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
