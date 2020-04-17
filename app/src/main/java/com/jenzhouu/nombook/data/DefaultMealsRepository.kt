package com.jenzhouu.nombook.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.model.Meals
import kotlinx.coroutines.*
import java.util.*


class DefaultMealsRepository(
    private val mealDao: MealDao,
    private val service: Service,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MealsRepository {
    companion object {
        private const val TIMEOUT_IN_DAYS = 1
    }

    private val _topRecipes = MutableLiveData<Result<Meals>>()
    val topRecipes: LiveData<Result<Meals>> = _topRecipes

    override suspend fun addMeal(meal: Meal) {
        GlobalScope.launch {
            mealDao.insertMeal(meal)
        }
    }

    override suspend fun addFavoriteMeal(meal: Meal) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //favoritesDao.addFavoriteRecipe(meal)
    }

    override suspend fun retrieveTopMeals() {
        // if the database is empty, just retrieve, else we will need to check.
        // check if the top meals had already been retrieved
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

    override suspend fun retrieveRandomMeals(): Result<Meals> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun findMeal(query: String): Result<Meals> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private suspend fun retrieveTopMealsFromApi() {
        withContext(dispatcher) {
            _topRecipes.postValue(Result.InProgress)
            service.retrieveTopRecipes { result ->
                when (result) {
                    is Result.Success -> {
                        _topRecipes.postValue(result)
                        GlobalScope.launch {
                            if (result.data?.mealsList != null) {
                                val resultData = setTopRecipesTrue(result.data.mealsList)
                                mealDao.insertAll(resultData)
                            }
                        }
                    }
                    is Result.Failure -> {
                        _topRecipes.postValue(result)
                    }
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

    private fun setTopRecipesTrue(mealList: List<Meal>): List<Meal> {
        return mealList.map { it.copy(topRecipe = true) }
    }
}