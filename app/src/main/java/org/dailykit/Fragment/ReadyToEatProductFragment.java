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
import org.dailykit.adapter.InventoryProductCSAdapter;
import org.dailykit.adapter.ReadyToEatProductCSAdapter;
import org.dailykit.adapter.ReadyToEatProductODAdapter;
import org.dailykit.listener.ContinuousScanListener;
import org.dailykit.listener.ReadyToEatProductListener;
import org.dailykit.viewmodel.ContinuousScanViewModel;
import org.dailykit.viewmodel.DashboardViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadyToEatProductFragment extends Fragment implements ReadyToEatProductListener {

    @BindView(R.id.ready_to_eat_list)
    RecyclerView readyToEatList;
    private ContinuousScanListener continuousScanListener;
    DashboardViewModel dashboardViewModel;
    ContinuousScanViewModel continuousScanViewModel;
    ContinuousScanActivity continuousScanActivity;
    private OrderListSubscription.Order order;
    private ReadyToEatProductODAdapter readyToEatProductCSAdapter;
    private ReadyToEatProductFragment readyToEatProductFragment;

    public ReadyToEatProductFragment() {

    }

    public ReadyToEatProductFragment(ContinuousScanListener continuousScanListener) {
        this.continuousScanListener = continuousScanListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ready_to_eat_product, container, false);
        ButterKnife.bind(this,view);
        readyToEatProductFragment = this;
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        continuousScanViewModel = ViewModelProviders.of(this).get(ContinuousScanViewModel.class);
        continuousScanActivity = (ContinuousScanActivity)getActivity();
        setView();
        return view;
    }

    public void setView(){
        order = dashboardViewModel.getSelectedOrder();
        readyToEatProductCSAdapter = new ReadyToEatProductODAdapter(continuousScanActivity, readyToEatProductFragment, order.orderReadyToEatProducts());
        readyToEatList.setLayoutManager( new LinearLayoutManager(continuousScanActivity, LinearLayoutManager.VERTICAL, false));
        readyToEatList.setAdapter(readyToEatProductCSAdapter);
        readyToEatProductCSAdapter.notifyDataSetChanged();
    }
}