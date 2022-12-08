package com.androiddevs.myapplication.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.myapplication.repositary.NewsRepositary

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(val repositary: NewsRepositary) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(repositary) as T
    }


}