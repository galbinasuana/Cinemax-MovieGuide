package com.example.cinemax.service

import com.example.cinemax.data.NewsArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class NewsWebScraping {

    suspend fun getArticles(): List<NewsArticle>? {
        return withContext(Dispatchers.IO) {
            try {
                val doc: Document = Jsoup.connect("https://m.imdb.com/news/top/").get()
                val articlesHtml = doc.select("div.ipc-list-card--base")

                if (articlesHtml != null) {
                    val articleList = mutableListOf<NewsArticle>()

                    for (node in articlesHtml) {
                        val titleNode: Element? = node.selectFirst("a[href]")
                        val title: String? = titleNode?.text()?.trim()
                        val articleUrl: String? = titleNode?.attr("href")
                        val imgNode: Element? = node.selectFirst("img")
                        val imageUrl: String? = imgNode?.attr("src")

                        if (!imageUrl.isNullOrEmpty() && !title.isNullOrEmpty() && !articleUrl.isNullOrEmpty()) {
                            articleList.add(NewsArticle(title ?: "", imageUrl, articleUrl ?: ""))
                        }
                    }
                    articleList
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun setArticleContent(newsArticle: NewsArticle): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val doc: Document = Jsoup.connect(newsArticle.articleUrl).get()
                val paragraphs = doc.select("p.paragraph")

                if (paragraphs != null) {
                    val content = StringBuilder()
                    for (paragraph in paragraphs) {
                        content.append(paragraph.text().trim()).append("\n\n")
                    }
                    newsArticle.content = content.toString()
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}