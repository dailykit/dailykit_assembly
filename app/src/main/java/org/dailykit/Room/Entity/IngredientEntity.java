package org.dailykit.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;


import org.dailykit.model.IngredientModel;
import org.dailykit.room.converter.IngredientDetailConverter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Danish Rafique on 23-08-2018.
 */

@Entity(tableName = "ingredient")
public class IngredientEntity implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ingredient_id")
    public String ingredientId;

    @ColumnInfo(name = "item_id")
    public String ingredientItemId;

    @ColumnInfo(name = "ingredient_index")
    public String ingredientIndex;

    @ColumnInfo(name = "slip_name")
    public String slipName;

    @ColumnInfo(name = "ingredient_is_packed_complete")
    public boolean isPackedComplete;

    @ColumnInfo(name = "ingredient_is_deleted")
    public boolean isDeleted;

    @ColumnInfo(name = "ingredient_is_labeled")
    public boolean isLabeled;

    @ColumnInfo(name = "ingredient_is_scanned")
    public boolean isScanned;

    @ColumnInfo(name = "selected_ingredient_position")
    public int selectedIngredientPosition;

    @ColumnInfo(name = "ingredient_measured_total_weight")
    public double ingredientMeasuredTotalWeight;

    @ColumnInfo(name = "ingredient_detail")
    @TypeConverters(IngredientDetailConverter.class)
    public ArrayList<IngredientDetailEntity> ingredientEntity;


    public IngredientEntity(@NonNull String ingredientId, String ingredientItemId, String ingredientIndex, String slipName, boolean isPackedComplete, boolean isDeleted, boolean isLabeled, int selectedIngredientPosition, double ingredientMeasuredTotalWeight, ArrayList<IngredientDetailEntity> ingredientEntity) {
        this.ingredientId = ingredientId;
        this.ingredientItemId = ingredientItemId;
        this.ingredientIndex = ingredientIndex;
        this.slipName = slipName;
        this.isPackedComplete = isPackedComplete;
        this.isDeleted = isDeleted;
        this.isLabeled = isLabeled;
        this.selectedIngredientPosition = selectedIngredientPosition;
        this.ingredientMeasuredTotalWeight = ingredientMeasuredTotalWeight;
        this.ingredientEntity = ingredientEntity;
    }

    public IngredientEntity(IngredientModel ingredientModel){
        this.ingredientId = ingredientModel.getIngredientId();
        this.ingredientItemId = ingredientModel.getItemId();
        this.ingredientIndex = ingredientModel.getIngredientIndex();
        this.slipName = ingredientModel.getSlipName();
        this.isPackedComplete = ingredientModel.getIngredientIsPackedComplete().equals("1");
        this.isDeleted = ingredientModel.getIngredientIsDeleted().equals("1");
        this.isLabeled = ingredientModel.getIngredientIsLabeled().equals("1");
        this.selectedIngredientPosition = Integer.parseInt(ingredientModel.getSelectedIngredientPosition());
        this.ingredientMeasuredTotalWeight = Double.parseDouble(ingredientModel.getIngredientMeasuredTotalWeight());

    }

    @NonNull
    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(@NonNull String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientItemId() {
        return ingredientItemId;
    }

    public void setIngredientItemId(String ingredientItemId) {
        this.ingredientItemId = ingredientItemId;
    }

    public String getIngredientIndex() {
        return ingredientIndex;
    }

    public void setIngredientIndex(String ingredientIndex) {
        this.ingredientIndex = ingredientIndex;
    }

    public String getSlipName() {
        return slipName;
    }

    public void setSlipName(String slipName) {
        this.slipName = slipName;
    }

    public boolean isPackedComplete() {
        return isPackedComplete;
    }

    public void setPackedComplete(boolean packedComplete) {
        isPackedComplete = packedComplete;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isLabeled() {
        return isLabeled;
    }

    public void setLabeled(boolean labeled) {
        isLabeled = labeled;
    }

    public int getSelectedIngredientPosition() {
        return selectedIngredientPosition;
    }

    public void setSelectedIngredientPosition(int selectedIngredientPosition) {
        this.selectedIngredientPosition = selectedIngredientPosition;
    }

    public double getIngredientMeasuredTotalWeight() {
        return ingredientMeasuredTotalWeight;
    }

    public void setIngredientMeasuredTotalWeight(double ingredientMeasuredTotalWeight) {
        this.ingredientMeasuredTotalWeight = ingredientMeasuredTotalWeight;
    }

    public ArrayList<IngredientDetailEntity> getIngredientEntity() {
        return ingredientEntity;
    }

    public void setIngredientEntity(ArrayList<IngredientDetailEntity> ingredientEntity) {
        this.ingredientEntity = ingredientEntity;
    }

    public boolean isScanned() {
        return isScanned;
    }

    public void setScanned(boolean scanned) {
        isScanned = scanned;
    }

    @Override
    public String toString() {
        return "IngredientEntity{" +
                "ingredientId='" + ingredientId + '\'' +
                ", ingredientItemId='" + ingredientItemId + '\'' +
                ", ingredientIndex='" + ingredientIndex + '\'' +
                ", slipName='" + slipName + '\'' +
                ", isPackedComplete=" + isPackedComplete +
                ", isDeleted=" + isDeleted +
                ", isLabeled=" + isLabeled +
                ", isScanned=" + isScanned +
                ", selectedIngredientPosition=" + selectedIngredientPosition +
                ", ingredientMeasuredTotalWeight=" + ingredientMeasuredTotalWeight +
                ", ingredientEntity=" + ingredientEntity +
                '}';
    }
}
