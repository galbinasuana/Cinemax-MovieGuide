package com.example.cinemax.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class NewsArticle(
    val title: String,
    val imageUrl: String,
    val articleUrl: String,
    var content: String = "",
) : Parcelable {
    constructor(title: String, imageUrl: String, articleUrl: String) : this(
        title = title,
        imageUrl = imageUrl,
        articleUrl = articleUrl,
        content = ""
    )
}
fun NewsArticleEntity.toNewsArticle(): NewsArticle {
    return NewsArticle(
        title = this.title,
        imageUrl = this.imageUrl,
        articleUrl = this.articleUrl,
        content = this.content
    )
}