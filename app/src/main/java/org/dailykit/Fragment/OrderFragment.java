package org.dailykit.Fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.dailykit.Activity.DashboardActivity;
import org.dailykit.Adapter.OrderAdapter;
import org.dailykit.R;
import org.dailykit.Room.Entity.OrderEntity;
import org.dailykit.ViewModel.DashboardViewModel;

import java.util.List;


public class OrderFragment extends Fragment{

    OrderAdapter orderScreenAdapter;
    RecyclerView orderRecyclerView;
    DashboardActivity dashboardActivity;
    SwipeRefreshLayout orderSwipeRefresh;
    DashboardViewModel dashboardViewModel;
    private static final String TAG= "OrderFragment";

    public OrderFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        dashboardActivity=(DashboardActivity)getActivity();
        dashboardViewModel= ViewModelProviders.of(this).get(DashboardViewModel.class);
        orderRecyclerView=view.findViewById(R.id.order_recycler_view);
        orderSwipeRefresh=view.findViewById(R.id.order_swipe_refresh);

        orderSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dashboardViewModel.fetchOrderList(dashboardActivity);
                Toast.makeText(dashboardActivity, "Refreshing List", Toast.LENGTH_SHORT).show();
                orderSwipeRefresh.setRefreshing(false);
            }
        });
        return view;
    }

    public void updateList(List<OrderEntity> orderEntityList){
        Log.e(TAG,"Order List Count : "+orderEntityList.size());
        orderRecyclerView.setHasFixedSize(true);
        orderScreenAdapter = new OrderAdapter(getActivity(), orderEntityList);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        orderRecyclerView.setAdapter(orderScreenAdapter);
        orderScreenAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.updateList();
    }
}
