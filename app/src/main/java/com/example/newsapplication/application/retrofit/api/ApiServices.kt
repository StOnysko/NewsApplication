package com.example.newsapplication.application.retrofit.api

import com.example.newsapplication.application.model.network.News
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("top-headlines")
    suspend fun getNewsResponse(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): News
}