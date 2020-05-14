package org.dailykit.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Danish Rafique on 29-01-2019.
 */

data class IngredientModel(@field:SerializedName("ingredient_id")
                           var ingredientId: String,

                           @field:SerializedName("item_id")
                           var itemId: String,

                           @field:SerializedName("ingredient_index")
                           var ingredientIndex: String,

                           @field:SerializedName("slip_name")
                           var slipName: String,

                           @field:SerializedName("ingredient_is_packed_complete")
                           var ingredientIsPackedComplete: String,

                           @field:SerializedName("ingredient_is_deleted")
                           var ingredientIsDeleted: String,

                           @field:SerializedName("ingredient_is_labeled")
                           var ingredientIsLabeled: String,

                           @field:SerializedName("selected_ingredient_position")
                           var selectedIngredientPosition: String,

                           @field:SerializedName("ingredient_measured_total_weight")
                           var ingredientMeasuredTotalWeight: String,

                           @field:SerializedName("ingredient_details")
                           var ingredientDetails: ArrayList<IngredientDetailModel>)