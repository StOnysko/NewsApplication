package com.example.newsapplication.application.viewmodel.repository

import androidx.lifecycle.LiveData
import com.example.newsapplication.application.model.localdb.Article
import com.example.newsapplication.application.retrofit.RetrofitInstance
import com.example.newsapplication.application.retrofit.api.ApiServices
import com.example.newsapplication.application.room.dao.ArticleDao

class ArticleRepository(
    private val articleDao: ArticleDao,
    private val apiServices: ApiServices = RetrofitInstance.getInstance()
) {
    val getArticleData: LiveData<List<Article>> = articleDao.getArticles()

    suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    suspend fun removeArticle(article: Article) {
        articleDao.removeArticle(article)
    }

    suspend fun fetchNewsFromInternet(countryCode: String, apiKey: String): List<Article> {
        return apiServices.getNewsResponse(countryCode, apiKey).articles.map { it.toArticle() }
    }
}