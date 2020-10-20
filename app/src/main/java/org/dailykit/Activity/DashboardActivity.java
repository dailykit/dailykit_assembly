package org.dailykit.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.constants.FragmentConstants;
import org.dailykit.fragment.IngredientFragment;
import org.dailykit.fragment.LabelFragment;
import org.dailykit.fragment.MenuFragment;
import org.dailykit.fragment.NotificationFragment;
import org.dailykit.fragment.OrderFragment;
import org.dailykit.fragment.ScanFragment;
import org.dailykit.listener.DashboardListener;
import org.dailykit.network.Network;
import org.dailykit.viewmodel.DashboardViewModel;
import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class DashboardActivity extends CustomAppCompatActivity implements DashboardListener {

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
    private OrderListSubscription orderListSubscription;
    private ApolloSubscriptionCall<OrderListSubscription.Data> apolloSubscriptionCall= null;

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
        subscribeOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dashboardViewModel.showScanToast();
        dashboardViewModel.setActiveMealkitTab(0);
        dashboardViewModel.setActiveProductTab(0);
    }

    @Override
    public void updateList() {

    }

    @Override
    public void updateIngredientList() {

    }

    public void switchToIngredientFragment(){
        switchFragments(ingredientFragment,FragmentConstants.INGREDIENT);
    }

    public void switchToOrderFragment(){
        switchFragments(orderFragment,FragmentConstants.ORDER);
    }



    public void subscribeOrders(){
        showDialog();
        orderListSubscription = OrderListSubscription.builder().build();
        if(null != Network.apolloClient) {
            apolloSubscriptionCall = Network.apolloClient.subscribe(orderListSubscription);
            Timber.e("subscribeOrders");
            apolloSubscriptionCall.execute(new ApolloSubscriptionCall.Callback<OrderListSubscription.Data>() {
                @Override
                public void onResponse(@NotNull Response<OrderListSubscription.Data> response) {
                    dismissDialog();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            dashboardViewModel.setOrderList(response.getData().orders());
                            orderFragment.updateList(response.data().orders());
                        }
                    });
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    Timber.e("onFailure : " + e.getLocalizedMessage());
                    Timber.e("onFailure : " + e.toString());
                    dismissDialog();
                    e.printStackTrace();
                }

                @Override
                public void onCompleted() {
                    Timber.e("onCompleted");
                    dismissDialog();
                }

                @Override
                public void onTerminated() {
                    Timber.e("onTerminated");
                    dismissDialog();
                }

                @Override
                public void onConnected() {
                    Timber.e("onConnected");
                }
            });
        }
        else{
            dismissDialog();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(null != apolloSubscriptionCall){
            apolloSubscriptionCall.cancel();;
        }
    }
}
