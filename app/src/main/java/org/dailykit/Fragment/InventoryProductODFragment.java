package org.dailykit.fragment;

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
import org.dailykit.activity.OrderDetailActivity;
import org.dailykit.adapter.InventoryProductODAdapter;
import org.dailykit.listener.InventoryProductListener;
import org.dailykit.listener.OrderDetailListener;
import org.dailykit.viewmodel.DashboardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InventoryProductODFragment extends Fragment implements InventoryProductListener {

    @BindView(R.id.list)
    RecyclerView list;
    DashboardViewModel dashboardViewModel;
    private OrderDetailListener orderDetailListener;
    private InventoryProductODAdapter inventoryProductODAdapter;
    private OrderListDetailSubscription.Order order;
    private InventoryProductODFragment inventoryProductODFragment;
    private OrderDetailActivity orderDetailActivity;

    public InventoryProductODFragment() {

    }

    public InventoryProductODFragment(OrderDetailListener orderDetailListener) {
        this.orderDetailListener = orderDetailListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inventory_product_od, container, false);
        ButterKnife.bind(this,view);
        inventoryProductODFragment = this;
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        orderDetailActivity = (OrderDetailActivity)getActivity();
        setView();
        return view;
    }

    public void setView(){
        order = dashboardViewModel.getSelectedOrderDetail();
        inventoryProductODAdapter = new InventoryProductODAdapter(orderDetailActivity, inventoryProductODFragment, order.orderInventoryProducts());
        list.setLayoutManager( new LinearLayoutManager(orderDetailActivity, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(inventoryProductODAdapter);
        inventoryProductODAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(OrderListDetailSubscription.OrderInventoryProduct orderInventoryProduct,String message) {
        dashboardViewModel.setActiveProductTab(0);
        dashboardViewModel.setActiveMealkitTab(0);
        orderDetailActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(orderDetailActivity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}