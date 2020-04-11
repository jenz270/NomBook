package com.jenzhouu.nombook.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service
import com.jenzhouu.nombook.model.Meal
import com.jenzhouu.nombook.model.Meals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeViewModel (private val service: Service,  private val context: CoroutineContext = Dispatchers.IO) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = context

    private val _randomRecipe = MutableLiveData<Result<Meals>>()
    private val randomRecipe: LiveData<Result<Meals>> = _randomRecipe
    private val _topRecipesList = MutableLiveData<Result<Meals>>()
    private val topRecipesList: LiveData<Result<Meals>> = _topRecipesList
    private val _searchResults = MutableLiveData<Result<List<Meal>>>()
    private val searchResults: LiveData<Result<List<Meal>>> = _searchResults

    fun getRandomRecipe(): LiveData<Result<Meals>> {
        return randomRecipe
    }

    fun getRecipes(): LiveData<Result<Meals>> {
        return topRecipesList
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
            _topRecipesList.postValue(Result.InProgress)
            service.retrieveTopRecipes {result ->
                when (result) {
                    is Result.Success -> {
                        _topRecipesList.postValue(result)
                    }
                    is Result.Failure -> {
                        _topRecipesList.postValue(result)
                    }
                }

            }
        }
    }

    fun retrieveSearchResults(searchQuery: String) {
        // TODO: Implement the logic to get the data
       // _searchResults.value = SampleData.mealsList
    }

}