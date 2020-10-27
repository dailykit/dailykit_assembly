package org.dailykit.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListDetailSubscription;
import org.dailykit.R;
import org.dailykit.activity.ContinuousScanActivity;
import org.dailykit.adapter.InventoryProductODAdapter;
import org.dailykit.listener.ContinuousScanListener;
import org.dailykit.listener.InventoryProductListener;
import org.dailykit.viewmodel.ContinuousScanViewModel;
import org.dailykit.viewmodel.DashboardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryProductFragment extends Fragment implements InventoryProductListener {

    @BindView(R.id.inventory_list)
    RecyclerView inventoryList;
    private ContinuousScanListener continuousScanListener;
    DashboardViewModel dashboardViewModel;
    ContinuousScanViewModel continuousScanViewModel;
    ContinuousScanActivity continuousScanActivity;
    private OrderListDetailSubscription.Order order;
    private InventoryProductODAdapter inventoryProductCSAdapter;
    private InventoryProductFragment inventoryProductFragment;
    private Activity activity;

    public InventoryProductFragment() {
    }

    public InventoryProductFragment(ContinuousScanListener continuousScanListener) {
        this.continuousScanListener = continuousScanListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_product, container, false);
        ButterKnife.bind(this,view);
        inventoryProductFragment = this;
        activity = getActivity();
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        continuousScanViewModel = ViewModelProviders.of(this).get(ContinuousScanViewModel.class);
        continuousScanActivity = (ContinuousScanActivity)getActivity();
        setView();
        return view;
    }

    public void setView(){
        order = dashboardViewModel.getSelectedOrderDetail();
        inventoryProductCSAdapter = new InventoryProductODAdapter(continuousScanActivity, inventoryProductFragment, order.orderInventoryProducts());
        inventoryList.setLayoutManager( new LinearLayoutManager(continuousScanActivity, LinearLayoutManager.VERTICAL, false));
        inventoryList.setAdapter(inventoryProductCSAdapter);
        inventoryProductCSAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(OrderListDetailSubscription.OrderInventoryProduct orderInventoryProduct,String message) {
        dashboardViewModel.setActiveProductTab(0);
        dashboardViewModel.setActiveMealkitTab(0);
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}