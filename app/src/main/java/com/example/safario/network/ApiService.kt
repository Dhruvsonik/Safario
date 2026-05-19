package com.example.safario.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // AI CHAT
    @POST("chat/completions")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ChatResponse

    // GOOGLE DIRECTIONS API
    @GET("maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin")
        origin: String,

        @Query("destination")
        destination: String,

        @Query("key")
        apiKey: String
    ): DirectionsResponse
    // WEATHER API
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location")
        location: String,

        @Query("radius")
        radius: Int,

        @Query("type")
        type: String,

        @Query("key")
        apiKey: String
    ): PlacesResponse
    @POST("v1/chat/completions")
    suspend fun getChatResponse(

        @Header("Authorization")
        authorization: String,

        @Body
        request: ChatRequest

    ): ChatResponse
}