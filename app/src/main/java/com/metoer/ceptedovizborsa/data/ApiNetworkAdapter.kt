package com.metoer.ceptedovizborsa.data

import android.util.Log
import com.metoer.ceptedovizborsa.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object ApiNetworkAdapter {
    fun appApi(): AppApi {
        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor { message ->
                Log.d(
                    "tag_retrofit_intercept",
                    "log: http log: $message"
                )
            }.setLevel(HttpLoggingInterceptor.Level.BODY))

        val retrofit = Retrofit
            .Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                QualifiedTypeConverterFactory(
                    GsonConverterFactory.create(),
                    SimpleXmlConverterFactory.create()
                )
            )
            .baseUrl(Constants.BASE_URL)
            .client(builder.build())
            .build()

        return retrofit.create(AppApi::class.java)
    }
}