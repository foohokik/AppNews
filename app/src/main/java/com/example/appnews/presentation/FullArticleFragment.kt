package com.example.appnews.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.appnews.App

import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.databinding.FragmentFullArticleBinding
import com.example.appnews.presentation.navigation.OnBackPressedListener

class FullArticleFragment : Fragment(), OnBackPressedListener {

    private var _binding: FragmentFullArticleBinding? = null
    private val binding get() = _binding!!

    private val article: ArticlesUI.Article? by lazy { requireArguments().get(ARG) as? ArticlesUI.Article }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFullArticleBinding.inflate(inflater, container, false )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.materialToolbar.title = article?.title
        binding.materialToolbar.setNavigationOnClickListener {
            (requireActivity().application as App).router.exit()
        }



        binding.webView.apply {
            webViewClient = WebViewClient()
            article?.let { loadUrl(it.url) }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val ARG = "ARG"
        @JvmStatic
        fun newInstance(article: ArticlesUI.Article) =
            FullArticleFragment().apply {
                arguments = Bundle().apply {

                    putSerializable(ARG, article)

                }
            }
    }

    override fun onBackPressed() {
        (requireActivity().application as App).router.exit()
    }
}