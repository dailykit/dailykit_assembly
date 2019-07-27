package org.dailykit.Fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.dailykit.Activity.DashboardActivity;
import org.dailykit.Adapter.IngredientAdapter;
import org.dailykit.Adapter.IngredientLowerTabAdapter;
import org.dailykit.CustomView.FuturaBoldTextView;
import org.dailykit.R;
import org.dailykit.Room.Entity.ItemEntity;
import org.dailykit.ViewModel.DashboardViewModel;


public class IngredientFragment extends Fragment {

    RecyclerView ingredientTabList,ingredientDetailList;
    DashboardViewModel dashboardViewModel;
    IngredientLowerTabAdapter ingredientLowerTabAdapter;
    FuturaBoldTextView orderName;
    IngredientAdapter ingredientAdapter;
    ItemEntity itemEntity;
    RecyclerView.SmoothScroller smoothScroller;
    ImageView backButton;
    DashboardActivity dashboardActivity;
    private static final String TAG = "IngredientFragment";


    public IngredientFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        dashboardViewModel= ViewModelProviders.of(this).get(DashboardViewModel.class);
        dashboardActivity=(DashboardActivity)getActivity();
        ingredientTabList = view.findViewById(R.id.list_ingredient_tab);
        orderName = view.findViewById(R.id.order_name);
        ingredientDetailList = view.findViewById(R.id.ingredient_detail_list);
        backButton = view.findViewById(R.id.back_button);
        smoothScroller = new LinearSmoothScroller(getActivity()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.switchToOrderFragment();
            }
        });
        updateUI();
        return view;
    }

    public void updateTabList(){
        ingredientTabList.setHasFixedSize(true);
        ingredientLowerTabAdapter = new IngredientLowerTabAdapter(getActivity(), dashboardViewModel.fetchItemList());
        ingredientTabList.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        ingredientTabList.setAdapter(ingredientLowerTabAdapter);
        ingredientLowerTabAdapter.notifyDataSetChanged();
    }

    public void updateIngredientList(){
        itemEntity = dashboardViewModel.getCurrentItemEntity();
        ingredientDetailList.invalidate();
        ingredientAdapter = new IngredientAdapter(getActivity(),this, dashboardViewModel.getIngredientEntityList(), itemEntity,dashboardViewModel.getNumberOfIngredientsPacked());
        ingredientDetailList.setLayoutManager(null);
        ingredientDetailList.setAdapter(null);
        ingredientDetailList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ingredientDetailList.setAdapter(ingredientAdapter);
        ingredientAdapter.notifyDataSetChanged();
        if(itemEntity.getSelectedPosition()!=-1) {
            smoothScroller.setTargetPosition(itemEntity.getSelectedPosition());
            ingredientDetailList.getLayoutManager().startSmoothScroll(smoothScroller);
        }
    }

    public void updateUI(){
        orderName.setText(dashboardViewModel.getSelectedOrderNumber());
        updateTabList();
        updateIngredientList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateIngredientList();
        updateTabList();
    }

    public void updatePackingStatus(String ingredientId){
        dashboardViewModel.updatePacking(ingredientId);
    }


}
