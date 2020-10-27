package org.dailykit.listener;

import org.dailykit.OrderListDetailSubscription;

public interface InventoryProductListener {

    void onResponse(OrderListDetailSubscription.OrderInventoryProduct orderInventoryProduct,String message);
}
