package org.dailykit.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import org.dailykit.model.OrderModel;
import org.dailykit.room.converter.ItemConverter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Danish Rafique on 23-08-2018.
 */

@Entity(tableName = "order")
public class OrderEntity implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "order_id")
    public String orderId;

    @ColumnInfo(name = "order_number")
    public String orderNumber;

    @ColumnInfo(name = "order")
    @TypeConverters(ItemConverter.class)
    public ArrayList<ItemEntity> orderItems;

    public OrderEntity(@NonNull String orderId, String orderNumber, ArrayList<ItemEntity> orderItems) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderItems = orderItems;
    }

    public OrderEntity(OrderModel orderModel){
        this.orderId = orderModel.getOrderId();
        this.orderNumber = orderModel.getOrderNumber();
    }

    @NonNull
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(@NonNull String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<ItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<ItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderId='" + orderId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }
}
