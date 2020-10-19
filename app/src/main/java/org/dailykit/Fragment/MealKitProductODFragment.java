package org.dailykit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.dailykit.OrderListDetailSubscription;
import org.dailykit.R;
import org.dailykit.activity.OrderDetailActivity;
import org.dailykit.adapter.MealKitViewPager;
import org.dailykit.listener.OrderDetailListener;
import org.dailykit.viewmodel.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MealKitProductODFragment extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private OrderDetailListener orderDetailListener;
    private OrderDetailActivity orderDetailActivity;
    DashboardViewModel dashboardViewModel;
    private OrderListDetailSubscription.Order order;

    public MealKitProductODFragment() {

    }

    public MealKitProductODFragment(OrderDetailListener orderDetailListener) {
        this.orderDetailListener = orderDetailListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_kit_product_od, container, false);
        ButterKnife.bind(this,view);
        orderDetailActivity = (OrderDetailActivity)getActivity();
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        order = dashboardViewModel.getSelectedOrderDetail();
        setTabs(order.orderMealKitProducts());
        return view;
    }

    public void setTabs(List<OrderListDetailSubscription.OrderMealKitProduct> orderMealKitProductList) {
        for (OrderListDetailSubscription.OrderMealKitProduct orderMealKitProduct : orderMealKitProductList) {
            tabLayout.addTab(tabLayout.newTab().setText(orderMealKitProduct.simpleRecipeProduct().name()));
        }
        tabLayout.setTabMode(tabLayout.MODE_SCROLLABLE);
        MealKitViewPager adapter = new MealKitViewPager(getActivity(), getChildFragmentManager(), orderDetailListener,orderMealKitProductList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }
}