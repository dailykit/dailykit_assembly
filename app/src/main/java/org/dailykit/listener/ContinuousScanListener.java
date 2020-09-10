package org.dailykit.listener;

import org.dailykit.OrderListSubscription;

public interface ContinuousScanListener {

    void updateIngredientList();

    void setScannedIngredientDetail();

    int getPageCount();

    OrderListSubscription.Order getOrder();

}
