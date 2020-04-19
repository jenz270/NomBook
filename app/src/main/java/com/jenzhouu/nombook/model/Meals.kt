package com.jenzhouu.nombook.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "meal_table")
data class Meal(
    @PrimaryKey
    @ColumnInfo(name = "mealId")
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

    val ingredients: List<Ingredient?>? = listOf(),

    val topRecipe: Boolean = false,

    var favoriteMeal: Boolean = false,

    @ColumnInfo(name = "last_fetched")
    @Expose(deserialize = false)
    var created: Date = Date()
) : Parcelable

@Parcelize
data class Meals(
    @SerializedName("meals")
    val mealsList: List<Meal> = listOf()
) : Parcelable