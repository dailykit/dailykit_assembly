package org.dailykit.listener;


import org.dailykit.OrderListDetailSubscription;

public interface ContinuousScanListener {

    void updateIngredientList();

    void setScannedIngredientDetail();

    int getPageCount();

    OrderListDetailSubscription.Order getOrder();

}
