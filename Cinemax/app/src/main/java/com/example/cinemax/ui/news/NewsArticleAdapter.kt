package com.example.cinemax.ui.news

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemax.R
import com.example.cinemax.data.NewsArticle
import com.example.cinemax.service.NewsWebScraping
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsArticleAdapter : RecyclerView.Adapter<NewsArticleAdapter.NewsArticleViewHolder>() {

    private var articles: List<NewsArticle> = listOf()

    fun submitList(articles: List<NewsArticle>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_article, parent, false)
        return NewsArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class NewsArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.articleTitle)
        private val imageView: ImageView = itemView.findViewById(R.id.articleImage)
        private val readMoreButton: Button = itemView.findViewById(R.id.readMoreButton)

        fun bind(newsArticle: NewsArticle) {
            titleTextView.text = newsArticle.title
            Glide.with(itemView.context).load(newsArticle.imageUrl).into(imageView)

            readMoreButton.setOnClickListener {
                val context = itemView.context
                CoroutineScope(Dispatchers.Main).launch {
                    val success = withContext(Dispatchers.IO) {
                        NewsWebScraping().setArticleContent(newsArticle)
                    }
                    if (success) {
                        val intent = Intent(context, ArticleContentActivity::class.java)
                        intent.putExtra("article", newsArticle)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Failed to load the article content. Please try again later.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}