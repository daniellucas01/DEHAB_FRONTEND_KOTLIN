package com.example.dehab.repository

import com.example.dehab.Constants
import com.example.dehab.network.KeyProviderApiInterface
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSingleton {
    private const val BASE_URL = Constants.API_BASE_URL
    //BASE URL is localhost:3000
    val instance: KeyProviderApiInterface by lazy {
        val retrofit = Retrofit.Builder ()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        retrofit.create(KeyProviderApiInterface::class.java)
    }
}