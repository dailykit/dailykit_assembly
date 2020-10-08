package org.dailykit.listener;

import org.dailykit.OrderListSubscription;

public interface InventoryProductListener {

    void markAssemble(OrderListSubscription.OrderInventoryProduct orderInventoryProduct);

}
