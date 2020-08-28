package org.dailykit.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import org.dailykit.R;
import org.dailykit.listener.SplashListener;
import org.dailykit.network.Network;
import org.dailykit.viewmodel.SplashViewModel;

public class SplashActivity extends CustomAppCompatActivity implements SplashListener {

    SplashViewModel splashViewModel;
    Intent intent;
    private Network network;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        network = new Network();
        network.setApolloClient(this);
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
}
