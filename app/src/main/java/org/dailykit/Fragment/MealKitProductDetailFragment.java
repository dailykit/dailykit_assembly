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
import org.dailykit.adapter.MealKitSachetODAdapter;
import org.dailykit.listener.MealKitProductListener;
import org.dailykit.listener.OrderDetailListener;
import org.dailykit.viewmodel.DashboardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MealKitProductDetailFragment extends Fragment implements MealKitProductListener {

    private OrderDetailListener orderDetailListener;
    private OrderListDetailSubscription.OrderMealKitProduct orderMealKitProduct;
    @BindView(R.id.list)
    RecyclerView list;
    DashboardViewModel dashboardViewModel;
    private MealKitSachetODAdapter mealKitSachetODAdapter;
    private OrderListDetailSubscription.Order order;
    private MealKitProductDetailFragment mealKitProductDetailFragment;
    private Activity orderDetailActivity;
    private int position;

    public MealKitProductDetailFragment() {
    }

    public MealKitProductDetailFragment(int position,OrderListDetailSubscription.OrderMealKitProduct orderMealKitProduct,OrderDetailListener orderDetailListener) {
        this.orderMealKitProduct = orderMealKitProduct;
        this.orderDetailListener = orderDetailListener;
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_kit_product_detail, container, false);
        ButterKnife.bind(this,view);
        mealKitProductDetailFragment = this;
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        orderDetailActivity = (Activity)getActivity();
        setView();
        return view;
    }

    public void setView(){
        if(null !=  orderMealKitProduct && null !=  orderMealKitProduct.orderSachets()) {
            order = dashboardViewModel.getSelectedOrderDetail();
            mealKitSachetODAdapter = new MealKitSachetODAdapter(orderDetailActivity, mealKitProductDetailFragment, orderMealKitProduct.orderSachets());
            list.setLayoutManager(new LinearLayoutManager(orderDetailActivity, LinearLayoutManager.VERTICAL, false));
            list.setAdapter(mealKitSachetODAdapter);
            mealKitSachetODAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResponse(OrderListDetailSubscription.OrderSachet orderSachet,String message) {
        int index = 0;
        if(dashboardViewModel.getSelectedOrderDetail().orderInventoryProducts().size()>0) {
            index++;
        }
        if(dashboardViewModel.getSelectedOrderDetail().orderMealKitProducts().size()>0){
            index++;
        }
        Timber.e("Index : "+index);
        dashboardViewModel.setActiveProductTab(index);
        dashboardViewModel.setActiveMealkitTab(position);
        orderDetailActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(orderDetailActivity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}