package com.example.androidfinal.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfinal.R
import com.example.androidfinal.core.data.db.NewsArticleEntity
import com.example.androidfinal.core.data.model.NewsArticle


class NewsAdapter(private val articles: List<NewsArticleEntity>, private val listener: OnArticleClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_FEATURED = 0
    private val VIEW_TYPE_STANDARD = 1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_FEATURED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_featured_news,parent,false)
                FeaturedViewHolder(view)
            }
            VIEW_TYPE_STANDARD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_standard_news,parent,false)
                StandardViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val article = articles[position]

        when(holder.itemViewType){
            VIEW_TYPE_FEATURED -> {
                (holder as FeaturedViewHolder).bind(article)
            }
            VIEW_TYPE_STANDARD -> {
                (holder as StandardViewHolder).bind(article)
            }
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            VIEW_TYPE_FEATURED
        }else{
            VIEW_TYPE_STANDARD
        }
    }

    inner class FeaturedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val featuredImage: ImageView = itemView.findViewById(R.id.featured_image)
        val featuredTitle: TextView = itemView.findViewById(R.id.featured_title)
        val featuredSummary: TextView = itemView.findViewById(R.id.featured_summary)


        fun bind(article: NewsArticleEntity){
            featuredTitle.text = article.title
            featuredSummary.text = article.description
            if (!article.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(article.imageUrl)
                    .placeholder(R.drawable.news_placeholder)
                    .error(R.drawable.error)
                    .into(featuredImage)
            } else {
                featuredImage.setImageResource(R.drawable.news_placeholder)
            }


            itemView.setOnClickListener {
                listener.onArticleClicked(article)
            }
        }
    }

    inner class StandardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val standardImage: ImageView = itemView.findViewById(R.id.standard_image)
        val standardTitle: TextView = itemView.findViewById(R.id.standard_title)
        val standardMetadata: TextView = itemView.findViewById(R.id.standard_metadata)

        fun bind(article: NewsArticleEntity){
            standardTitle.text = article.title
            standardMetadata.text = article.author ?: "Unknown Author"
            if (!article.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(article.imageUrl)
                    .placeholder(R.drawable.news_placeholder)
                    .error(R.drawable.error)
                    .into(standardImage)
            } else {
                standardImage.setImageResource(R.drawable.news_placeholder)
            }
            itemView.setOnClickListener {
                listener.onArticleClicked(article)
            }
        }

    }
}