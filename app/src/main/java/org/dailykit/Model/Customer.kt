package org.dailykit.model


import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("customerAddress")
    var customerAddress: CustomerAddress,
    @SerializedName("customerEmail")
    var customerEmail: String,
    @SerializedName("customerFirstName")
    var customerFirstName: String,
    @SerializedName("customerLastName")
    var customerLastName: String,
    @SerializedName("customerPhone")
    var customerPhone: String
)