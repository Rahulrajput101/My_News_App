package com.androiddevs.myapplication.ui.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androiddevs.myapplication.model.Article
import com.androiddevs.myapplication.model.NewsResponse
import com.androiddevs.myapplication.repositary.NewsRepositary
import com.androiddevs.myapplication.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(application: Application, val repositary: NewsRepositary) :
    AndroidViewModel(application) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null


    init {
        getBreakingNews("in")
    }


    fun getBreakingNews(countrycode: String) = viewModelScope.launch {
        safeBreakingNews(countrycode)
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    suspend fun safeBreakingNews(countrycode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnectioin()) {
                val response = repositary.getBreakingNews(countrycode, breakingNewsPage)
                breakingNews.postValue(handleBreakingResponse(response))
            } else {
                breakingNews.postValue(Resource.Error(" has no Internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error(" network failure"))
                else -> breakingNews.postValue(Resource.Error(" Conversion error"))
            }

        }

    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnectioin()) {
                val response = repositary.searchFornews(searchQuery!!, searchNewsPage)
                searchNews.postValue(handleSearchResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun handleBreakingResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
//                   val oldArticle = breakingNewsResponse?.articles
//                   val newArticle = resultResponse.articles
//                   oldArticle?.addAll(newArticle)
                    breakingNewsResponse?.articles?.addAll(resultResponse.articles)

                }

                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())

    }

    private fun handleSearchResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
//                    val oldArticle = searchNewsResponse?.articles
//                    val newArticle = resultResponse.articles
//                    oldArticle?.addAll(newArticle)
                    searchNewsResponse?.articles?.addAll(resultResponse.articles)
                }

                return Resource.Success(searchNewsResponse ?: resultResponse)
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

    private fun hasInternetConnectioin(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        //Build.VERSION_CODES.M is the api 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
                else -> false
            }
        } else {

            connectivityManager.activeNetworkInfo?.isConnected.run {
                return when (connectivityManager.activeNetworkInfo?.type) {
                    TYPE_WIFI -> return true
                    TYPE_MOBILE -> return true
                    TYPE_ETHERNET -> return true
                    else -> false
                }
            }
//            // if the android version is below M
//            @Suppress("DEPRECATION") val networkInfo =
//                connectivityManager.activeNetworkInfo ?: return false
//            @Suppress("DEPRECATION")
//            return networkInfo.isConnected
        }
    }


}