package com.jenzhouu.nombook.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jenzhouu.nombook.model.Recipe
import com.jenzhouu.nombook.model.SampleData

class HomeViewModel : ViewModel() {

    private val _topRecipesList = MutableLiveData<List<Recipe>>()
    private val topRecipesList: LiveData<List<Recipe>> = _topRecipesList

    fun getRecipes(): LiveData<List<Recipe>> {
        return topRecipesList
    }

    fun retrieveRecipes() {
        _topRecipesList.value = SampleData.recipes
    }
}