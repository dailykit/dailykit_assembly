package org.dailykit.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.dailykit.OrderListDetailSubscription;
import org.dailykit.fragment.MealKitProductDetailFragment;
import org.dailykit.listener.OrderDetailListener;

import java.util.ArrayList;
import java.util.List;

public class MealKitViewPager extends FragmentStatePagerAdapter {

    Context context;
    OrderDetailListener orderDetailListener;
    List<OrderListDetailSubscription.OrderMealKitProduct> orderMealKitProductArrayList;

    public MealKitViewPager(Context context, FragmentManager fm,OrderDetailListener orderDetailListener, List<OrderListDetailSubscription.OrderMealKitProduct> orderMealKitProductArrayList) {
        super(fm);
        this.context = context;
        this.orderDetailListener = orderDetailListener;
        this.orderMealKitProductArrayList=orderMealKitProductArrayList;
    }
    @Override
    public int getCount() {
        return orderMealKitProductArrayList.size();
    }

    @Override
    public Fragment getItem(int position) {

        MealKitProductDetailFragment mealKitProductDetailFragment = new MealKitProductDetailFragment(orderMealKitProductArrayList.get(position),orderDetailListener);
        return mealKitProductDetailFragment;

    }

}

