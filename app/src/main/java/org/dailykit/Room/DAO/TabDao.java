package org.dailykit.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import org.dailykit.room.entity.TabEntity;
import java.util.List;

/**
 * Created by Danish Rafique on 09-11-2018.
 */

@Dao
public interface TabDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TabEntity tabEntity);

    @Update
    void update(TabEntity tabEntity);

    @Query("SELECT * FROM tab")
    List<TabEntity> load();

    @Query("UPDATE tab SET active_position= :activePosition WHERE order_id LIKE :orderId")
    void setActivePosition(String orderId, int activePosition);

    @Delete
    void delete(TabEntity tabEntity);

    @Query("SELECT order_number FROM tab WHERE order_id LIKE :orderId")
    String getOrderNumber(String orderId);
}
