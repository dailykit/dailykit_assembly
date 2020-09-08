package org.dailykit.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.adapter.ContinuousScanIngredientAdapter;
import org.dailykit.constants.Constants;
import org.dailykit.listener.ContinuousScanListener;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.viewmodel.ContinuousScanViewModel;
import org.dailykit.viewmodel.DashboardViewModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ContinuousScanActivity extends CustomAppCompatActivity implements ContinuousScanListener {

    @BindView(R.id.barcode_scanner)
    DecoratedBarcodeView barcodeScanner;
    @BindView(R.id.order_id)
    TextView orderId;
    @BindView(R.id.pause_scan)
    Button pauseScan;
    @BindView(R.id.resume_scan)
    Button resumeScan;
    @BindView(R.id.barcodePreview)
    ImageView barcodePreview;
    private BeepManager beepManager;
    private String lastText;
    DashboardViewModel dashboardViewModel;
    ContinuousScanViewModel continuousScanViewModel;
    ContinuousScanIngredientAdapter continuousScanIngredientAdapter;
    SharedPreferences.Editor editor;
    ContinuousScanActivity continuousScanActivity;
    String slipName;
    private OrderListSubscription.Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_scan);
        ButterKnife.bind(this);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        continuousScanViewModel = ViewModelProviders.of(this).get(ContinuousScanViewModel.class);
        continuousScanActivity = this;

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeScanner.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeScanner.initializeFromIntent(getIntent());
        barcodeScanner.decodeContinuous(callback);
        pauseScan.setVisibility(View.VISIBLE);

        beepManager = new BeepManager(this);
        setView();
    }

    public void setView() {
        order = dashboardViewModel.getSelectedOrder();
        orderId.setText((String) order.id());
        updateIngredientList();
    }

    @Override
    public void updateIngredientList() {


    }

    @Override
    public void setScannedIngredientDetail() {

    }


    @Override
    protected void onResume() {
        super.onResume();

        barcodeScanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeScanner.pause();
    }

    public void pause(View view) {
        barcodeScanner.pause();
        pauseScan.setVisibility(View.GONE);
        resumeScan.setVisibility(View.VISIBLE);
    }

    public void resume(View view) {
        barcodeScanner.resume();
        pauseScan.setVisibility(View.VISIBLE);
        resumeScan.setVisibility(View.GONE);
    }

    public void triggerScan(View view) {
        barcodeScanner.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            lastText = result.getText();
            Timber.e(lastText);
            slipName = continuousScanViewModel.getIngredientSlipName(continuousScanActivity, lastText);
            editor.putString(Constants.INGREDIENT_FOUND, continuousScanViewModel.getIngredientSlipName(continuousScanActivity, lastText));
            editor.commit();
            barcodeScanner.setStatusText(slipName);

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };
}
