package com.example.newsapplication.application.model.network

import com.example.newsapplication.application.model.localdb.Article

data class NetworkArticle(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
) {
    fun toArticle() =
        Article(
            source = source,
            author = author,
            title = title,
            description = description,
            url = url,
            urlToImage = urlToImage,
            publishedAt = publishedAt,
            content = content
        )
}