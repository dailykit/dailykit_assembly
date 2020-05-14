package org.dailykit.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import org.dailykit.room.entity.ItemEntity;

import java.util.List;

/**
 * Created by Danish Rafique on 24-08-2018.
 */

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemEntity itemEntity);

    @Update
    void update(ItemEntity itemEntity);

    @Query("SELECT * FROM item WHERE order_id LIKE :orderId")
    LiveData<List<ItemEntity>> loadAllItem(String orderId);

    @Query("SELECT * FROM item WHERE order_id LIKE :orderId")
    List<ItemEntity> loadItemsByOrderId(String orderId);

    @Query("SELECT * FROM item")
    List<ItemEntity> loadItems();

    @Query("UPDATE item SET selected_position= :selectedPosition WHERE item_order_id LIKE :orderId")
    void setSelectedItem(String orderId, int selectedPosition);

    @Query("SELECT * FROM item WHERE item_order_id LIKE :itemId")
    ItemEntity loadItem(String itemId);

    @Query("SELECT COUNT(*) FROM item")
    int countItemDao();

}
