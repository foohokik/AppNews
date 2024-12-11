package com.example.appnews.presentation.headlines.search

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
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
import com.example.appnews.databinding.FragmentSearchHeadlinesBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.hideKeyboard
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.showKeyBoard
import com.example.appnews.presentation.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


class SearchHeadlinesFragment : Fragment(R.layout.fragment_search_headlines), OnBackPressedListener {

    @Inject
    internal lateinit var viewModelProvider: Provider<SearchHeadlinesViewModel>

    private lateinit var headlineAdapter: HeadlinesAdapter

    private val binding by viewBinding(FragmentSearchHeadlinesBinding::bind)

    private val viewModel by viewModelFactory { viewModelProvider.get() }

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
        activity?.hideKeyboard()
    }

    private fun editTextChange() {
        binding.editTextSearchHeadlines.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (text.toString().isNotEmpty()) {
                    viewModel.getSearchNews(searchQuery = text.toString())
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.searchHeadlinesViewModel.collect {
                        headlineAdapter.setItems(it.articles)
                    }
                }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
                launch { viewModel.showKeyboard.collect(::renderKeyboard) }
                launch {
                    viewModel.queryFlow.collect { renderQuery(it) }
                }
                launch { viewModel.networkStatus.collect(::networkState) }
            }
        }
    }
    private fun networkState(networkStatus: NetworkStatus) {
        when (networkStatus) {
            NetworkStatus.Connected -> {
                with(binding) {
                    recycleviewHeadlinesSearch.visibility = View.VISIBLE
                    viewErrorSearchHeadlines.visibility = View.INVISIBLE
                }
            }

            NetworkStatus.Disconnected -> {
                with(binding) {
                    recycleviewHeadlinesSearch.visibility = View.GONE
                    viewErrorSearchHeadlines.visibility = View.VISIBLE
                    viewErrorSearchHeadlines.setText(resources.getString(R.string.no_connection))
                }
            }

            NetworkStatus.Unknown -> {}
        }
    }

    private fun renderQuery(text: String) {
        if (text != binding.editTextSearchHeadlines.text.toString()) {
            binding.editTextSearchHeadlines.setText(text)
        }
    }

    private fun backArrow() {
        binding.imageButtonBackSearch.setOnClickListener {
            viewModel.navigateToBack()
            activity?.hideKeyboard()
        }
    }

    private fun closeEnteringSearch() {
        binding.imageButtonClose.setOnClickListener {
            with(binding.editTextSearchHeadlines) {
                text.clear()
                clearFocus()
                isCursorVisible = false

            }
            activity?.hideKeyboard()
            viewModel.clearFlowAndOnChangeKeyBoardFlag()
            headlineAdapter.setItems(emptyList())
        }
    }

   private fun renderKeyboard(isShow: Boolean) {
        if (isShow) {
            binding.editTextSearchHeadlines.context.showKeyBoard(binding.editTextSearchHeadlines)
        } else {
            binding.editTextSearchHeadlines.context.hideKeyboard(binding.editTextSearchHeadlines)
        }
    }

    private fun initViews() = with(binding.recycleviewHeadlinesSearch) {
        val manager = LinearLayoutManager(requireContext())
        headlineAdapter = HeadlinesAdapter(viewModel, changeBackgroundColor = true)
        layoutManager = manager
        adapter = headlineAdapter
        itemAnimator = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.ErrorEffect -> {}
            else -> {}
        }
    }
}