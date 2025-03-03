package com.example.cinemax.service

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.os.IBinder
import com.example.cinemax.data.NewsArticleEntity
import com.example.cinemax.data.NewsDatabase
import com.example.cinemax.data.toNewsArticle
import com.example.cinemax.provider.NewsContentProvider
import com.example.cinemax.widget.NewsWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsUpdateService : Service() {

    private lateinit var newsDatabase: NewsDatabase

    override fun onCreate() {
        super.onCreate()
        newsDatabase = NewsDatabase.getDatabase(this)
        startNewsUpdates()
    }

    private fun startNewsUpdates() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    val articles = NewsWebScraping().getArticles()
                    if (!articles.isNullOrEmpty()) {
                        newsDatabase.newsArticleDao().deleteAllArticles()
                        articles.forEach { article ->
                            val articleEntity = NewsArticleEntity(
                                title = article.title,
                                imageUrl = article.imageUrl,
                                articleUrl = article.articleUrl,
                                content = article.content
                            )
                            newsDatabase.newsArticleDao().insertArticle(articleEntity)

                            val values = ContentValues().apply {
                                put("title", article.title)
                                put("image_url", article.imageUrl)
                                put("article_url", article.articleUrl)
                                put("content", article.content)
                            }
                            contentResolver.insert(NewsContentProvider.CONTENT_URI, values)
                        }
                        updateWidget()
                    }
                    Thread.sleep(60000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateWidget() {
        CoroutineScope(Dispatchers.IO).launch {
            val appWidgetManager = AppWidgetManager.getInstance(this@NewsUpdateService)
            val thisWidget = ComponentName(this@NewsUpdateService, NewsWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)

            val articles = newsDatabase.newsArticleDao().getAllArticles()
            if (articles.isNotEmpty()) {
                val randomArticle = articles.random()
                for (appWidgetId in appWidgetIds) {
                    NewsWidgetProvider.updateAppWidget(this@NewsUpdateService, appWidgetManager, appWidgetId, randomArticle.toNewsArticle())
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}