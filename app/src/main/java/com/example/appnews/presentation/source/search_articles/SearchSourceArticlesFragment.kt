package com.example.appnews.presentation.source.search_articles

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.databinding.FragmentSearchSourceArticlesBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.hideKeyboard
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.showKeyBoard
import com.example.appnews.presentation.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchSourceArticlesFragment : Fragment(R.layout.fragment_search_source_articles), OnBackPressedListener {

    @Inject
    lateinit var viewModelFactory: SearchSourceArticlesViewModel.Factory

    private val binding by viewBinding(FragmentSearchSourceArticlesBinding::bind)

    private lateinit var sourceSearchAdapter: HeadlinesAdapter

    private val sourceId: String by lazy {
        requireArguments().getString(SOURCE_ARTICLE_ARG) as String
    }

    private val viewModel: SearchSourceArticlesViewModel by viewModelFactory {
        viewModelFactory.create(sourceId = sourceId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backArrow()
        closeEnteringSearch()
        editTextChange()
        initViews()
        observe()
    }

    override fun onBackPressed() {
        viewModel.navigateToBack()
    }
    private fun networkState (networkStatus: NetworkStatus) {
        when(networkStatus) {
            NetworkStatus.Connected -> {
                with(binding) {
                    rvSourceSearch.visibility = View.VISIBLE
                    viewErrorSearchSources.visibility = View.INVISIBLE
                }
            }
            NetworkStatus.Disconnected -> {
                with(binding) {
                    rvSourceSearch.visibility = View.GONE
                    viewErrorSearchSources.visibility = View.VISIBLE
                    viewErrorSearchSources.setText(resources.getString(
                        R.string.no_connection))
                }
            }
            NetworkStatus.Unknown -> {}
        }
    }

    private fun editTextChange(){
        binding.editTextSearchSource.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (text.toString().isNotEmpty()) {
                    viewModel.getSearchNews(searchQuery = text.toString())
                }
            }
        }
    }
    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.searchSourceArticles.collect {
                        sourceSearchAdapter.setItems(it.articles)
                    }
                }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
                launch { viewModel.showKeyboard.collect(::renderKeyboard) }
                launch {viewModel.queryFlow.collect { renderQuery(it) } }
                launch { viewModel.networkStatus.collect(::networkState) }
            }
        }
    }
    private fun initViews() = with(binding.rvSourceSearch) {
        val manager = LinearLayoutManager(requireContext())
        sourceSearchAdapter = HeadlinesAdapter(viewModel, changeBackgroundColor = true)
        layoutManager = manager
        adapter = sourceSearchAdapter
        itemAnimator = null
    }

   private fun backArrow() {
        binding.imageSourceButtonBackSearch.setOnClickListener {
            it.context.hideKeyboard(it)
            viewModel.navigateToBack()
        }
    }
   private fun closeEnteringSearch() {
        binding.imageSourceButtonClose.setOnClickListener {
            with(binding.editTextSearchSource) {
                text.clear()
                clearFocus()
                isCursorVisible = false
            }
            activity?.hideKeyboard()
            viewModel.clearFlowAndOnChangeKeyBoardFlag()
            sourceSearchAdapter.setItems(emptyList())
        }
    }

    private fun renderQuery(text: String) {
        if (text != binding.editTextSearchSource.text.toString()) {
            binding.editTextSearchSource.setText(text)
        }
    }
    private fun renderKeyboard(isShow: Boolean) {
        if (isShow) {
            with(binding.editTextSearchSource) {
                requestFocus()
                context.showKeyBoard(this)
            }
        } else {
            activity?.hideKeyboard()
        }
    }

    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.ErrorEffect -> {}
            else -> {}
        }
    }

    companion object {
        const val SOURCE_ARTICLE_ARG = "SOURCE_ARTICLE"
        @JvmStatic
        fun newInstance(source: String) = SearchSourceArticlesFragment().apply {
            arguments = Bundle().apply {
                putString(SOURCE_ARTICLE_ARG, source)
            }
        }
    }

}