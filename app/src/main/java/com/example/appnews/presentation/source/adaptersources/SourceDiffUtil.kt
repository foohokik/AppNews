package com.example.appnews.presentation.source.adaptersources

import androidx.recyclerview.widget.DiffUtil
import com.example.appnews.presentation.model.SourceUI

class SourceDiffUtil(
    private val oldList: MutableList<SourceUI>,
    private val newList: List<SourceUI>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldList[oldItemPosition] == newList[newItemPosition]

    }
}