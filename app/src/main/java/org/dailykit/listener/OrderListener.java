package org.dailykit.listener;

import org.dailykit.OrderListSubscription;

public interface OrderListener {

    void moveToContinuousScanActivity(OrderListSubscription.Order order);

}
