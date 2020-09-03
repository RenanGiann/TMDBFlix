package br.com.renangiannella.tmdbflix.data.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {

    companion object {
        private const val USER = "USER"
    }

    private val sharedPref: SharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE)

    fun getData(key: String): String? = sharedPref.getString(key, "")

    fun saveData(key: String, email: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, email)
        editor.apply()
    }

    fun saveImage(key: String, value: Int){
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(key,value)
        editor.apply()
    }

    fun getSavedImage(key: String): Int? = sharedPref.getInt(key, 0)

}