package com.metoer.ceptedovizborsa.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPrefencesUtil(context: Context) {
    private var local: SharedPreferences

    init {
        local = context.getSharedPreferences(Constants.SHARED_PREFENCES_KEY, Context.MODE_PRIVATE)
    }

    fun addLocal(key: String, data: Any) {
        local.edit().apply {
            when (data) {
                is String -> {
                    this.putString(key, data)
                }
                is Boolean -> {
                    this.putBoolean(key, data)
                }
                is Int -> {
                    this.putInt(key, data)
                }
                is Float -> {
                    this.putFloat(key, data)
                }
                else -> {
                    Log.e("SHARED_PREFENCES_ERROR", "your shared data is not avaible")
                }
            }
        }.apply()
    }

    fun getLocal(key: String, dataType: Any): Any {
        return when (dataType) {
            String -> {
                local.getString(key, "")!!
            }
            Boolean -> {
                local.getBoolean(key, false)
            }
            Int -> {
                local.getInt(key, 0)
            }
            Float -> {
                local.getFloat(key, 0f)
            }
            else -> {
                Log.e("SHARED_PREFENCES_ERROR", "your shared data is not avaible")
            }
        }
    }
}