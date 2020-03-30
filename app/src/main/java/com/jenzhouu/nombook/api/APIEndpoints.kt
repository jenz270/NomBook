package com.jenzhouu.nombook.api

import com.jenzhouu.nombook.BuildConfig
import com.jenzhouu.nombook.model.Meals
import retrofit2.Call
import retrofit2.http.GET

interface APIEndpoints {
    companion object {
        // TODO: Hide API Key
        private val API_KEY = BuildConfig.API_KEY
        val BASE_URL = "https://www.themealdb.com/api/json/v2/$API_KEY/"
    }

    @GET("random.php")
    fun randomRecipe(): Call<Meals>

    @GET("randomselection.php")
    fun topRecipes(): Call<List<Meals>>

    @GET("filter")
    fun searchRecipes(recipe: String): Call<List<Meals>>
}