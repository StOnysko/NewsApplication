package com.example.newsapplication.application.model.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapplication.application.model.network.Source
import java.util.UUID

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = false) var id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "source") val source: Source,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "url_to_image") val urlToImage: String?,
    @ColumnInfo(name = "published_at") val publishedAt: String?,
    @ColumnInfo(name = "content") val content: String?,
)