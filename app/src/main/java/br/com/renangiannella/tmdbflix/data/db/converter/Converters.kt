package br.com.renangiannella.tmdbflix.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromList(list: List<Int?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(string: String?): List<Int?>? {
        val listType = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(string, listType)
    }
}