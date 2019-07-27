package org.dailykit.Activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.dailykit.Adapter.ContinuousScanIngredientAdapter;
import org.dailykit.Callback.ContinuousScanListener;
import org.dailykit.R;
import org.dailykit.Room.Entity.ItemEntity;
import org.dailykit.Util.AppUtil;
import org.dailykit.Util.Constants;
import org.dailykit.ViewModel.ContinuousScanViewModel;
import org.dailykit.ViewModel.DashboardViewModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ContinuousScanActivity extends AppCompatActivity implements ContinuousScanListener {

    private static final String TAG = ContinuousScanActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Button pauseScan , resumeScan;
    RecyclerView ingredientDetailList;
    RecyclerView.SmoothScroller smoothScroller;
    DashboardViewModel dashboardViewModel;
    ContinuousScanViewModel continuousScanViewModel;
    ContinuousScanIngredientAdapter continuousScanIngredientAdapter;
    ItemEntity itemEntity;
    TextView packingStatus,itemName,serving;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ContinuousScanActivity continuousScanActivity;
    String slipName;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            lastText = result.getText();
            Log.e(TAG,lastText);
            slipName = continuousScanViewModel.getIngredientSlipName(continuousScanActivity,lastText);
            editor.putString(Constants.INGREDIENT_FOUND, continuousScanViewModel.getIngredientSlipName(continuousScanActivity,lastText));
            editor.commit();
            barcodeView.setStatusText(slipName);

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_scan);
        dashboardViewModel= ViewModelProviders.of(this).get(DashboardViewModel.class);
        continuousScanViewModel= ViewModelProviders.of(this).get(ContinuousScanViewModel.class);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        continuousScanActivity=this;
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        pauseScan=(Button)findViewById(R.id.pause_scan);
        resumeScan=(Button)findViewById(R.id.resume_scan);
        ingredientDetailList =findViewById(R.id.ingredient_detail_list);
        packingStatus=findViewById(R.id.ingredient_packing_status);
        itemName=findViewById(R.id.ingredient_item_name);
        serving=findViewById(R.id.ingredient_serving);
        smoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        pauseScan.setVisibility(View.VISIBLE);

        beepManager = new BeepManager(this);
        updateUI();
    }

    public void updateUI(){
        itemEntity=dashboardViewModel.getCurrentItemEntity();
        packingStatus.setText(continuousScanViewModel.getItemPackStatus(itemEntity));
        serving.setText(itemEntity.getItemServing());
        itemName.setText(itemEntity.getItemName());
        updateIngredientList();
    }

    @Override
    public void updateIngredientList(){
        itemEntity = dashboardViewModel.getCurrentItemEntity();
        ingredientDetailList.invalidate();
        continuousScanIngredientAdapter = new ContinuousScanIngredientAdapter(this, dashboardViewModel.getIngredientEntityList(), itemEntity,dashboardViewModel.getNumberOfIngredientsPacked());
        ingredientDetailList.setLayoutManager(null);
        ingredientDetailList.setAdapter(null);
        ingredientDetailList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ingredientDetailList.setAdapter(continuousScanIngredientAdapter);
        continuousScanIngredientAdapter.notifyDataSetChanged();
        if(itemEntity.getSelectedPosition()!=-1) {
            smoothScroller.setTargetPosition(itemEntity.getSelectedPosition());
            ingredientDetailList.getLayoutManager().startSmoothScroll(smoothScroller);
        }
    }

    @Override
    public void setScannedIngredientDetail() {

    }


    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
        pauseScan.setVisibility(View.GONE);
        resumeScan.setVisibility(View.VISIBLE);
    }

    public void resume(View view) {
        barcodeView.resume();
        pauseScan.setVisibility(View.VISIBLE);
        resumeScan.setVisibility(View.GONE);
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
