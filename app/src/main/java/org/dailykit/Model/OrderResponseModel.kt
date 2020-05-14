package org.dailykit.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Danish Rafique on 29-01-2019.
 */
data class OrderResponseModel(@field:SerializedName("success")
                              var success: String,

                              @field:SerializedName("all_orders")
                              var allOrders: ArrayList<OrderModel>)