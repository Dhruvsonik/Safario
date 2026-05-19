package com.example.safario.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DirectionsRetrofit {

    private const val BASE_URL =
        "https://maps.googleapis.com/"

    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}