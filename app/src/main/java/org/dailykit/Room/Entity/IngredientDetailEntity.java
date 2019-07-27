package org.dailykit.Room.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.dailykit.Model.IngredientDetailModel;

import java.io.Serializable;

/**
 * Created by Danish Rafique on 23-08-2018.
 */

@Entity(tableName = "ingredient_detail")
public class IngredientDetailEntity implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ingredient_detail_id")
    public String ingredientDetailId;

    @ColumnInfo(name = "ingredient_id")
    public String ingredientId;

    @ColumnInfo(name = "ingredient_name")
    public String ingredientName;

    @ColumnInfo(name = "ingredient_quantity")
    public double ingredientQuantity;

    @ColumnInfo(name = "ingredient_measure")
    public String ingredientMsr;

    @ColumnInfo(name = "ingredient_section")
    public String ingredientSection;

    @ColumnInfo(name = "ingredient_process")
    public String ingredientProcess;

    @ColumnInfo(name = "ingredient_is_packed")
    public boolean isPacked;

    @ColumnInfo(name = "ingredient_pack_timestamp")
    public String ingredientPackTimestamp;

    @ColumnInfo(name = "ingredient_is_deleted")
    public boolean isDeleted;

    @ColumnInfo(name = "ingredient_is_weighed")
    public boolean isWeighed;

    @ColumnInfo(name = "ingredient_detail_index")
    public String ingredientDetailIndex;

    @ColumnInfo(name = "ingredient_detail_position")
    public int ingredientDetailPosition;

    @ColumnInfo(name = "ingredient_measured_weight")
    public double ingredientMeasuredWeight;

    public IngredientDetailEntity(@NonNull String ingredientDetailId, String ingredientId, String ingredientName, double ingredientQuantity, String ingredientMsr, String ingredientSection, String ingredientProcess, boolean isPacked, String ingredientPackTimestamp, boolean isDeleted, boolean isWeighed, String ingredientDetailIndex, int ingredientDetailPosition, double ingredientMeasuredWeight) {
        this.ingredientDetailId = ingredientDetailId;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientMsr = ingredientMsr;
        this.ingredientSection = ingredientSection;
        this.ingredientProcess = ingredientProcess;
        this.isPacked = isPacked;
        this.ingredientPackTimestamp = ingredientPackTimestamp;
        this.isDeleted = isDeleted;
        this.isWeighed = isWeighed;
        this.ingredientDetailIndex = ingredientDetailIndex;
        this.ingredientDetailPosition = ingredientDetailPosition;
        this.ingredientMeasuredWeight = ingredientMeasuredWeight;
    }

    public IngredientDetailEntity(IngredientDetailModel ingredientDetailModel){
        this.ingredientDetailId = ingredientDetailModel.getIngredientDetailId();
        this.ingredientId = ingredientDetailModel.getIngredientId();
        this.ingredientName = ingredientDetailModel.getIngredientName();
        this.ingredientQuantity = Double.parseDouble(ingredientDetailModel.getIngredientQuantity());
        this.ingredientMsr = ingredientDetailModel.getIngredientMeasure();
        this.ingredientSection = ingredientDetailModel.getIngredientSection();
        this.ingredientProcess = ingredientDetailModel.getIngredientProcess();
        this.isPacked = ingredientDetailModel.getIngredientIsPacked().equals("1");
        this.ingredientPackTimestamp = ingredientDetailModel.getIngredientPackTimestamp();
        this.isDeleted = ingredientDetailModel.getIngredientIsDeleted().equals("1");
        this.isWeighed = ingredientDetailModel.getIngredientIsWeighed().equals("1");
        this.ingredientDetailIndex = ingredientDetailModel.getIngredientDetailIndex();
        this.ingredientDetailPosition = Integer.parseInt(ingredientDetailModel.getIngredientDetailPosition());
        this.ingredientMeasuredWeight = Double.parseDouble(ingredientDetailModel.getIngredientMeasuredWeight());
    }

    public IngredientDetailEntity() {
    }

    @NonNull
    public String getIngredientDetailId() {
        return ingredientDetailId;
    }

    public void setIngredientDetailId(@NonNull String ingredientDetailId) {
        this.ingredientDetailId = ingredientDetailId;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(double ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getIngredientMsr() {
        return ingredientMsr;
    }

    public void setIngredientMsr(String ingredientMsr) {
        this.ingredientMsr = ingredientMsr;
    }

    public String getIngredientSection() {
        return ingredientSection;
    }

    public void setIngredientSection(String ingredientSection) {
        this.ingredientSection = ingredientSection;
    }

    public String getIngredientProcess() {
        return ingredientProcess;
    }

    public void setIngredientProcess(String ingredientProcess) {
        this.ingredientProcess = ingredientProcess;
    }

    public boolean isPacked() {
        return isPacked;
    }

    public void setPacked(boolean packed) {
        isPacked = packed;
    }

    public String getIngredientPackTimestamp() {
        return ingredientPackTimestamp;
    }

    public void setIngredientPackTimestamp(String ingredientPackTimestamp) {
        this.ingredientPackTimestamp = ingredientPackTimestamp;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isWeighed() {
        return isWeighed;
    }

    public void setWeighed(boolean weighed) {
        isWeighed = weighed;
    }

    public String getIngredientDetailIndex() {
        return ingredientDetailIndex;
    }

    public void setIngredientDetailIndex(String ingredientDetailIndex) {
        this.ingredientDetailIndex = ingredientDetailIndex;
    }

    public int getIngredientDetailPosition() {
        return ingredientDetailPosition;
    }

    public void setIngredientDetailPosition(int ingredientDetailPosition) {
        this.ingredientDetailPosition = ingredientDetailPosition;
    }

    public double getIngredientMeasuredWeight() {
        return ingredientMeasuredWeight;
    }

    public void setIngredientMeasuredWeight(double ingredientMeasuredWeight) {
        this.ingredientMeasuredWeight = ingredientMeasuredWeight;
    }

    @Override
    public String toString() {
        return "IngredientDetailEntity{" +
                "ingredientDetailId='" + ingredientDetailId + '\'' +
                ", ingredientId='" + ingredientId + '\'' +
                ", ingredientName='" + ingredientName + '\'' +
                ", ingredientQuantity=" + ingredientQuantity +
                ", ingredientMsr='" + ingredientMsr + '\'' +
                ", ingredientSection='" + ingredientSection + '\'' +
                ", ingredientProcess='" + ingredientProcess + '\'' +
                ", isPacked=" + isPacked +
                ", ingredientPackTimestamp='" + ingredientPackTimestamp + '\'' +
                ", isDeleted=" + isDeleted +
                ", isWeighed=" + isWeighed +
                ", ingredientDetailIndex='" + ingredientDetailIndex + '\'' +
                ", ingredientDetailPosition=" + ingredientDetailPosition +
                ", ingredientMeasuredWeight=" + ingredientMeasuredWeight +
                '}';
    }
}