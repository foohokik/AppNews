package com.example.appnews.presentation.source.adaptersources

import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.databinding.SourceItemBinding
import com.example.appnews.presentation.model.SourceUI

class ViewHolderSource(val binding: SourceItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(source: SourceUI, listener: SourceListener) = with(binding) {
        tvSourceName.text = source.name
        val text = "${source.category} | ${source.country}"
        tvCountrySource.text = text
        root.setOnClickListener {
            listener.onClickSource(source)
        }
    }

}