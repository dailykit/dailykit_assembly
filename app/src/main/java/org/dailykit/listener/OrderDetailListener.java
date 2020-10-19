package org.dailykit.listener;

import org.dailykit.OrderListDetailSubscription;

import java.util.List;

public interface OrderDetailListener {

    int getPageCount();

    OrderListDetailSubscription.Order getOrder();

}
