package com.androiddevs.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.myapplication.model.Article
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(
    entities = [Article::class],
    version =1
)
@TypeConverters(Converters::class)
 abstract class ArticleDatabase : RoomDatabase() {

     abstract fun getArticleDao() : ArticleDao

     companion object{
         @Volatile
         private var instance : ArticleDatabase? = null
         private val LOCK  = Any()

         @OptIn(InternalCoroutinesApi::class)
         operator fun invoke(context: Context) = instance?: synchronized(LOCK){
             instance ?: createDatabase(context).also { instance =it}

         }
         fun createDatabase(context: Context) =
             Room.databaseBuilder(context.applicationContext, ArticleDatabase::class.java,
             "articleDb.db").build()


     }

}