package com.jenzhouu.nombook.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meal (
    @SerializedName("idMeal")
    val recipeId: String,

    @SerializedName("strMeal")
    val name: String,

    @SerializedName("strArea")
    val category: String,

    @SerializedName("strInstructions")
    val instructions: String,

    @SerializedName("strMealThumb")
    val image: String,

    val ingredients: List<Ingredient>,

    @SerializedName("strSource")
    val sourceUrl: String
) : Parcelable

@Parcelize
data class Meals(
    @SerializedName("meals")
    val mealsList: List<Meal>
) : Parcelable