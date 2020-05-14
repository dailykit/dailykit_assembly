package org.dailykit.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import org.dailykit.room.entity.OrderEntity;

import java.util.List;

/**
 * Created by Danish Rafique on 24-08-2018.
 */

@Dao
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderEntity orderEntity);

    @Update
    void update(OrderEntity orderEntity);

    @Query("SELECT * FROM `order` WHERE order_number LIKE :orderId")
    OrderEntity loadOrder(String orderId);

    @Query("SELECT * FROM `order`")
    LiveData<List<OrderEntity>> loadAllOrder();

    @Query("SELECT * FROM `order`")
    List<OrderEntity> loadAllOrderList();

    @Query("SELECT COUNT(*) FROM `order`")
    int countOrder();
}
