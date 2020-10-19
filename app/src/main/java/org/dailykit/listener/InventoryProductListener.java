package org.dailykit.listener;

import org.dailykit.OrderListDetailSubscription;

public interface InventoryProductListener {

    void markAssemble(OrderListDetailSubscription.OrderInventoryProduct orderInventoryProduct);
}
