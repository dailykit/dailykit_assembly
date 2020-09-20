package org.dailykit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.adapter.OrderDetailViewPager;
import org.dailykit.adapter.TabAdapter;
import org.dailykit.listener.OrderDetailListener;
import org.dailykit.listener.OrderListener;
import org.dailykit.viewmodel.DashboardViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends CustomAppCompatActivity implements OrderListener, OrderDetailListener {

    @BindView(R.id.all)
    LinearLayout all;
    @BindView(R.id.tab_list)
    RecyclerView tabList;
    TabAdapter tabAdapter;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private OrderDetailActivity orderDetailActivity;
    DashboardViewModel dashboardViewModel;
    private OrderListSubscription.Order order;
    private OrderDetailViewPager orderDetailViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderDetailActivity = this;
        ButterKnife.bind(this);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        all.setOnClickListener(v -> {
            finish();
        });
        setView();
    }

    public void setView() {
        order = dashboardViewModel.getSelectedOrder();
        orderDetailViewPager = new OrderDetailViewPager(getSupportFragmentManager(), orderDetailActivity);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(orderDetailViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTabList(dashboardViewModel.getTabList());
    }

    @Override
    public void moveToContinuousScanActivity(OrderListSubscription.Order order) {
        dashboardViewModel.setSelectedOrder(order);
        dashboardViewModel.addToTabList((String) order.id());
        startActivity(new Intent(orderDetailActivity, ContinuousScanActivity.class));
    }

    @Override
    public void removeOrderFromList(String orderId) {
        dashboardViewModel.removeFromTabList(orderDetailActivity, orderId);
    }

    @Override
    public void updateTabList(List<String> tabModelArrayList) {
        tabList.setHasFixedSize(true);
        tabAdapter = new TabAdapter(orderDetailActivity, orderDetailActivity, tabModelArrayList);
        tabList.setLayoutManager(new LinearLayoutManager(orderDetailActivity, LinearLayoutManager.HORIZONTAL, false));
        tabList.setAdapter(tabAdapter);
        tabAdapter.notifyDataSetChanged();
    }

    @Override
    public void getOrder(String orderId) {
        dashboardViewModel.setSelectedOrder(orderId);
        dashboardViewModel.addToTabList(orderId);
        startActivity(new Intent(orderDetailActivity, OrderDetailActivity.class));
        finish();
    }

    @Override
    public OrderListSubscription.Order getSelectedOrder() {
        return dashboardViewModel.getSelectedOrder();
    }

    @Override
    public int getPageCount() {
        int count = 0;
        if(order.orderMealKitProducts().size()>0)
            count++;
        if(order.orderReadyToEatProducts().size()>0)
            count++;
        if(order.orderInventoryProducts().size()>0)
            count++;
        return count;
    }

    @Override
    public OrderListSubscription.Order getOrder() {
        return order;
    }
}