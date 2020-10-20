package org.dailykit.model


import com.google.gson.annotations.SerializedName

data class BarCodeModel(
    @SerializedName("type")
    var type: String? = "",
    @SerializedName("order_id")
    var orderId: String? = "",
    @SerializedName("product_id")
    var productId: String? = "",
    @SerializedName("sachet_id")
    var sachetId: String? = ""
)