package org.dailykit.Activity;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.dailykit.Callback.SplashListener;
import org.dailykit.R;
import org.dailykit.ViewModel.SplashViewModel;

public class SplashActivity extends AppCompatActivity implements SplashListener {

    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashViewModel= ViewModelProviders.of(this).get(SplashViewModel.class);
        //splashViewModel.fetchOrderList(this);
        moveToNextScreen();

    }

    @Override
    public void moveToNextScreen() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
