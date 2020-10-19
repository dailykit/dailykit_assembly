package org.dailykit.listener;

import org.dailykit.OrderListDetailSubscription;

public interface ReadyToEatProductListener {

    void markAssemble(OrderListDetailSubscription.OrderReadyToEatProduct orderReadyToEatProduct);

}
