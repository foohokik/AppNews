package com.example.appnews.presentation.source.articles

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.databinding.FragmentSourceArticlesListBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.source.search_articles.SearchSourceArticlesFragment
import com.example.appnews.presentation.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class SourceArticlesListFragment : Fragment(R.layout.fragment_source_articles_list), OnBackPressedListener {

    @Inject
    lateinit var viewModelFactory: SourceArticlesListViewModel.Factory

    private val binding by viewBinding(FragmentSourceArticlesListBinding::bind)

    private lateinit var sourceArticlesAdapter: HeadlinesAdapter

    private val sourceId: String by lazy {
        requireArguments().getString(SearchSourceArticlesFragment.SOURCE_ARTICLE_ARG) as String
    }

    private val viewModel: SourceArticlesListViewModel by viewModelFactory {
        viewModelFactory.create(sourceId = sourceId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        backArrow()
        navigateToSearch()
        observe()
    }

    private fun networkState(networkStatus: NetworkStatus) {
        when (networkStatus) {
            NetworkStatus.Connected -> {
                with(binding) {
                    viewModel.getSourceArticles()
                    rvSourcesArticles.visibility = View.VISIBLE
                    viewErrorArticlesSources.visibility = View.INVISIBLE
                }
            }

            NetworkStatus.Disconnected -> {
                with(binding) {
                    rvSourcesArticles.visibility = View.GONE
                    viewErrorArticlesSources.visibility = View.VISIBLE
                    viewErrorArticlesSources.setText(resources.getString(
                        R.string.no_connection))
                }
            }

            NetworkStatus.Unknown -> {}
        }
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.newsFlow.collect {
                        sourceArticlesAdapter.setItems(it.articles)
                    }
                }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
                launch { viewModel.networkStatus.collect { networkState(it) } }
            }
        }
    }
    private fun initViews() = with(binding.rvSourcesArticles) {
        val manager = LinearLayoutManager(requireContext())
        sourceArticlesAdapter = HeadlinesAdapter(viewModel, changeBackgroundColor = false)
        layoutManager = manager
        adapter = sourceArticlesAdapter
        itemAnimator = null
    }
    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.ErrorEffect -> {}
            else -> {}
        }
    }

    private fun backArrow() {
        binding.imageButtonBackSourceList.setOnClickListener {
            viewModel.navigateToBack()
        }
    }

    private fun navigateToSearch() {
        binding.imageButtonSearchSource.setOnClickListener {
            viewModel.navigateToSearch()
        }
    }

    override fun onBackPressed() {
        viewModel.navigateToBack()
    }

    companion object {
        const val SOURCE_ARTICLE_ARG = "SOURCE_ARTICLE"
        @JvmStatic
        fun newInstance(source: String) = SourceArticlesListFragment().apply {
            arguments = Bundle().apply {
                putString(SOURCE_ARTICLE_ARG, source)
            }
        }
    }

}