package org.dailykit.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.dailykit.R;
import org.dailykit.Util.MarshMallowPermission;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_CAMERA = 1;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        int MyVersion = Build.VERSION.SDK_INT;
        MarshMallowPermission marshMallowPermission =new MarshMallowPermission(this);
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (!marshMallowPermission.checkPermissionForCamera()) {
                requestPermission();
            }
            else if(!marshMallowPermission.checkPermissionForExternalStorage()){
                requestPermission();
            }
            else{
                Log.e(TAG,"Permission already granted");
            }
        }
    }

    private void requestPermission(){
        Log.e(TAG,"Requesting Permission");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CAMERA);
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button: {
                Intent intent=new Intent(LoginActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }
}
