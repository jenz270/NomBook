package com.jenzhouu.nombook.data

import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.model.Meals

/**
 * Interface to the data layer.
 */
interface MealsRepository {
    suspend fun addMeal(meal: Meal)
    suspend fun addFavoriteMeal(meal: Meal)
    suspend fun retrieveTopMeals()
    suspend fun retrieveRandomMeals(): Result<Meals>
    suspend fun findMeal(query: String): Result<Meals>
}