package com.example.appnews.presentation.source.AdapterSources

import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.data.dataclassesresponse.SourceFromSources
import com.example.appnews.databinding.SourceItemBinding

class ViewHolderSource(val binding:SourceItemBinding ): RecyclerView.ViewHolder(binding.root){

    fun bind (source: SourceFromSources, listener: SourceListener) = with(binding) {

        tvSourceName.text = source.name
        tvCountrySource.text = source.category + " | " + source.country
        root.setOnClickListener {
            listener.onClickSource(source)
        }
    }


}