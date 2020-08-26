package org.dailykit.models


import com.google.gson.annotations.SerializedName

data class CustomerModel(
    @SerializedName("email")
    var email: String
)