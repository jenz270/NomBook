package com.jenzhouu.nombook.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jenzhouu.nombook.api.Service
import com.jenzhouu.nombook.ui.home.HomeViewModel

class ViewModelFactory(private val application: Application, private val service: Service) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(
            application,
            service
        ) as T
    }

}