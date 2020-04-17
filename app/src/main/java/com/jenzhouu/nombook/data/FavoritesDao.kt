package com.jenzhouu.nombook.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jenzhouu.nombook.model.Meal

@Dao
interface FavoritesDao {
    @Query("SELECT * from meal_table WHERE favoriteMeal = 'true'")
    fun getFavorites(): LiveData<List<Meal>>

    @Insert
    suspend fun addFavoriteRecipe(meal: Meal)    //TODO: Handle conflict

    @Query("DELETE FROM meal_table WHERE mealId = :mealId")
    suspend fun removeFavoriteRecipe(mealId: Int)
}