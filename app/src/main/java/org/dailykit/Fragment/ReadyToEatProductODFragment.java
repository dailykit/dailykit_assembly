package org.dailykit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListDetailSubscription;
import org.dailykit.R;
import org.dailykit.activity.OrderDetailActivity;
import org.dailykit.adapter.ReadyToEatProductODAdapter;
import org.dailykit.listener.OrderDetailListener;
import org.dailykit.listener.ReadyToEatProductListener;
import org.dailykit.viewmodel.DashboardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadyToEatProductODFragment extends Fragment implements ReadyToEatProductListener {

    @BindView(R.id.list)
    RecyclerView list;
    DashboardViewModel dashboardViewModel;
    private OrderDetailListener orderDetailListener;
    private ReadyToEatProductODAdapter readyToEatProductODAdapter;
    private OrderListDetailSubscription.Order order;
    private ReadyToEatProductODFragment readyToEatProductODFragment;
    private OrderDetailActivity orderDetailActivity;

    public ReadyToEatProductODFragment() {

    }

    public ReadyToEatProductODFragment(OrderDetailListener orderDetailListener) {
        this.orderDetailListener = orderDetailListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ready_to_eat_product_od, container, false);
        ButterKnife.bind(this,view);
        readyToEatProductODFragment = this;
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        orderDetailActivity = (OrderDetailActivity)getActivity();
        setView();
        return view;
    }

    public void setView(){
        order = dashboardViewModel.getSelectedOrderDetail();
        readyToEatProductODAdapter = new ReadyToEatProductODAdapter(orderDetailActivity, readyToEatProductODFragment, order.orderReadyToEatProducts());
        list.setLayoutManager( new LinearLayoutManager(orderDetailActivity, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(readyToEatProductODAdapter);
        readyToEatProductODAdapter.notifyDataSetChanged();
    }

    @Override
    public void markAssemble(OrderListDetailSubscription.OrderReadyToEatProduct orderReadyToEatProduct) {
        int index = 0;
        if(dashboardViewModel.getSelectedOrderDetail().orderInventoryProducts().size()>0) {
            index++;
        }
        dashboardViewModel.setActiveProductTab(index);
        dashboardViewModel.setActiveMealkitTab(0);
    }
}