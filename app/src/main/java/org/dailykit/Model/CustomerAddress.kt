package org.dailykit.model


import com.google.gson.annotations.SerializedName

data class CustomerAddress(
    @SerializedName("city")
    var city: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("line1")
    var line1: String,
    @SerializedName("line2")
    var line2: String,
    @SerializedName("notes")
    var notes: String,
    @SerializedName("state")
    var state: String,
    @SerializedName("zipcode")
    var zipcode: String
)