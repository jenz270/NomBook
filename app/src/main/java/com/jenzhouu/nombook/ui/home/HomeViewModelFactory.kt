package com.jenzhouu.nombook.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jenzhouu.nombook.api.Service

class HomeViewModelFactory(private val service: Service) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(service) as T
    }

}