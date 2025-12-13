package com.example.androidfinal.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfinal.R
import com.example.androidfinal.core.data.model.NewsArticle

class SearchAdapter(
    private val articles: List<NewsArticle>,
    private val onArticleClick: (NewsArticle) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_standard_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.standard_title)
        private val metadataView: TextView = itemView.findViewById(R.id.standard_metadata)
        private val imageView: ImageView = itemView.findViewById(R.id.standard_image)

        fun bind(article: NewsArticle) {
            titleView.text = article.title
            metadataView.text = article.source.name ?: "Unknown Source"

            if (!article.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(article.imageUrl)
                    .placeholder(R.drawable.news_placeholder)
                    .error(R.drawable.error)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.news_placeholder)
            }

            itemView.setOnClickListener {
                onArticleClick(article)
            }
        }
    }
}