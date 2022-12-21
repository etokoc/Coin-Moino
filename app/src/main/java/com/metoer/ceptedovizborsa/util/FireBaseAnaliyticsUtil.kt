package com.metoer.ceptedovizborsa.util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*

class FireBaseAnaliyticsUtil(@ApplicationContext private val applicationContext: Context) {
    companion object {
        private var firebaseAnalytics: FirebaseAnalytics? = null
    }

    init {
        if (firebaseAnalytics == null)
            firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
    }

    fun startAnalytics() {
        firebaseAnalytics.apply {
            val countryInfo = Bundle()
            countryInfo.putString("Device_Country", Locale.getDefault().country)
            countryInfo.putString("Device_Language", Locale.getDefault().language)
            countryInfo.putString("Package_Name", applicationContext.packageName)
            this?.logEvent("Device_Information_Information", countryInfo)
        }
    }
}