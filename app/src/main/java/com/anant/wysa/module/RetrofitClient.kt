package com.anant.wysa.module

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    private val pool = hashMapOf<String, Retrofit>()

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    fun getRetrofit(baseUrl: String): Retrofit {
        val oldInstance = pool[baseUrl]
        return if (oldInstance != null) {
            oldInstance
        } else {
            val newInstance = Retrofit.Builder().baseUrl(baseUrl).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            pool[baseUrl] = newInstance
            newInstance
        }
    }


}