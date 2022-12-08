package com.androiddevs.myapplication.repositary

import com.androiddevs.myapplication.api.RetrofitInstance
import com.androiddevs.myapplication.database.ArticleDatabase
import com.androiddevs.myapplication.model.Article
import retrofit2.Retrofit

class NewsRepositary(val db : ArticleDatabase) {

    suspend fun getBreakingNews(countryCode : String, pageNumber :Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchFornews(searchQuery : String, searchPageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,searchPageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllData()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)



}