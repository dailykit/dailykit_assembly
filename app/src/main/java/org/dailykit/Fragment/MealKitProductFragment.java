package org.dailykit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.activity.ContinuousScanActivity;
import org.dailykit.adapter.MealKitProductCSAdapter;
import org.dailykit.adapter.ReadyToEatProductCSAdapter;
import org.dailykit.listener.ContinuousScanListener;
import org.dailykit.listener.MealKitProductListener;
import org.dailykit.viewmodel.ContinuousScanViewModel;
import org.dailykit.viewmodel.DashboardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealKitProductFragment extends Fragment implements MealKitProductListener {

    @BindView(R.id.meal_kit_list)
    RecyclerView mealKitList;
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
        setView();
        return view;
    }

    public void setView(){
        order = dashboardViewModel.getSelectedOrder();
        mealKitProductCSAdapter = new MealKitProductCSAdapter(continuousScanActivity, mealKitProductFragment, order.orderMealKitProducts());
        mealKitList.setLayoutManager( new LinearLayoutManager(continuousScanActivity, LinearLayoutManager.VERTICAL, false));
        mealKitList.setAdapter(mealKitProductCSAdapter);
        mealKitProductCSAdapter.notifyDataSetChanged();
    }
}