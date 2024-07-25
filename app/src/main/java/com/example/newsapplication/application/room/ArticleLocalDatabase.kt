package com.example.newsapplication.application.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapplication.application.model.localdb.Article
import com.example.newsapplication.application.room.dao.ArticleDao

@Database([Article::class], exportSchema = false, version = 1)
@TypeConverters(SourceConverter::class)
abstract class ArticleLocalDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: ArticleLocalDatabase? = null

        fun getDatabaseInstance(context: Context): ArticleLocalDatabase {
            val tempdb = INSTANCE
            if (tempdb != null) {
                return tempdb
            } else {
                synchronized(this) {
                    val database =
                        Room.databaseBuilder(
                            context = context,
                            klass = ArticleLocalDatabase::class.java,
                            name = "test.db"
                        ).build()
                    INSTANCE = database
                    return database
                }
            }
        }
    }
}