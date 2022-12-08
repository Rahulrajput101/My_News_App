package com.androiddevs.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.myapplication.model.Article


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article) : Long

    @Query("SELECT * FROM articles")
    fun getAllData() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}