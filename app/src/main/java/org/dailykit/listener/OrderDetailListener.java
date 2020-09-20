package org.dailykit.listener;

import org.dailykit.OrderListSubscription;

import java.util.List;

public interface OrderDetailListener {

    int getPageCount();

    OrderListSubscription.Order getOrder();

}
