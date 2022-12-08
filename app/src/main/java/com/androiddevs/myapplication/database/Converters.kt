package com.androiddevs.myapplication.database

import androidx.room.TypeConverter
import com.androiddevs.myapplication.model.Article
import com.androiddevs.myapplication.model.Source

class Converters {

     @TypeConverter
    fun fromSource(source: Source) : String{
        return source.name
    }

     @TypeConverter
    fun t0Source(name :String) : Source{
        return Source(name,name)
    }
}