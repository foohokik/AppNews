package com.example.appnews.core.viewclasses

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import com.example.appnews.R
import com.example.appnews.core.FilterTypes
import com.example.appnews.core.ModelFilterButtons
import com.example.appnews.databinding.PopularLayoutBottomBinding

class FilterLayoutButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = PopularLayoutBottomBinding
                         .inflate(LayoutInflater.from(context), this)

    fun setData(item:ModelFilterButtons) {
        when (item.type) {
            FilterTypes.POPULAR -> {
                commonSetData(item, R.drawable.shape_button_popular_pressed, R.drawable.shape_button_popular_not_pressed)
            }

            FilterTypes.NEW -> {
                commonSetData(item, R.drawable.shape_button_new_pressed, R.drawable.shape_button_new_not_pressed)
            }

            FilterTypes.RELEVANT -> {
                commonSetData(item, R.drawable.shape_button_relevant_pressed, R.drawable.shape_button_relevant_not_pressed)
            }

            FilterTypes.RUSSIAN -> {
                setLanguageBtnData(item, R.drawable.shape_btn_language_pressed, R.drawable.shape_btn_language_not_pressed)
            }

            FilterTypes.DEUTSCH -> {
                setLanguageBtnData(item, R.drawable.shape_btn_language_pressed, R.drawable.shape_btn_language_not_pressed)
            }

            FilterTypes.ENGLISH -> {
                setLanguageBtnData(item, R.drawable.shape_btn_language_pressed, R.drawable.shape_btn_language_not_pressed)
            }
        }
    }

    private fun commonSetData(item: ModelFilterButtons, pressedShape: Int, notPressed: Int) = with(binding) {
        tvButtonsFilter.text = context.getString(item.type.title)
        val drawable = if (item.isPressed) {
            pressedShape
        } else {
            notPressed
        }
        this@FilterLayoutButton.background = AppCompatResources.getDrawable(context, drawable)

        if (item.isPressed) {
            tvButtonsFilter.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_check, 0,0,0)
        } else {
            tvButtonsFilter.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,0,0)
        }
    }

    private fun setLanguageBtnData (item: ModelFilterButtons, pressedShape: Int, notPressed: Int) = with(binding){
        tvButtonsFilter.text = context.getString(item.type.title)
        val drawable = if (item.isPressed) {
            pressedShape
        } else {
            notPressed
        }
        this@FilterLayoutButton.background = AppCompatResources.getDrawable(context, drawable)
    }

}