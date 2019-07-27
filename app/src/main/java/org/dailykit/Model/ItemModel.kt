package org.dailykit.Model

import com.google.gson.annotations.SerializedName

/**
 * Created by Danish Rafique on 29-01-2019.
 */
data class ItemModel(@field:SerializedName("item_order_id")
                     var itemOrderId: String,

                     @field:SerializedName("order_id")
                     var orderId: String,

                     @field:SerializedName("customer_phone")
                     var customerPhone: String,

                     @field:SerializedName("customer_name")
                     var customerName: String,

                     @field:SerializedName("customer_address")
                     var customerAddress: String,

                     @field:SerializedName("merchant_id")
                     var merchantId: String,

                     @field:SerializedName("recipe_sku")
                     var recipeSku: String,

                     @field:SerializedName("recipe_name")
                     var recipeName: String,

                     @field:SerializedName("recipe_cuisine")
                     var recipeCuisine: String,

                     @field:SerializedName("recipe_servings")
                     var recipeServings: String,

                     @field:SerializedName("recipe_quantity")
                     var recipeQuantity: String,

                     @field:SerializedName("recipe_price")
                     var recipePrice: String,

                     @field:SerializedName("sub_total")
                     var subTotal: String,

                     @field:SerializedName("discount_percentage")
                     var discountPercentage: String,

                     @field:SerializedName("discount")
                     var discount: String,

                     @field:SerializedName("new_customer_discount")
                     var newCustomerDiscount: String,

                     @field:SerializedName("sgst")
                     var sgst: String,

                     @field:SerializedName("cgst")
                     var cgst: String,

                     @field:SerializedName("delivery_charges")
                     var deliveryCharges: String,

                     @field:SerializedName("total_price")
                     var totalPrice: String,

                     @field:SerializedName("grcash")
                     var grCash: String,

                     @field:SerializedName("walletcash")
                     var walletCash: String,

                     @field:SerializedName("final_amount")
                     var finalAmount: String,

                     @field:SerializedName("payment_type")
                     var paymentType: String,

                     @field:SerializedName("payment_status")
                     var paymentStatus: String,

                     @field:SerializedName("add_notes")
                     var addNotes: String,

                     @field:SerializedName("delivery_time")
                     var deliveryTime: String,

                     @field:SerializedName("order_at")
                     var orderAt: String,

                     @field:SerializedName("order_cancel_till")
                     var orderCancelTill: String,

                     @field:SerializedName("order_number")
                     var orderNumber: String,

                     @field:SerializedName("delivery_expected")
                     var deliveryExpected: String,

                     @field:SerializedName("dispatch_real")
                     var dispatchReal: String,

                     @field:SerializedName("number_of_ingredients")
                     var numberOfIngredients: String,

                     @field:SerializedName("selected_position")
                     var selectedPosition: String,

                     @field:SerializedName("ingredients")
                     var ingredients:ArrayList<IngredientModel>)