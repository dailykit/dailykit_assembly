package org.dailykit.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import org.dailykit.model.ItemModel;
import org.dailykit.room.converter.IngredientConverter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Danish Rafique on 23-08-2018.
 */

@Entity(tableName = "item")
public class ItemEntity implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "item_order_id")
    public String itemOrderId;

    @ColumnInfo(name = "order_id")
    public String orderId;

    @ColumnInfo(name = "recipe_sku")
    public String itemSku;

    @ColumnInfo(name = "recipe_name")
    public String itemName;

    @ColumnInfo(name = "recipe_servings")
    public String itemServing;

    @ColumnInfo(name = "recipe_quantity")
    public String itemQuantity;

    @ColumnInfo(name = "order_status")
    public String itemStatus;

    @ColumnInfo(name = "order_number")
    public String itemNumber;

    @ColumnInfo(name = "selected_position")
    public int selectedPosition;

    @ColumnInfo(name = "number_of_ingredient")
    public String itemNoOfIngredient;

    @ColumnInfo(name = "cus_phone")
    public String customerPhone;

    @ColumnInfo(name = "cus_name")
    public String customerName;

    @ColumnInfo(name = "cus_address")
    public String customerAddress;

    @ColumnInfo(name = "mer_id")
    public String merId;

    @ColumnInfo(name = "rec_cuisine")
    public String recipyCuisine;

    @ColumnInfo(name = "rec_price")
    public String recipyPrice;

    @ColumnInfo(name = "sub_total")
    public String subTotal;

    @ColumnInfo(name = "discount_percentage")
    public String discountPercentage;

    @ColumnInfo(name = "discount")
    public String discount;

    @ColumnInfo(name = "new_cus_discount")
    public String newCustomerDiscount;

    @ColumnInfo(name = "sgst")
    public String sgst;

    @ColumnInfo(name = "cgst")
    public String cgst;

    @ColumnInfo(name = "del_charges")
    public String deliveryCharges;

    @ColumnInfo(name = "total_price")
    public String totalPrice;

    @ColumnInfo(name = "grcash")
    public String grcash;

    @ColumnInfo(name = "walletcash")
    public String walletCash;

    @ColumnInfo(name = "final_amount")
    public String finalAmount;

    @ColumnInfo(name = "payment_type")
    public String paymentType;

    @ColumnInfo(name = "payment_status")
    public String paymentStatus;

    @ColumnInfo(name = "add_notes")
    public String addNotes;

    @ColumnInfo(name = "del_time")
    public String deliveryTime;

    @ColumnInfo(name = "order_at")
    public String orderAt;

    @ColumnInfo(name = "order_cancel_till")
    public String orderCancelTill;

    @ColumnInfo(name = "delivery_expected")
    public String deliveryExpected;

    @ColumnInfo(name = "dispatch_real")
    public String dispatchReal;

    @ColumnInfo(name = "ingredient")
    @TypeConverters(IngredientConverter.class)
    public ArrayList<IngredientEntity> itemIngredient;

    public ItemEntity(@NonNull String itemOrderId, String orderId, String itemSku, String itemName, String itemServing, String itemQuantity, String itemStatus, String itemNumber, int selectedPosition, String itemNoOfIngredient, String customerPhone, String customerName, String customerAddress, String merId, String recipyCuisine, String recipyPrice, String subTotal, String discountPercentage, String discount, String newCustomerDiscount, String sgst, String cgst, String deliveryCharges, String totalPrice, String grcash, String walletCash, String finalAmount, String paymentType, String paymentStatus, String addNotes, String deliveryTime, String orderAt, String orderCancelTill, String deliveryExpected, String dispatchReal, ArrayList<IngredientEntity> itemIngredient) {
        this.itemOrderId = itemOrderId;
        this.orderId = orderId;
        this.itemSku = itemSku;
        this.itemName = itemName;
        this.itemServing = itemServing;
        this.itemQuantity = itemQuantity;
        this.itemStatus = itemStatus;
        this.itemNumber = itemNumber;
        this.selectedPosition = selectedPosition;
        this.itemNoOfIngredient = itemNoOfIngredient;
        this.customerPhone = customerPhone;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.merId = merId;
        this.recipyCuisine = recipyCuisine;
        this.recipyPrice = recipyPrice;
        this.subTotal = subTotal;
        this.discountPercentage = discountPercentage;
        this.discount = discount;
        this.newCustomerDiscount = newCustomerDiscount;
        this.sgst = sgst;
        this.cgst = cgst;
        this.deliveryCharges = deliveryCharges;
        this.totalPrice = totalPrice;
        this.grcash = grcash;
        this.walletCash = walletCash;
        this.finalAmount = finalAmount;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.addNotes = addNotes;
        this.deliveryTime = deliveryTime;
        this.orderAt = orderAt;
        this.orderCancelTill = orderCancelTill;
        this.deliveryExpected = deliveryExpected;
        this.dispatchReal = dispatchReal;
        this.itemIngredient = itemIngredient;
    }

    public ItemEntity(ItemModel itemModel){
        this.itemOrderId = itemModel.getItemOrderId();
        this.orderId = itemModel.getOrderId();
        this.itemSku = itemModel.getRecipeSku();
        this.itemName = itemModel.getRecipeName();
        this.itemServing = itemModel.getRecipeServings();
        this.itemQuantity = itemModel.getRecipeQuantity();
        this.itemStatus = "";
        this.itemNumber = itemModel.getOrderNumber();
        this.selectedPosition = Integer.parseInt(itemModel.getSelectedPosition());
        this.itemNoOfIngredient = itemModel.getNumberOfIngredients();
        this.customerPhone = itemModel.getCustomerPhone();
        this.customerName = itemModel.getCustomerName();
        this.customerAddress = itemModel.getCustomerAddress();
        this.merId = itemModel.getMerchantId();
        this.recipyCuisine = itemModel.getRecipeCuisine();
        this.recipyPrice = itemModel.getRecipePrice();
        this.subTotal = itemModel.getSubTotal();
        this.discountPercentage = itemModel.getDiscountPercentage();
        this.discount = itemModel.getDiscount();
        this.newCustomerDiscount = itemModel.getNewCustomerDiscount();
        this.sgst = itemModel.getSgst();
        this.cgst = itemModel.getCgst();
        this.deliveryCharges = itemModel.getDeliveryCharges();
        this.totalPrice = itemModel.getTotalPrice();
        this.grcash = itemModel.getGrCash();
        this.walletCash = itemModel.getWalletCash();
        this.finalAmount = itemModel.getFinalAmount();
        this.paymentType = itemModel.getPaymentType();
        this.paymentStatus = itemModel.getPaymentStatus();
        this.addNotes = itemModel.getAddNotes();
        this.deliveryTime = itemModel.getDeliveryTime();
        this.orderAt = itemModel.getOrderAt();
        this.orderCancelTill = itemModel.getOrderCancelTill();
        this.deliveryExpected = itemModel.getDeliveryExpected();
        this.dispatchReal = itemModel.getDispatchReal();
    }


    @NonNull
    public String getItemOrderId() {
        return itemOrderId;
    }

    public void setItemOrderId(@NonNull String itemOrderId) {
        this.itemOrderId = itemOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemServing() {
        return itemServing;
    }

    public void setItemServing(String itemServing) {
        this.itemServing = itemServing;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public String getItemNoOfIngredient() {
        return itemNoOfIngredient;
    }

    public void setItemNoOfIngredient(String itemNoOfIngredient) {
        this.itemNoOfIngredient = itemNoOfIngredient;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getRecipyCuisine() {
        return recipyCuisine;
    }

    public void setRecipyCuisine(String recipyCuisine) {
        this.recipyCuisine = recipyCuisine;
    }

    public String getRecipyPrice() {
        return recipyPrice;
    }

    public void setRecipyPrice(String recipyPrice) {
        this.recipyPrice = recipyPrice;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNewCustomerDiscount() {
        return newCustomerDiscount;
    }

    public void setNewCustomerDiscount(String newCustomerDiscount) {
        this.newCustomerDiscount = newCustomerDiscount;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGrcash() {
        return grcash;
    }

    public void setGrcash(String grcash) {
        this.grcash = grcash;
    }

    public String getWalletCash() {
        return walletCash;
    }

    public void setWalletCash(String walletCash) {
        this.walletCash = walletCash;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAddNotes() {
        return addNotes;
    }

    public void setAddNotes(String addNotes) {
        this.addNotes = addNotes;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(String orderAt) {
        this.orderAt = orderAt;
    }

    public String getOrderCancelTill() {
        return orderCancelTill;
    }

    public void setOrderCancelTill(String orderCancelTill) {
        this.orderCancelTill = orderCancelTill;
    }

    public String getDeliveryExpected() {
        return deliveryExpected;
    }

    public void setDeliveryExpected(String deliveryExpected) {
        this.deliveryExpected = deliveryExpected;
    }

    public String getDispatchReal() {
        return dispatchReal;
    }

    public void setDispatchReal(String dispatchReal) {
        this.dispatchReal = dispatchReal;
    }

    public ArrayList<IngredientEntity> getItemIngredient() {
        return itemIngredient;
    }

    public void setItemIngredient(ArrayList<IngredientEntity> itemIngredient) {
        this.itemIngredient = itemIngredient;
    }

    @Override
    public String toString() {
        return "ItemEntity{" +
                "itemOrderId='" + itemOrderId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", itemSku='" + itemSku + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemServing='" + itemServing + '\'' +
                ", itemQuantity='" + itemQuantity + '\'' +
                ", itemStatus='" + itemStatus + '\'' +
                ", itemNumber='" + itemNumber + '\'' +
                ", selectedPosition=" + selectedPosition +
                ", itemNoOfIngredient='" + itemNoOfIngredient + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", merId='" + merId + '\'' +
                ", recipyCuisine='" + recipyCuisine + '\'' +
                ", recipyPrice='" + recipyPrice + '\'' +
                ", subTotal='" + subTotal + '\'' +
                ", discountPercentage='" + discountPercentage + '\'' +
                ", discount='" + discount + '\'' +
                ", newCustomerDiscount='" + newCustomerDiscount + '\'' +
                ", sgst='" + sgst + '\'' +
                ", cgst='" + cgst + '\'' +
                ", deliveryCharges='" + deliveryCharges + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", grcash='" + grcash + '\'' +
                ", walletCash='" + walletCash + '\'' +
                ", finalAmount='" + finalAmount + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", addNotes='" + addNotes + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", orderAt='" + orderAt + '\'' +
                ", orderCancelTill='" + orderCancelTill + '\'' +
                ", deliveryExpected='" + deliveryExpected + '\'' +
                ", dispatchReal='" + dispatchReal + '\'' +
                ", itemIngredient=" + itemIngredient +
                '}';
    }
}
