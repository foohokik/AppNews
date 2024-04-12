package com.example.appnews.presentation.headlines.headlines_adapterRV

import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.databinding.LoadingItemBinding

class LoadingViewHolder(val binding: LoadingItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind (loading: ArticlesUI.Loading) = with(binding) {

        //binding.progressBar.visibility = View.VISIBLE
    }
}