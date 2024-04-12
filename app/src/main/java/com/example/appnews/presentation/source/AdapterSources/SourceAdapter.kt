package com.example.appnews.presentation.source.AdapterSources

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.data.dataclassesresponse.SourceFromSources
import com.example.appnews.databinding.SourceItemBinding

class SourceAdapter(private var listener: SourceListener): RecyclerView.Adapter<ViewHolderSource>() {

    private var items = mutableListOf<SourceFromSources>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSource {

        val inflater = LayoutInflater.from(parent.context)
        val  binding = SourceItemBinding.inflate(inflater, parent, false)
        return ViewHolderSource(binding)

    }

    override fun onBindViewHolder(holder: ViewHolderSource, position: Int) {

        holder.bind(items[position], listener)

    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<SourceFromSources>) {
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