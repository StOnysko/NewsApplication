package com.example.newsapplication.application.model.network

data class News(
    val articles: List<NetworkArticle>,
    val status: String,
    val totalResults: Int
)