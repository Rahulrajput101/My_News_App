package com.androiddevs.myapplication.ui.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.myapplication.repositary.NewsRepositary

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(val repositary: NewsRepositary, val application: Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(application,repositary) as T
    }


}