package org.dailykit.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import org.dailykit.room.entity.IngredientEntity;

import java.util.List;

/**
 * Created by Danish Rafique on 24-08-2018.
 */

@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IngredientEntity ingredientEntity);

    @Update
    void update(IngredientEntity ingredientEntity);

    @Query("SELECT * FROM ingredient WHERE item_id LIKE :itemId AND ingredient_is_deleted = 0")
    LiveData<List<IngredientEntity>> loadAllIngredient(String itemId);

    @Query("SELECT * FROM ingredient WHERE item_id LIKE :itemId AND ingredient_is_deleted = 0")
    List<IngredientEntity> loadIngredientByItemId(String itemId);

    @Query("SELECT * FROM ingredient WHERE ingredient_is_deleted = 0")
    List<IngredientEntity> loadIngredients();

    @Query("SELECT COUNT(*) FROM ingredient")
    int countIngredientDao();

    @Query("UPDATE ingredient SET ingredient_is_packed_complete= :isPacked WHERE ingredient_id LIKE :ingredientId")
    void isPackedCompleteUpdate(String ingredientId, boolean isPacked);

    @Query("UPDATE ingredient SET ingredient_is_deleted= :isDeleted WHERE ingredient_id LIKE :ingredientId")
    void isDeletedUpdate(String ingredientId, boolean isDeleted);

    @Query("SELECT COUNT(*) FROM ingredient WHERE item_id LIKE :itemId AND ingredient_is_packed_complete LIKE :isPackedComplete")
    int countIngredientPackedDao(String itemId, boolean isPackedComplete);

    @Query("UPDATE ingredient SET selected_ingredient_position= :selectedIngredientPosition WHERE ingredient_id LIKE :ingredientId")
    void setSelectedItem(String ingredientId, int selectedIngredientPosition);

    @Query("SELECT * FROM ingredient WHERE ingredient_id LIKE :ingredientId")
    IngredientEntity getIngredientById(String ingredientId);

    @Query("UPDATE ingredient SET ingredient_measured_total_weight= :weight WHERE ingredient_id LIKE :ingredientId")
    void setMeasuredTotalWeight(String ingredientId, double weight);

    @Query("SELECT DISTINCT(slip_name) FROM ingredient")
    List<String> getDistinctSlipName();

    @Query("SELECT slip_name FROM ingredient WHERE ingredient_id = :ingredientId")
    String getSlipNameFromIngredientId(String ingredientId);

    @Query("UPDATE ingredient SET ingredient_is_scanned= :isScanned WHERE ingredient_id LIKE :ingredientId")
    void setScanned(String ingredientId, boolean isScanned);

    @Query("SELECT ingredient_is_scanned FROM ingredient WHERE ingredient_id LIKE :ingredientId")
    boolean isScanned(String ingredientId);

    @Query("SELECT ingredient_is_packed_complete FROM ingredient WHERE ingredient_id LIKE :ingredientId")
    boolean isPacked(String ingredientId);

    @Query("SELECT COUNT(*) FROM ingredient WHERE item_id LIKE :itemId AND ingredient_is_scanned LIKE :isScanned")
    int countIngredientScannedDao(String itemId, boolean isScanned);

    @Query("SELECT COUNT(*) FROM ingredient WHERE item_id LIKE :itemId AND ingredient_is_packed_complete LIKE :isPackedCompleted")
    int countIngredientPacked(String itemId, boolean isPackedCompleted);

    @Query("UPDATE ingredient SET ingredient_is_labeled= :isLabeled WHERE ingredient_id LIKE :ingredientId")
    void isLabeledUpdate(String ingredientId, boolean isLabeled);

}
