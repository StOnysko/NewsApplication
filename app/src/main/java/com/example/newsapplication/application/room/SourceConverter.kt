package com.example.newsapplication.application.room

import androidx.room.TypeConverter
import com.example.newsapplication.application.model.network.Source

class SourceConverter {
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return source?.name
    }

    @TypeConverter
    fun toSource(sourceString: String): Source {
        return Source(sourceString, sourceString)
    }
}