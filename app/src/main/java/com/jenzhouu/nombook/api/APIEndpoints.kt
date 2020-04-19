package com.jenzhouu.nombook.api

import com.jenzhouu.nombook.BuildConfig
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.model.Meals
import retrofit2.Call
import retrofit2.http.GET

interface APIEndpoints {
    companion object {
        private const val API_KEY = BuildConfig.API_KEY
        const val BASE_URL = "https://www.themealdb.com/api/json/v2/$API_KEY/"
    }

    @GET("random.php")
    fun randomRecipe(): Call<Meals>

    @GET("randomselection.php")
    fun topRecipes(): Call<Meals>

    @GET("filter")
    fun searchRecipes(recipe: String): Call<Meals>

    @GET("filter")
    fun retrieveRecipe(id: String): Call<Meal>
}