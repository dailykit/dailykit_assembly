package org.dailykit.listener;

import org.dailykit.OrderListSubscription;

import java.util.List;

public interface OrderListener {

    void moveToContinuousScanActivity(OrderListSubscription.Order order);
    void removeOrderFromList(String orderId);
    void updateTabList(List<String> tabModelArrayList);
    void getOrder(String orderId);
    OrderListSubscription.Order getSelectedOrder();

}
