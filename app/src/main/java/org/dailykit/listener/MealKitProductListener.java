package org.dailykit.listener;

import org.dailykit.OrderListDetailSubscription;

public interface MealKitProductListener {

    void onResponse(OrderListDetailSubscription.OrderSachet orderSachet,String message);

}
