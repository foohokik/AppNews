package com.example.appnews.presentation.headlines

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.databinding.FragmentFullArticleHeadlinesBinding
import com.example.appnews.presentation.FullArticleFragmentWeb
import com.example.appnews.presentation.navigation.OnBackPressedListener
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FullArticleHeadlinesFragment : Fragment(), OnBackPressedListener {

    private var isSelected = false

    private var _binding: FragmentFullArticleHeadlinesBinding? = null
    private val binding get() = _binding!!

    private val article: ArticlesUI.Article? by lazy { requireArguments().get(FullArticleFragmentWeb.ARG) as? ArticlesUI.Article }
    private val viewModel by activityViewModels<FullArticleViewModel> { FullArticleViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFullArticleHeadlinesBinding.inflate(inflater, container, false)
        return binding.root

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var parsedDate = LocalDateTime.parse(
            article?.publishedAt,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
        )
        var formatter = DateTimeFormatter.ofPattern("d MMM uuuu | hh-mm a")
        var convertDate = parsedDate.format(formatter)


        with(binding) {
            Glide.with(collapsingIv.context).load(article?.urlToImage).into(collapsingIv)
            tvSourceFullArticle.text = article?.source?.name
            tvDateFullArticle.text = convertDate
            tvTitleFullArticle.text = article?.title
            tvFullArticle.text = article?.description

        }

        backArrowToolBar()


        viewModel.getArticle(article?.title ?: "title")


        binding.ivSaveSign.setOnClickListener {

            viewModel.getArticle(article?.title ?: "title")


            if (!viewModel.stateIconSaved.value) {
                binding.ivSaveSign.setImageResource(R.drawable.icon_saved_filled)
                article?.let { viewModel.saveArticle(it) }

            } else {
                binding.ivSaveSign.setImageResource(R.drawable.icon_saved)
                article?.title?.let { it1 -> viewModel.deleteArticle(it1) }

            }


        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.stateIconSaved.collect(::stateIcon)
                }


            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun stateIcon(state: Boolean) {

        if (state) {
            binding.ivSaveSign.setImageResource(R.drawable.icon_saved_filled)

        } else {
            binding.ivSaveSign.setImageResource(R.drawable.icon_saved)

        }

    }

    override fun onBackPressed() {
        (requireActivity().application as App).router.exit()
    }

    fun backArrowToolBar() {
        binding.imageButtonBackFullArticle.setOnClickListener {
            (requireActivity().application as App).router.exit()
        }
    }


    companion object {

        const val ARG = "ARG"

        @JvmStatic
        fun newInstance(article: ArticlesUI.Article) = FullArticleHeadlinesFragment().apply {
            arguments = Bundle().apply {

                putSerializable(ARG, article)

            }
        }
    }

}

