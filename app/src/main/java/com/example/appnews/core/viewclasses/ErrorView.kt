package com.example.appnews.core.viewclasses

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.appnews.R
import com.example.appnews.databinding.ErrorViewBinding

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): LinearLayout(context, attrs) {

    private val binding = ErrorViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL
    }

     fun setText(text:String) {
        binding.textError.text= text.toString()
    }



}