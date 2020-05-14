package org.dailykit.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Danish Rafique on 29-01-2019.
 */
data class OrderModel(@field:SerializedName("order_id")
                      var orderId: String,

                      @field:SerializedName("order_number")
                      var orderNumber: String,

                      @field:SerializedName("items")
                      var items: ArrayList<ItemModel>)