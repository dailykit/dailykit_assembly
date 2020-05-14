package org.dailykit.activity;

import android.Manifest;
import android.app.ProgressDialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import org.dailykit.listener.ScanListener;
import org.dailykit.util.AppUtil;
import org.dailykit.util.Constants;
import org.dailykit.viewmodel.ScanViewModel;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, ScanListener {

    public static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    public static final String TAG = "ScanActivity";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog pDialog;
    boolean isScanning=false;
    ScanViewModel scanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView= new ZXingScannerView(this);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        scanViewModel = ViewModelProviders.of(this).get(ScanViewModel.class);
        pDialog = new ProgressDialog(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            if(!checkPermission()){
                requestPermission();
            }
        }
        setContentView(scannerView);
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CAMERA);
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult=result.getText();
        Log.e(TAG,isScanning+" "+scanResult);
        Log.e(TAG,"Found : "+scanResult);
        scanViewModel.getIngredientSlipName(this,scanResult);
        editor.putString(Constants.INGREDIENT_FOUND, scanViewModel.getIngredientSlipName(this,scanResult));
        editor.commit();
        if(!isScanning) {
            isScanning=true;
        }
        scannerView.resumeCameraPreview(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView==null){
                    scannerView=new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();

            }
            else {
                requestPermission();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    @Override
    public void finishActivity(){
        ScanActivity.this.finish();
    }
}
