package org.dailykit.fragment;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.dailykit.activity.DashboardActivity;
import org.dailykit.adapter.IngredientAdapter;
import org.dailykit.adapter.IngredientLowerTabAdapter;
import org.dailykit.customview.FuturaBoldTextView;
import org.dailykit.R;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.viewmodel.DashboardViewModel;


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



    public void updateUI(){
        orderName.setText("");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updatePackingStatus(String ingredientId){
        //dashboardViewModel.updatePacking(ingredientId);
    }


}
