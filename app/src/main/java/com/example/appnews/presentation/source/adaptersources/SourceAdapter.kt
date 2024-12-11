package com.example.appnews.presentation.source.adaptersources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.databinding.SourceItemBinding
import com.example.appnews.presentation.model.SourceUI

class SourceAdapter(private var listener: SourceListener) :
    RecyclerView.Adapter<ViewHolderSource>() {

    private var items = mutableListOf<SourceUI>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSource {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SourceItemBinding.inflate(inflater, parent, false)
        return ViewHolderSource(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderSource, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<SourceUI>) {
        val diffResult = DiffUtil.calculateDiff(
            SourceDiffUtil(
                items,
                newItems
            )
        )
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

}