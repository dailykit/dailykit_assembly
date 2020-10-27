package org.dailykit.listener;

import org.dailykit.OrderListDetailSubscription;

public interface ReadyToEatProductListener {

    void onResponse(OrderListDetailSubscription.OrderReadyToEatProduct orderReadyToEatProduct,String message);

}
