package org.dailykit.Model

import com.google.gson.annotations.SerializedName

/**
 * Created by Danish Rafique on 29-01-2019.
 */

data class IngredientDetailModel(@field:SerializedName("ingredient_detail_id")
                                 var ingredientDetailId: String,

                                 @field:SerializedName("ingredient_id")
                                 var ingredientId: String,

                                 @field:SerializedName("ingredient_name")
                                 var ingredientName: String,

                                 @field:SerializedName("ingredient_quantity")
                                 var ingredientQuantity: String,

                                 @field:SerializedName("ingredient_measure")
                                 var ingredientMeasure: String,

                                 @field:SerializedName("ingredient_section")
                                 var ingredientSection: String,

                                 @field:SerializedName("ingredient_process")
                                 var ingredientProcess: String,

                                 @field:SerializedName("ingredient_is_packed")
                                 var ingredientIsPacked: String,

                                 @field:SerializedName("ingredient_pack_timestamp")
                                 var ingredientPackTimestamp: String,

                                 @field:SerializedName("ingredient_is_deleted")
                                 var ingredientIsDeleted: String,

                                 @field:SerializedName("ingredient_is_weighed")
                                 var ingredientIsWeighed: String,

                                 @field:SerializedName("ingredient_detail_index")
                                 var ingredientDetailIndex: String,

                                 @field:SerializedName("ingredient_detail_position")
                                 var ingredientDetailPosition: String,

                                 @field:SerializedName("ingredient_measured_weight")
                                 var ingredientMeasuredWeight: String)