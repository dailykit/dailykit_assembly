package org.dailykit.fragment;


import androidx.lifecycle.ViewModelProviders;
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

import org.dailykit.activity.DashboardActivity;
import org.dailykit.adapter.OrderAdapter;
import org.dailykit.R;
import org.dailykit.room.entity.OrderEntity;
import org.dailykit.viewmodel.DashboardViewModel;

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
