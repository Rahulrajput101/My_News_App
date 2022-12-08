package com.androiddevs.myapplication.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.myapplication.model.Article
import com.androiddevs.myapplication.model.NewsResponse
import com.androiddevs.myapplication.repositary.NewsRepositary
import com.androiddevs.myapplication.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repositary: NewsRepositary) : ViewModel() {

    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
            var breakingNewsPage =1

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage =1


    init {
        getBreakingNews("us")
    }




    fun getBreakingNews(countrycode: String) = viewModelScope.launch {

        breakingNews.postValue(Resource.Loading())
        val response = repositary.getBreakingNews(countrycode,breakingNewsPage)
        breakingNews.postValue(handleBreakingResponse(response))


    }

    fun getSearchNews(searchQuery : String ) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repositary.searchFornews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchResponse(response))
    }


    private fun handleBreakingResponse( response: Response<NewsResponse>) : Resource<NewsResponse>{
       if(response.isSuccessful){
           response.body()?.let {
               return Resource.Success(it)
           }
       }

        return Resource.Error(response.message())

    }

    private fun handleSearchResponse( response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())

    }


    fun saveArticle(article: Article) = viewModelScope.launch {
        repositary.upsert(article)
    }

    fun getSavedNews() = repositary.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repositary.deleteArticle(article)

    }

}