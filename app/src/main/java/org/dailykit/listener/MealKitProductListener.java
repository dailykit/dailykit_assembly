package org.dailykit.listener;

import org.dailykit.OrderListDetailSubscription;

public interface MealKitProductListener {

    void markAssemble(OrderListDetailSubscription.OrderSachet orderSachet);

}
