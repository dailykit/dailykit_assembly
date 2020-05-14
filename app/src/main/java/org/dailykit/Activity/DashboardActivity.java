package org.dailykit.activity;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.dailykit.listener.DashboardListener;
import org.dailykit.fragment.IngredientFragment;
import org.dailykit.fragment.LabelFragment;
import org.dailykit.fragment.MenuFragment;
import org.dailykit.fragment.NotificationFragment;
import org.dailykit.fragment.OrderFragment;
import org.dailykit.fragment.ScanFragment;
import org.dailykit.R;
import org.dailykit.util.FragmentConstants;
import org.dailykit.viewmodel.DashboardViewModel;

public class DashboardActivity extends AppCompatActivity implements DashboardListener {

    private TextView mTextMessage;
    DashboardViewModel dashboardViewModel;
    private FragmentManager myFragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static final String TAG= "DashboardActivity";
    MenuFragment menuFragment;
    ScanFragment scanFragment;
    OrderFragment orderFragment;
    LabelFragment labelFragment;
    NotificationFragment notificationFragment;
    IngredientFragment ingredientFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_menu:
                    switchFragments(menuFragment,FragmentConstants.MENU);
                    return true;
                case R.id.navigation_scan:
                    switchFragments(scanFragment,FragmentConstants.SCAN);
                    return true;
                case R.id.navigation_order:
                    switchFragments(orderFragment,FragmentConstants.ORDER);
                    return true;
                case R.id.navigation_label:
                    switchFragments(labelFragment,FragmentConstants.LABEL);
                    return true;
                case R.id.navigation_notifications:
                    switchFragments(notificationFragment,FragmentConstants.NOTIFICATION);
                    return true;
            }
            return false;
        }
    };

    public void switchFragments(Fragment fragment,String fragmentTitle){
        fragmentTransaction = myFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dashboard_frame_layout,fragment,fragmentTitle);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dashboardViewModel= ViewModelProviders.of(this).get(DashboardViewModel.class);
        mTextMessage = (TextView) findViewById(R.id.message);
        myFragmentManager = getSupportFragmentManager();
        menuFragment=new MenuFragment();
        scanFragment=new ScanFragment();
        orderFragment=new OrderFragment();
        labelFragment=new LabelFragment();
        notificationFragment=new NotificationFragment();
        ingredientFragment=new IngredientFragment();
        fragmentTransaction = myFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dashboard_frame_layout, orderFragment, FragmentConstants.ORDER);
        fragmentTransaction.commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_order);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dashboardViewModel.fetchOrderList(this);
        dashboardViewModel.showScanToast();
    }

    @Override
    public void updateList() {
        orderFragment.updateList(dashboardViewModel.fetchOrderList());
    }

    @Override
    public void updateIngredientList() {
        ingredientFragment.updateIngredientList();
    }

    public void switchToIngredientFragment(){
        switchFragments(ingredientFragment,FragmentConstants.INGREDIENT);
    }

    public void switchToOrderFragment(){
        switchFragments(orderFragment,FragmentConstants.ORDER);
    }
}
