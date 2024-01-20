package com.example.appnews.data.database

import androidx.room.TypeConverter
import com.example.appnews.data.dataclassesresponse.Source

class Converters {


    @TypeConverter
    fun fromSource (source: Source): String{
        return source.name
    }

    @TypeConverter
    fun toSource (name: String): Source{
        return  Source(name, name)
    }
}