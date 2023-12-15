package com.example.appnews.presentation.headlines

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appnews.R

class HeadlinesFragment : Fragment() {

    companion object {
        fun newInstance() = HeadlinesFragment()
    }

    private lateinit var viewModel: HeadlinesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_headlines, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HeadlinesViewModel::class.java)

    }

}