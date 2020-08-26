package org.dailykit.models

import com.google.gson.annotations.SerializedName

data class OrderModel(
        @SerializedName("id")
        var id: String,
        @SerializedName("orderStatus")
        var orderStatus: String,
        @SerializedName("customer")
        var customer: CustomerModel
)