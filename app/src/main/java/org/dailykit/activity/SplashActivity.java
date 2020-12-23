package org.dailykit.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import org.dailykit.R;
import org.dailykit.listener.SplashListener;
import org.dailykit.network.Network;
import org.dailykit.viewmodel.SplashViewModel;

import timber.log.Timber;

public class SplashActivity extends CustomAppCompatActivity implements SplashListener {

    SplashViewModel splashViewModel;
    Intent intent;
    private Network network;
    private int CAMERA_PERMISSION_REQUEST = 10012;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashViewModel= ViewModelProviders.of(this).get(SplashViewModel.class);
        splashViewModel.setBaseUrl("test.dailykit.org");
        splashViewModel.setClientSecret("60ea76ab-5ab6-4f09-ad44-efeb00f978ce");
        network = new Network();
        network.setApolloClient(this,splashViewModel.getBaseUrl(),splashViewModel.getClientSecret());
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            moveToNextScreen();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.CAMERA)) {
                Toast.makeText(SplashActivity.this, getResources().getString(R.string.camera_setting_alert), Toast.LENGTH_SHORT).show();
                moveToNextScreen();
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST);
            }
        }
    }

    @Override
    public void moveToNextScreen() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(splashViewModel.isUserLoggedIn()){
                    intent=new Intent(SplashActivity.this,DashboardActivity.class);
                }
                else{
                    intent=new Intent(SplashActivity.this,DashboardActivity.class);
                }
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 1000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Timber.e("onRequestPermissionsResult");
        moveToNextScreen();
    }
}
