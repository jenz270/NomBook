package com.jenzhouu.nombook.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service
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
    private val _topRecipesList = MutableLiveData<Result<List<Meals>>>()
    private val topRecipesList: LiveData<Result<List<Meals>>> = _topRecipesList
    private val _searchResults = MutableLiveData<Result<List<Meals>>>()
    private val searchResults: LiveData<Result<List<Meals>>> = _searchResults

    fun getRandomRecipe(): LiveData<Result<Meals>> {
        return randomRecipe
    }

    fun getRecipes(): LiveData<Result<List<Meals>>> {
        return topRecipesList
    }

    fun getSearchResults(): LiveData<Result<List<Meals>>> {
        return searchResults
    }

    fun retrieveRandomRecipe() {
        // TODO: Implement the logic to get the data
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

    fun retrieveRecipes() {
        // TODO: Implement the logic to get the data
       // _topRecipesList.value = SampleData.mealsList
    }

    fun retrieveSearchResults(searchQuery: String) {
        // TODO: Implement the logic to get the data
       // _searchResults.value = SampleData.mealsList
    }

}