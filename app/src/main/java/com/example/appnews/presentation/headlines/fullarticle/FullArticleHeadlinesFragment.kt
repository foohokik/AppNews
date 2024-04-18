package com.example.appnews.presentation.headlines.fullarticle

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.databinding.FragmentFullArticleHeadlinesBinding
import com.example.appnews.domain.dataclasses.ArticlesUI
import com.example.appnews.domain.dataclasses.FullArticleState
import com.example.appnews.presentation.customGetSerializable
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
class FullArticleHeadlinesFragment : Fragment(R.layout.fragment_full_article_headlines), OnBackPressedListener {

    @Inject
    lateinit var viewModelFactory: FullArticleViewModel.Factory

    private val binding by viewBinding(FragmentFullArticleHeadlinesBinding::bind)

    private val article: ArticlesUI.Article? by lazy {
        requireArguments().customGetSerializable(FULL_ARTICLE_ARG) as ArticlesUI.Article?
    }

    private val viewModel: FullArticleViewModel by viewModelFactory {
        viewModelFactory.create(article = article)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backArrowToolBar()
        initView()
        observe()
    }

    private fun initView() {
        binding.ivSaveSign.setOnClickListener {
            viewModel.onSaveOrRemoveArticle()
        }
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateIconSaved.collect(::stateIcon)
                }
                launch {
                    viewModel.contentState.collect(::setContent)
                }
            }
        }
    }
    private fun setContent(state: FullArticleState) = with(binding) {
        Glide.with(collapsingIv.context).load(state.urlImage).into(collapsingIv)
        tvSourceFullArticle.text = state.nameSource
        tvDateFullArticle.text = state.date
        tvTitleFullArticle.text = state.title
        tvFullArticle.text = state.description
    }

    private fun stateIcon(state: Boolean) {
        val drawable = if (state) {
            R.drawable.icon_saved_filled
        } else {
            R.drawable.icon_saved
        }
        binding.ivSaveSign.setImageResource(drawable)
    }

    override fun onBackPressed() {
        viewModel.navigateToBack()
    }
    private fun backArrowToolBar() {
        binding.imageButtonBackFullArticle.setOnClickListener {
            viewModel.navigateToBack()
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.checkArtcicleInDatabase()
    }

    companion object {
       private const val FULL_ARTICLE_ARG = "FULL_ARTICLE"
        @JvmStatic
        fun newInstance(article: ArticlesUI.Article) = FullArticleHeadlinesFragment().apply {
            arguments = Bundle().apply {
                putSerializable(FULL_ARTICLE_ARG, article)
            }
        }
    }

}

