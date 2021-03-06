package org.dailykit.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.dailykit.room.entity.IngredientDetailEntity;

import java.util.List;


/**
 * Created by Danish Rafique on 24-08-2018.
 */

@Dao
public interface IngredientDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IngredientDetailEntity ingredientDetailEntity);

    @Update
    void update(IngredientDetailEntity ingredientDetailEntity);

    @Query("SELECT * FROM ingredient_detail WHERE ingredient_id LIKE :ingredientId AND ingredient_is_deleted = 0")
    LiveData<List<IngredientDetailEntity>> loadAllIngredientDetail(String ingredientId);

    @Query("SELECT * FROM ingredient_detail ")
    List<IngredientDetailEntity> loadAll();

    @Query("SELECT * FROM ingredient_detail WHERE ingredient_id LIKE :ingredientId AND ingredient_is_deleted = 0")
    List<IngredientDetailEntity> loadIngredientDetailByIngredientId(String ingredientId);

    @Query("SELECT * FROM ingredient_detail")
    IngredientDetailEntity loadSingleIngredientEntity();

    @Query("UPDATE ingredient_detail SET ingredient_is_packed= :isPacked WHERE ingredient_detail_id LIKE :ingredientDetailId")
    void isPackedUpdate(String ingredientDetailId, boolean isPacked);

    @Query("UPDATE ingredient_detail SET ingredient_is_packed= :isPacked WHERE ingredient_id LIKE :ingredientId")
    void isPackedUpdateByIngredientId(String ingredientId, boolean isPacked);

    @Query("UPDATE ingredient_detail SET ingredient_is_deleted= :isDeleted WHERE ingredient_detail_id LIKE :ingredientDetailId")
    void isDeletedUpdate(String ingredientDetailId, boolean isDeleted);

    @Query("UPDATE ingredient_detail SET ingredient_pack_timestamp= :timestamp WHERE ingredient_detail_id LIKE :ingredientDetailId")
    void updateTimestamp(String ingredientDetailId, String timestamp);

    @Query("SELECT ingredient_is_packed FROM ingredient_detail WHERE ingredient_detail_id LIKE :ingredientDetailId")
    boolean isPacked(String ingredientDetailId);

    @Query("SELECT COUNT(*) FROM ingredient_detail")
    int countIngredientDetailDao();

    @Query("SELECT COUNT(*) FROM ingredient_detail WHERE ingredient_id LIKE :ingredientId")
    int countIngredientDetailPerIngredientId(String ingredientId);

    @Query("SELECT * FROM ingredient_detail WHERE ingredient_id LIKE :ingredientId AND ingredient_detail_position LIKE :position")
    IngredientDetailEntity getIngredientByPositionAndDetailId(String ingredientId,int position);

    @Query("UPDATE ingredient_detail SET ingredient_is_weighed= :isWeighed WHERE ingredient_id LIKE :ingredientId")
    void isWeighedUpdate(String ingredientId, boolean isWeighed);
}
