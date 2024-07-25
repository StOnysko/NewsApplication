package com.example.newsapplication.application.retrofit

import com.example.newsapplication.application.retrofit.api.ApiServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL: String = "https://newsapi.org/v2/"

class RetrofitInstance {
    companion object {
        private var instance: ApiServices? = null

        @Synchronized
        fun getInstance(): ApiServices {
            if (instance == null)
                instance = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(ApiServices::class.java)
            return instance as ApiServices
        }
    }
}