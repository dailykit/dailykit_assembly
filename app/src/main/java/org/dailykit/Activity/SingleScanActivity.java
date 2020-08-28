package org.dailykit.activity;

import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.dailykit.listener.ContinuousScanListener;
import org.dailykit.model.ScanIngredientDataModel;
import org.dailykit.R;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.util.AppUtil;
import org.dailykit.util.Constants;
import org.dailykit.viewmodel.ContinuousScanViewModel;
import org.dailykit.viewmodel.DashboardViewModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;

public class SingleScanActivity extends CustomAppCompatActivity implements ContinuousScanListener {

    private static final String TAG = SingleScanActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    TextView packingStatus,itemName,serving,ingredientName,ingredientProcessing,ingredientWeight,scannedSuccessfully,scanningInProgress;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    SingleScanActivity singleScanActivity;
    String slipName;
    DashboardViewModel dashboardViewModel;
    ContinuousScanViewModel continuousScanViewModel;
    ItemEntity itemEntity;
    LinearLayout scannedLayout,back;
    FrameLayout backLayout;
    ScanIngredientDataModel scanIngredientDataModel;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            Timber.e(lastText);
            slipName = continuousScanViewModel.getIngredientSlipName(singleScanActivity,lastText);
            editor.putString(Constants.INGREDIENT_FOUND, continuousScanViewModel.getIngredientSlipName(singleScanActivity,lastText));
            editor.commit();
            barcodeView.setStatusText(slipName);

            beepManager.playBeepSoundAndVibrate();

            barcodeView.pause();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_scan);
        singleScanActivity=this;
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();
        dashboardViewModel= ViewModelProviders.of(this).get(DashboardViewModel.class);
        continuousScanViewModel= ViewModelProviders.of(this).get(ContinuousScanViewModel.class);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        packingStatus=findViewById(R.id.ingredient_packing_status);
        itemName=findViewById(R.id.ingredient_item_name);
        serving=findViewById(R.id.ingredient_serving);
        ingredientName=findViewById(R.id.ingredient_name);
        ingredientWeight=findViewById(R.id.ingredient_weight);
        ingredientProcessing=findViewById(R.id.ingredient_processing);
        scannedSuccessfully=findViewById(R.id.scanned_successfully);
        scanningInProgress=findViewById(R.id.scanning_in_progress);
        scannedLayout=findViewById(R.id.scanned_layout);
        back=findViewById(R.id.back);
        backLayout=findViewById(R.id.back_layout);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);
        updateUI();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void updateUI(){
        itemEntity=dashboardViewModel.getCurrentItemEntity();
        scanIngredientDataModel=continuousScanViewModel.getScanIngredient();
        packingStatus.setText(continuousScanViewModel.getItemPackStatus(itemEntity));
        serving.setText(itemEntity.getItemServing());
        itemName.setText(itemEntity.getItemName());
        ingredientName.setText(scanIngredientDataModel.getIngredientName());
        ingredientWeight.setText(scanIngredientDataModel.getIngredientWeight());
        ingredientProcessing.setText(scanIngredientDataModel.getIngredientProcessing());
    }

    @Override
    public void setScannedIngredientDetail(){
        scannedSuccessfully.setVisibility(View.VISIBLE);
        scanningInProgress.setVisibility(View.GONE);
        backLayout.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void updateIngredientList() {

    }
}
