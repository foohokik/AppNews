package com.example.appnews.presentation.headlines.tabfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnews.core.ARG_OBJECT

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = PagerContainerFragment()
//        fragment.arguments = Bundle().apply {
//
//            putInt(ARG_OBJECT, position+1)
//        }
        return fragment
    }



}