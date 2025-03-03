package com.example.cinemax.ui.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.cinemax.R
import com.example.cinemax.data.NewsArticle
import com.example.cinemax.databinding.ActivityArticleContentBinding
import com.example.cinemax.service.NewsWebScraping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityArticleContentBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_content)

        val article: NewsArticle? = intent.getParcelableExtra("article")
        binding.article = article

        article?.let {
            CoroutineScope(Dispatchers.Main).launch {
                if (it.content.isEmpty()) {
                    val success = withContext(Dispatchers.IO) {
                        NewsWebScraping().setArticleContent(it)
                    }
                    if (success) {
                        binding.article = it
                    }
                }
                Glide.with(this@ArticleContentActivity).load(it.imageUrl).into(binding.articleImage)
                binding.articleTitle.text = it.title
                binding.articleContent.text = it.content
            }
        }
    }
}