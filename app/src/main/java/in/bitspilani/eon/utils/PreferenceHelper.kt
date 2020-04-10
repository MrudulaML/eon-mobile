package `in`.bitspilani.eon.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder


/**
 * Singleton class for managing preferences for POJO or model class's object.
 * Only the fields are stored. Functions, Inner classes, Nested classes and inner interfaces are not stored.
 *
 */
object ModelPreferencesManager {

    //Shared Preference field used to save and retrieve JSON string
    lateinit var preferences: SharedPreferences

    //Name of Shared Preference file
    private const val PREFERENCES_FILE_NAME = "USER_PREFERENCE"

    /**
     * Call this first before retrieving or saving object.
     *
     * @param application Instance of application class
     */
    fun with(application: Application) {
        preferences = application.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        preferences.edit().putString(key, jsonString).apply()
    }

    /**
     * Saves values into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun putString(key: String,value:String) {
        //Save that String in SharedPreferences
        preferences.edit().putString(key, value).apply()
    }
    fun putInt(key: String,value:Int) {
        //Save that String in SharedPreferences
        preferences.edit().putInt(key, value).apply()
    }


    /**
     * get primitive values from the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun getString(key: String): String? {
        //Save that String in SharedPreferences
        return  preferences.getString(key, null)

    }
    fun getInt(key: String): Int? {
        //Save that String in SharedPreferences
        return  preferences.getInt(key, 100)

    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = preferences.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    /**
     * Used to clear all the cached data
     *
     **/
    fun clearCache(){
        preferences.edit().clear().apply()
    }
}