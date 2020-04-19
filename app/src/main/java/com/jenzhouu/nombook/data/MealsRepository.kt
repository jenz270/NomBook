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
    suspend fun insertTopRecipes(meals: Result<Meals>?)
    suspend fun retrieveSearchMeals(searchQuery: String)
    suspend fun retrieveRandomMeals()
}