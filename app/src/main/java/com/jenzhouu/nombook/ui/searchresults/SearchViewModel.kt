package com.jenzhouu.nombook.ui.searchresults

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jenzhouu.nombook.api.Result
import com.jenzhouu.nombook.api.Service
import com.jenzhouu.nombook.data.DefaultMealsRepository
import com.jenzhouu.nombook.data.MealRoomDatabase
import com.jenzhouu.nombook.model.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SearchViewModel (application: Application, private val service: Service, private val context: CoroutineContext = Dispatchers.IO) : AndroidViewModel(application), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = context

    private val repository: DefaultMealsRepository
    val searchRecipes: LiveData<Result<List<Meal>>>

    init {
        // Gets reference to mealDao from database to construct the correct repository
        val mealDao = MealRoomDatabase(application).mealDao()
        repository = DefaultMealsRepository(mealDao, service)
        searchRecipes = repository.searchResults
    }

    fun retrieveSearchedRecipes(query: String) {
        launch {
            repository.retrieveSearchMeals(query)
        }
    }
}