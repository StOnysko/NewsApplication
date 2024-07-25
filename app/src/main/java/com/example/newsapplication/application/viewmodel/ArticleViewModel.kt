package com.example.newsapplication.application.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplication.application.model.localdb.Article
import com.example.newsapplication.application.viewmodel.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleViewModel(
    private val repository: ArticleRepository
) : ViewModel() {

    val localArticles: LiveData<List<Article>> = repository.getArticleData
    private val articles: MutableLiveData<List<Article>> = MutableLiveData()
    fun articles(): LiveData<List<Article>> = articles

    fun insertArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertArticle(article)
        }
    }

    fun removeArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeArticle(article)
        }
    }

    fun fetchArticlesFromInternet(countryCode: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.fetchNewsFromInternet(countryCode, apiKey)
                withContext(Dispatchers.Main) {
                    articles.value = result
                }
            } catch (e: Exception) {
                Log.d("API_NEWS", "${e.message}")
            }
        }
    }
}