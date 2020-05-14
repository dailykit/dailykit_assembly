package org.dailykit.model

import com.google.gson.annotations.SerializedName

data class ScanRequestModel(@field:SerializedName("ingredient_id")
                            var ingredientId: String,

                            @field:SerializedName("is_scanned")
                            var isScanned: Boolean)