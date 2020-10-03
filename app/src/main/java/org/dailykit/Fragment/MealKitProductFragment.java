package org.dailykit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.activity.ContinuousScanActivity;
import org.dailykit.adapter.MealKitProductCSAdapter;
import org.dailykit.adapter.MealKitViewPager;
import org.dailykit.adapter.ReadyToEatProductCSAdapter;
import org.dailykit.listener.ContinuousScanListener;
import org.dailykit.listener.MealKitProductListener;
import org.dailykit.viewmodel.ContinuousScanViewModel;
import org.dailykit.viewmodel.DashboardViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealKitProductFragment extends Fragment implements MealKitProductListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private ContinuousScanListener continuousScanListener;
    DashboardViewModel dashboardViewModel;
    ContinuousScanViewModel continuousScanViewModel;
    ContinuousScanActivity continuousScanActivity;
    private OrderListSubscription.Order order;
    private MealKitProductCSAdapter mealKitProductCSAdapter;
    private MealKitProductFragment mealKitProductFragment;

    public MealKitProductFragment() {
    }

    public MealKitProductFragment(ContinuousScanListener continuousScanListener) {
        this.continuousScanListener = continuousScanListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_kit_product, container, false);
        ButterKnife.bind(this,view);
        mealKitProductFragment = this;
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        continuousScanViewModel = ViewModelProviders.of(this).get(ContinuousScanViewModel.class);
        continuousScanActivity = (ContinuousScanActivity)getActivity();
        order = dashboardViewModel.getSelectedOrder();
        setTabs(order.orderMealKitProducts());
        return view;
    }


    public void setTabs(List<OrderListSubscription.OrderMealKitProduct> orderMealKitProductList) {
        for (OrderListSubscription.OrderMealKitProduct orderMealKitProduct : orderMealKitProductList) {
            tabLayout.addTab(tabLayout.newTab().setText(orderMealKitProduct.simpleRecipeProduct().name()));
        }
        tabLayout.setTabMode(tabLayout.MODE_SCROLLABLE);
        MealKitViewPager adapter = new MealKitViewPager(getActivity(), getChildFragmentManager(), continuousScanActivity,orderMealKitProductList);
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