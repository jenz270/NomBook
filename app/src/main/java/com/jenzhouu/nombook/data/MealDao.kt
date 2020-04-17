package com.jenzhouu.nombook.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jenzhouu.nombook.model.Meal
import java.util.*

@Dao
interface MealDao {
    @Query("SELECT * from meal_table")
    fun getTopRecipes(): List<Meal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mealsList: List<Meal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Query("SELECT last_fetched from meal_table")
    fun getTimeTopMeal(): List<Date>?

    @Insert
    suspend fun addRecipe(meal: Meal)

    @Query("DELETE FROM meal_table")
    suspend fun deleteAll()

    @Query("DELETE FROM meal_table WHERE topRecipe = 1")
    suspend fun deleteTopRecipes()
}