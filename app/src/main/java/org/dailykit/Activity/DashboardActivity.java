package org.dailykit.Activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import org.dailykit.Callback.DashboardListener;
import org.dailykit.Fragment.IngredientFragment;
import org.dailykit.Fragment.LabelFragment;
import org.dailykit.Fragment.MenuFragment;
import org.dailykit.Fragment.NotificationFragment;
import org.dailykit.Fragment.OrderFragment;
import org.dailykit.Fragment.ScanFragment;
import org.dailykit.R;
import org.dailykit.Util.FragmentConstants;
import org.dailykit.ViewModel.DashboardViewModel;

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
