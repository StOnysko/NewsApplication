package com.example.newsapplication.application.ui.adapters.interfaces

import com.example.newsapplication.application.model.localdb.Article

interface OnArticleClickListener {
    fun onClickOpenArticle(article: Article)

    fun onAddArticleClickAction(article: Article)
}