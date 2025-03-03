package com.example.cinemax.data

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsArticleDao {

    @Query("SELECT * FROM news_articles")
    fun getAllArticles(): List<NewsArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: NewsArticleEntity): Long

    @Query("DELETE FROM news_articles")
    suspend fun deleteAllArticles()

    @Query("SELECT * FROM news_articles")
    fun getAllArticlesCursor(): Cursor
}