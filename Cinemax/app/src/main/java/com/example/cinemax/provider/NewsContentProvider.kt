package com.example.cinemax.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.cinemax.data.NewsArticleEntity
import com.example.cinemax.data.NewsDatabase
import kotlinx.coroutines.runBlocking

class NewsContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.cinemax.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/news")
    }

    private lateinit var newsDatabase: NewsDatabase

    override fun onCreate(): Boolean {
        newsDatabase = NewsDatabase.getDatabase(context as Context)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var id: Long = 0
        runBlocking {
            val article = NewsArticleEntity(
                title = values?.getAsString("title") ?: "",
                imageUrl = values?.getAsString("image_url") ?: "",
                articleUrl = values?.getAsString("article_url") ?: "",
                content = values?.getAsString("content") ?: ""
            )
            id = newsDatabase.newsArticleDao().insertArticle(article)
        }
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return newsDatabase.newsArticleDao().getAllArticlesCursor()
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}