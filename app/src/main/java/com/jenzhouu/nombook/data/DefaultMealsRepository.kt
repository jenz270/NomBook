package com.jenzhouu.nombook.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.model.Meals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class DefaultMealsRepository(
    private val mealDao: MealDao,
    private val service: Service,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MealsRepository {
    companion object {
        private const val TIMEOUT_IN_DAYS = 1
        private const val TAG = "DefaultMealsRepository"
    }

    private val _topRecipes = MutableLiveData<Result<Meals>>()
    val topRecipes: LiveData<Result<Meals>> = _topRecipes
    private val _randomRecipe = MutableLiveData<Result<Meals>>()
    val randomRecipe: LiveData<Result<Meals>> = _randomRecipe
    private val _searchResults = MutableLiveData<Result<List<Meal>>>()
    val searchResults: LiveData<Result<List<Meal>>> = _searchResults

    override suspend fun addMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    override suspend fun addFavoriteMeal(meal: Meal) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //favoritesDao.addFavoriteRecipe(meal)
    }

    override suspend fun retrieveTopMeals() {
        // if the database is empty, just retrieve, else we will need to check.
        // if time is less than 24 hours, we will not retrieve from api, but from database.
        val lastRetrievalTime: Date? = if (mealDao.getTimeTopMeal().isNullOrEmpty()) {
            Date(0)
        } else {
            mealDao.getTimeTopMeal()?.get(0)
        }
        val mealList = mealDao.getTopRecipes()
        val checked = shouldRefreshData(lastRetrievalTime)
        if (checked || mealList.isEmpty()) {
            mealDao.deleteTopRecipes()
            retrieveTopMealsFromApi()
            insertTopRecipes(topRecipes.value)
        } else {
            val mealsItem = Meals(mealList)
            _topRecipes.postValue(Result.InProgress)
            if (mealList.isNullOrEmpty()) {
                _topRecipes.postValue(Result.Failure("Unable to retrieve meal list from database"))
            } else {
                _topRecipes.postValue(Result.Success(mealsItem))
            }
        }
    }

    override suspend fun retrieveSearchMeals(query: String) {
        withContext(dispatcher) {
            _searchResults.postValue(Result.InProgress)
            service.searchRecipes(query) { result ->
                when (result) {
                    is Result.Success -> {
                        retrieveListOfRecipes(result.data)
                    }
                    is Result.Failure -> {
                        _searchResults.postValue(result)
                    }
                }
            }
        }
    }

    override suspend fun retrieveRandomMeals() {
        withContext(dispatcher) {
            _randomRecipe.postValue(Result.InProgress)
            service.retrieveRandomRecipe { result ->
                when (result) {
                    is Result.Success -> {
                        _randomRecipe.postValue(result)
                    }
                    is Result.Failure -> {
                        _randomRecipe.postValue(result)
                    }
                }
            }
        }
    }

    private suspend fun retrieveTopMealsFromApi() {
        withContext(dispatcher) {
            _topRecipes.postValue(Result.InProgress)
            service.retrieveTopRecipes { result ->
                when (result) {
                    is Result.Success -> {
                        _topRecipes.postValue(result)
                    }
                    is Result.Failure -> {
                        _topRecipes.postValue(result)
                    }
                }
            }
        }
    }

    override suspend fun insertTopRecipes(meals: Result<Meals>?) {
        if (meals != null) {
            when (meals) {
                is Result.Success -> {
                    val resultData = setTopRecipesTrue(meals.data?.mealsList)
                    mealDao.insertAll(resultData)
                }
                is Result.Failure -> {
                    Log.e(
                        TAG,
                        "Insert top recipes to database is not possible as data is not correct."
                    )
                }
            }
        }
    }

    private fun shouldRefreshData(savedTime: Date?): Boolean {
        // TODO: Check the math on this
        if (savedTime != null) {
            val diffInDays = ((Date().time - savedTime.time) / (1000 * 60 * 60 * 24))
            if (diffInDays >= TIMEOUT_IN_DAYS) {
                return true
            }
        }
        return false
    }

    private fun setTopRecipesTrue(mealList: List<Meal>?): List<Meal> {
        if (mealList == null) {
            return listOf()
        }
        return mealList.map { it.copy(topRecipe = true) }
    }

    private fun retrieveListOfRecipes(result: Meals?) {
        val mealList = result?.mealsList
        val resultList = mutableListOf<Meal>()

        mealList?.map {
            service.retrieveRecipe(it.recipeId) { result ->
                when (result) {
                    is Result.Success -> {
                        if (result.data != null) {
                            resultList.add(result.data)
                        }
                    }
                    is Result.Failure -> {
                        Log.e(TAG, "Failed to retrieve recipe with id: ${it.recipeId}")
                    }
                }
            }
        }
        _searchResults.value = Result.Success(resultList)
    }
}