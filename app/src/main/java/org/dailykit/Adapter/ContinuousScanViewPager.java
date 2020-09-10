package org.dailykit.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.dailykit.OrderListSubscription;
import org.dailykit.fragment.InventoryProductFragment;
import org.dailykit.fragment.MealKitProductFragment;
import org.dailykit.fragment.ReadyToEatProductFragment;
import org.dailykit.listener.ContinuousScanListener;

public class ContinuousScanViewPager extends FragmentStatePagerAdapter {

    private InventoryProductFragment inventoryProductFragment;
    private ReadyToEatProductFragment readyToEatProductFragment;
    private MealKitProductFragment mealKitProductFragment;
    private ContinuousScanListener continuousScanListener;

    public ContinuousScanViewPager(@NonNull FragmentManager fm, ContinuousScanListener continuousScanListener) {
        super(fm);
        this.continuousScanListener = continuousScanListener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        OrderListSubscription.Order order = continuousScanListener.getOrder();
        if(position == 0 && order.orderInventoryProducts().size()>0){
            inventoryProductFragment = new InventoryProductFragment(continuousScanListener);
            return inventoryProductFragment;
        }
        else if((position == 0 && order.orderInventoryProducts().size() == 0) || (position == 1 && order.orderReadyToEatProducts().size()>0)){
            readyToEatProductFragment = new ReadyToEatProductFragment(continuousScanListener);
            return readyToEatProductFragment;
        }
        else if((position == 0 && order.orderInventoryProducts().size() == 0 && order.orderReadyToEatProducts().size() == 0) || ((position == 1 && order.orderMealKitProducts().size()==0)
                || (position == 1 && order.orderInventoryProducts().size()==0)) || (position == 2 && order.orderMealKitProducts().size()>0)){
            mealKitProductFragment = new MealKitProductFragment(continuousScanListener);
            return mealKitProductFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return continuousScanListener.getPageCount();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        OrderListSubscription.Order order = continuousScanListener.getOrder();
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

    public InventoryProductFragment getInventoryProductFragment(){
        return inventoryProductFragment;
    }

    public ReadyToEatProductFragment getReadyToEatProductFragment(){
        return readyToEatProductFragment;
    }

    public MealKitProductFragment getMealKitProductFragment(){
        return mealKitProductFragment;
    }
}
