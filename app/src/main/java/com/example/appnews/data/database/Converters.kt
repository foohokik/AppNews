package com.example.appnews.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.appnews.data.model.SourceResponse
import com.google.gson.Gson

object Converters  {

    private lateinit var gson: Gson
    fun initialize(gson: Gson){
        this.gson = gson
    }
    @TypeConverter
    fun fromSource (source: SourceResponse): String = gson.toJson(source)

    @TypeConverter
    fun toSource (source: String): SourceResponse = gson.fromJson(source, SourceResponse::class.java)
}