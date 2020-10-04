package org.dailykit.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.activity.DashboardActivity;
import org.dailykit.activity.OrderDetailActivity;
import org.dailykit.adapter.OrderAdapter;
import org.dailykit.adapter.TabAdapter;
import org.dailykit.listener.OrderListener;
import org.dailykit.viewmodel.DashboardViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class OrderFragment extends Fragment implements OrderListener {

    OrderAdapter orderScreenAdapter;
    TabAdapter tabAdapter;
    DashboardActivity dashboardActivity;
    DashboardViewModel dashboardViewModel;
    OrderFragment orderFragment;
    private static final String TAG = "OrderFragment";
    @BindView(R.id.all)
    LinearLayout all;
    @BindView(R.id.tab_list)
    RecyclerView tabList;
    @BindView(R.id.order_recycler_view)
    RecyclerView orderRecyclerView;
    @BindView(R.id.order_swipe_refresh)
    SwipeRefreshLayout orderSwipeRefresh;
    @BindView(R.id.all_text)
    TextView allText;

    public OrderFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        dashboardActivity = (DashboardActivity) getActivity();
        ButterKnife.bind(this, view);
        orderFragment = this;
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        orderRecyclerView = view.findViewById(R.id.order_recycler_view);
        orderSwipeRefresh = view.findViewById(R.id.order_swipe_refresh);

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

    public void updateList(List<OrderListSubscription.Order> orderEntityList) {
        if(null != orderEntityList && orderEntityList.size()>0) {
            Timber.e("Order List Count : " + orderEntityList.size());
            orderRecyclerView.setHasFixedSize(true);
            orderScreenAdapter = new OrderAdapter(dashboardActivity, orderFragment, orderEntityList);
            orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            orderRecyclerView.setAdapter(orderScreenAdapter);
            orderScreenAdapter.notifyDataSetChanged();
            allText.setText("All (" + orderEntityList.size() + ")");
        }
    }

    @Override
    public void updateTabList(List<String> tabModelArrayList) {
        tabList.setHasFixedSize(true);
        tabAdapter = new TabAdapter(dashboardActivity, orderFragment, tabModelArrayList);
        tabList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tabList.setAdapter(tabAdapter);
        tabAdapter.notifyDataSetChanged();
    }

    @Override
    public void getOrder(String orderId) {
        dashboardViewModel.setSelectedOrder(orderId);
        dashboardViewModel.addToTabList(orderId);
        startActivity(new Intent(dashboardActivity, OrderDetailActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardViewModel.removeSelectedOrder();
        updateTabList(dashboardViewModel.getTabList());
        updateList(dashboardViewModel.getOrderList());
    }

    @Override
    public void moveToContinuousScanActivity(OrderListSubscription.Order order) {
        dashboardViewModel.setSelectedOrder(order);
        dashboardViewModel.addToTabList((String) order.id());
        startActivity(new Intent(dashboardActivity, OrderDetailActivity.class));
    }

    @Override
    public void removeOrderFromList(String orderId) {
        dashboardViewModel.removeFromTabList(orderFragment, orderId);
    }

    @Override
    public OrderListSubscription.Order getSelectedOrder() {
        return dashboardViewModel.getSelectedOrder();
    }

}
