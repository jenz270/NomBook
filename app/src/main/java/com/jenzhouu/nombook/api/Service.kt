package com.jenzhouu.nombook.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.model.Meals
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Service {
    companion object {
        private const val TAG = "Service"
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(Meal::class.java, MealDeserializer())
        .create()

    private val api: APIEndpoints by lazy {
        val retrofit = Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl(APIEndpoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create<APIEndpoints>(APIEndpoints::class.java)
    }

    fun retrieveRandomRecipe(callback: (result: Result<Meals>) -> Unit) {
        try {
            val response: Response<Meals> = api.randomRecipe().execute()
            if (response.isSuccessful) {
                callback.invoke(Result.Success(response.body()))
            } else {
                callback.invoke(Result.Failure(response.message()))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Meals Retrieval Failed", e)
            callback.invoke(Result.Failure(e.message ?: ""))
        }
    }

    fun retrieveTopRecipes(callback: (result: Result<Meals>) -> Unit) {
        try {
            val response: Response<Meals> = api.topRecipes().execute()
            if (response.isSuccessful) {
                callback.invoke(Result.Success(response.body()))
            } else {
                callback.invoke(Result.Failure(response.message()))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Meals Retrieval Failed", e)
            callback.invoke(Result.Failure(e.message ?: ""))
        }
    }

    fun searchRecipes(recipe: String, callback: (result: Result<Meals>) -> Unit) {
        try {
            val response: Response<Meals> = api.searchRecipes(recipe).execute()
            if (response.isSuccessful) {
                callback.invoke(Result.Success(response.body()))
            } else {
                callback.invoke(Result.Failure(response.message()))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Meals Retrieval Failed", e)
            callback.invoke(Result.Failure(e.message ?: ""))
        }
    }

    fun retrieveRecipe(id: String, callback: (result: Result<Meal>) -> Unit) {
        try {
            val response: Response<Meal> = api.retrieveRecipe(id).execute()
            if (response.isSuccessful) {
                callback.invoke(Result.Success(response.body()))
            } else {
                callback.invoke(Result.Failure(response.message()))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Meals Retrieval Failed", e)
            callback.invoke(Result.Failure(e.message ?: ""))
        }
    }
}
