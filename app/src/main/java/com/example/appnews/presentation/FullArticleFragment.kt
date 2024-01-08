package com.example.appnews.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.appnews.data.dataclasses.Article
import com.example.appnews.databinding.FragmentFullArticleBinding

class FullArticleFragment : Fragment() {

    private var _binding: FragmentFullArticleBinding? = null
    private val binding get() = _binding!!

    private val article: Article? by lazy { requireArguments().get(ARG) as? Article }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFullArticleBinding.inflate(inflater, container, false )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  binding.materialToolbar.title
        binding.materialToolbar.setNavigationOnClickListener {
            //navigation back to fragment which webfragment was called
        }

        //val article = arguments?.getSerializable(ARG)

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
        fun newInstance(article: Article) =
            FullArticleFragment().apply {
                arguments = Bundle().apply {

                    putSerializable(ARG, article)

                }
            }
    }
}