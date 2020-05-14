package org.dailykit.model

import com.google.gson.annotations.SerializedName

data class RepackRequestModel(@field:SerializedName("ingredient_id")
                              var ingredientId: String,

                              @field:SerializedName("is_weighed")
                              var isWeighed: Boolean,

                              @field:SerializedName("is_packed")
                              var isPacked: Boolean,

                              @field:SerializedName("is_labelled")
                              var isLabelled: Boolean)