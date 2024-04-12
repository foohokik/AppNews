package com.example.appnews.presentation.headlines.headlines_adapterRV

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appnews.R
import com.example.appnews.databinding.NewsItemBinding
import com.example.appnews.data.dataclassesresponse.ArticlesUI

class ArticleViewHolder (val binding: NewsItemBinding):RecyclerView.ViewHolder(binding.root) {

val backgroundColor = ContextCompat.getColor(binding.itemLayout.context, R.color.backgroundColor)


    fun bind (article: ArticlesUI.Article, listener: ArticleListener, changeBackgroundColor:Boolean) = with(binding) {

        Glide.with(ivNewsHeadline.context).load(article.urlToImage).into(ivNewsHeadline)
        tvSourceNewsName.text = article.source.name
        tvTitle.text = article.title
        root.setOnClickListener {
            listener.onClickArticle(article)
        }

        if (changeBackgroundColor) {
            binding.itemLayout.setBackgroundColor(backgroundColor)
        }


    }

}