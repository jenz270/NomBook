package com.jenzhouu.nombook.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service
import com.jenzhouu.nombook.data.DefaultMealsRepository
import com.jenzhouu.nombook.data.MealRoomDatabase
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.model.Meals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeViewModel (application: Application, private val service: Service, private val context: CoroutineContext = Dispatchers.IO) : AndroidViewModel(application), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = context

    private val repository: DefaultMealsRepository
    private val _randomRecipe = MutableLiveData<Result<Meals>>()
    private val randomRecipe: LiveData<Result<Meals>> = _randomRecipe
    private val _searchResults = MutableLiveData<Result<List<Meal>>>()
    private val searchResults: LiveData<Result<List<Meal>>> = _searchResults
    val topRecipes: LiveData<Result<Meals>>

    init {
        // Gets reference to mealDao from database to construct the correct repository
        val mealDao = MealRoomDatabase(application).mealDao()
        repository = DefaultMealsRepository(mealDao, service)
        topRecipes = repository.topRecipes
    }

    fun getRandomRecipe(): LiveData<Result<Meals>> {
        return randomRecipe
    }

    fun getSearchResults(): LiveData<Result<List<Meal>>> {
        return searchResults
    }

    fun retrieveRandomRecipe() {
        launch {
            _randomRecipe.postValue(Result.InProgress)
            service.retrieveRandomRecipe {result ->
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

    fun retrieveTopRecipes() {
        launch {
            repository.retrieveTopMeals()
        }
    }

//    fun retrieveSearchResults(searchQuery: String) {
//        // TODO: Implement the logic to get the data
//       // _searchResults.value = SampleData.mealsList
//    }

//    private fun onDataNotAvailable() {
//        _dataLoading.value = false
//    }

}