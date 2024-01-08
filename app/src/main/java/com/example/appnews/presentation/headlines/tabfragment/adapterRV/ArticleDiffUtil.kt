package com.example.appnews.presentation.headlines.tabfragment.adapterRV

import androidx.recyclerview.widget.DiffUtil
import com.example.appnews.data.dataclasses.Article

class ArticleDiffUtil(
    private val oldList: MutableList<Article>,
    private val newList: List<Article>
): DiffUtil.Callback()  {


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