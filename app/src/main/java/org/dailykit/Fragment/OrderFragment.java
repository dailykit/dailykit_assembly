package org.dailykit.fragment;


import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.dailykit.OrderListSubscription;
import org.dailykit.activity.ContinuousScanActivity;
import org.dailykit.activity.DashboardActivity;
import org.dailykit.adapter.OrderAdapter;
import org.dailykit.R;
import org.dailykit.listener.OrderListener;
import org.dailykit.room.entity.OrderEntity;
import org.dailykit.viewmodel.DashboardViewModel;

import java.util.List;

import timber.log.Timber;


public class OrderFragment extends Fragment implements OrderListener {

    OrderAdapter orderScreenAdapter;
    RecyclerView orderRecyclerView;
    DashboardActivity dashboardActivity;
    SwipeRefreshLayout orderSwipeRefresh;
    DashboardViewModel dashboardViewModel;
    OrderFragment orderFragment;

    private static final String TAG= "OrderFragment";

    public OrderFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        dashboardActivity=(DashboardActivity)getActivity();
        orderFragment = this;
        dashboardViewModel= ViewModelProviders.of(this).get(DashboardViewModel.class);
        orderRecyclerView=view.findViewById(R.id.order_recycler_view);
        orderSwipeRefresh=view.findViewById(R.id.order_swipe_refresh);

        orderSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(dashboardActivity, "Refreshing List", Toast.LENGTH_SHORT).show();
                orderSwipeRefresh.setRefreshing(false);
                dashboardActivity.subscribeOrders();
            }
        });
        return view;
    }

    public void updateList(List<OrderListSubscription.Order> orderEntityList){
        Timber.e("Order List Count : "+orderEntityList.size());
        orderRecyclerView.setHasFixedSize(true);
        orderScreenAdapter = new OrderAdapter(dashboardActivity,orderFragment, orderEntityList);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        orderRecyclerView.setAdapter(orderScreenAdapter);
        orderScreenAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void moveToContinuousScanActivity(OrderListSubscription.Order order) {
        dashboardViewModel.setSelectedOrder(order);
        startActivity(new Intent(dashboardActivity, ContinuousScanActivity.class));
    }
}
