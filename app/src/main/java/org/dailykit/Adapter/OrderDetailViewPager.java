package org.dailykit.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.dailykit.OrderListSubscription;
import org.dailykit.fragment.InventoryProductODFragment;
import org.dailykit.fragment.MealKitProductODFragment;
import org.dailykit.fragment.ReadyToEatProductODFragment;

import org.dailykit.listener.OrderDetailListener;

public class OrderDetailViewPager extends FragmentStatePagerAdapter {

    private InventoryProductODFragment inventoryProductODFragment;
    private ReadyToEatProductODFragment readyToEatProductODFragment;
    private MealKitProductODFragment mealKitProductODFragment;
    private OrderDetailListener orderDetailListener;

    public OrderDetailViewPager(@NonNull FragmentManager fm, OrderDetailListener orderDetailListener) {
        super(fm);
        this.orderDetailListener = orderDetailListener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        OrderListSubscription.Order order = orderDetailListener.getOrder();
        if(position == 0 && order.orderInventoryProducts().size()>0){
            inventoryProductODFragment = new InventoryProductODFragment(orderDetailListener);
            return inventoryProductODFragment;
        }
        else if((position == 0 && order.orderInventoryProducts().size() == 0) || (position == 1 && order.orderReadyToEatProducts().size()>0)){
            readyToEatProductODFragment = new ReadyToEatProductODFragment(orderDetailListener);
            return readyToEatProductODFragment;
        }
        else if((position == 0 && order.orderInventoryProducts().size() == 0 && order.orderReadyToEatProducts().size() == 0) || ((position == 1 && order.orderMealKitProducts().size()==0)
                || (position == 1 && order.orderInventoryProducts().size()==0)) || (position == 2 && order.orderMealKitProducts().size()>0)){
            mealKitProductODFragment = new MealKitProductODFragment(orderDetailListener);
            return mealKitProductODFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return orderDetailListener.getPageCount();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        OrderListSubscription.Order order = orderDetailListener.getOrder();
        if(position == 0 && order.orderInventoryProducts().size()>0) {
            return "Inventory";
        }
        else if((position == 0 && order.orderInventoryProducts().size() == 0) || (position == 1 && order.orderReadyToEatProducts().size()>0)){
            return "Ready to Eat";
        }
        else if((position == 0 && order.orderInventoryProducts().size() == 0 && order.orderReadyToEatProducts().size() == 0) || ((position == 1 && order.orderMealKitProducts().size()==0)
                || (position == 1 && order.orderInventoryProducts().size()==0)) || (position == 2 && order.orderMealKitProducts().size()>0)){
            return "Meal Kit";
        }
        return "";

    }

    public InventoryProductODFragment getInventoryProductODFragment(){
        return inventoryProductODFragment;
    }

    public ReadyToEatProductODFragment getReadyToEatProductODFragment(){
        return readyToEatProductODFragment;
    }

    public MealKitProductODFragment getMealKitProductODFragment(){
        return mealKitProductODFragment;
    }
}
