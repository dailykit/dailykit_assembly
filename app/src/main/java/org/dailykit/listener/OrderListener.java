package org.dailykit.listener;

import org.dailykit.OrderListDetailSubscription;
import org.dailykit.OrderListSubscription;

import java.util.List;

public interface OrderListener {

    void moveToContinuousScanActivity(OrderListDetailSubscription.Order order);
    void removeOrderFromList(String orderId);
    void updateTabList(List<String> tabModelArrayList);
    void getOrder(String orderId);
    OrderListDetailSubscription.Order getSelectedOrder();
    OrderListSubscription.Order getSelectedOrderIndividual();
    void moveToOrderDetailActivity(OrderListSubscription.Order order);

}
