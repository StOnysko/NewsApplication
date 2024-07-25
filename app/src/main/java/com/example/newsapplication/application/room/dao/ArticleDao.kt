package com.example.newsapplication.application.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapplication.application.model.localdb.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY id ASC")
    fun getArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Delete
    suspend fun removeArticle(article: Article)
}