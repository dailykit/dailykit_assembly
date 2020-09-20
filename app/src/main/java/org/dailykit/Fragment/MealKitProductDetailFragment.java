package org.dailykit.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.activity.OrderDetailActivity;
import org.dailykit.adapter.InventoryProductODAdapter;
import org.dailykit.adapter.MealKitSachetODAdapter;
import org.dailykit.adapter.ReadyToEatProductODAdapter;
import org.dailykit.listener.MealKitProductListener;
import org.dailykit.listener.OrderDetailListener;
import org.dailykit.viewmodel.DashboardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealKitProductDetailFragment extends Fragment implements MealKitProductListener {

    private OrderDetailListener orderDetailListener;
    private OrderListSubscription.OrderMealKitProduct orderMealKitProduct;
    @BindView(R.id.list)
    RecyclerView list;
    DashboardViewModel dashboardViewModel;
    private MealKitSachetODAdapter mealKitSachetODAdapter;
    private OrderListSubscription.Order order;
    private MealKitProductDetailFragment mealKitProductDetailFragment;
    private OrderDetailActivity orderDetailActivity;

    public MealKitProductDetailFragment() {
    }

    public MealKitProductDetailFragment(OrderListSubscription.OrderMealKitProduct orderMealKitProduct,OrderDetailListener orderDetailListener) {
        this.orderMealKitProduct = orderMealKitProduct;
        this.orderDetailListener = orderDetailListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_kit_product_detail, container, false);
        ButterKnife.bind(this,view);
        mealKitProductDetailFragment = this;
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        orderDetailActivity = (OrderDetailActivity)getActivity();
        setView();
        return view;
    }

    public void setView(){
        order = dashboardViewModel.getSelectedOrder();
        mealKitSachetODAdapter = new MealKitSachetODAdapter(orderDetailActivity, mealKitProductDetailFragment, orderMealKitProduct.orderSachets());
        list.setLayoutManager( new LinearLayoutManager(orderDetailActivity, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(mealKitSachetODAdapter);
        mealKitSachetODAdapter.notifyDataSetChanged();
    }
}