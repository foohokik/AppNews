package com.example.appnews.presentation.source

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnews.App
import com.example.appnews.Screens
import com.example.appnews.databinding.FragmentSearchSourceArticlesBinding
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.hideKeyboard
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.showKeyBoard
import kotlinx.coroutines.launch

class SearchSourceArticlesFragment : Fragment(), OnBackPressedListener {


    private var _binding: FragmentSearchSourceArticlesBinding? = null
    private val binding get() = _binding!!

    private lateinit var sourceSearchAdapter: HeadlinesAdapter

    private val viewModel by viewModels<SearchSourceArticlesViewModel> { SearchSourceArticlesViewModel.Factory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchSourceArticlesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backArrow()

        closeEnteringSearch()

        binding.editTextSearchSource.doOnTextChanged { text, start, before, count ->

            text?.let {
                if (text.toString().isNotEmpty()) {
                    viewModel.getSearchNews(searchQuery = text.toString())

                }
            }
        }



        initViews()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.searchSourceArticles.collect {
                        sourceSearchAdapter.setItems(it.articles)
                    }
                }

                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }

                launch { viewModel.showKeyboard.collect(::renderKeyboard) }


                launch {
                    viewModel.queryFlow.collect { renderQuery(it) }
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed() {
        (requireActivity().application as App).router.exit()
    }

    private fun initViews() = with(binding.rvSourceSearch) {
        val manager = LinearLayoutManager(requireContext())
        sourceSearchAdapter = HeadlinesAdapter(viewModel, changeBackgroundColor = true)
        layoutManager = manager
        adapter = sourceSearchAdapter
        itemAnimator = null
    }

    fun backArrow() {
        binding.imageSourceButtonBackSearch.setOnClickListener {
            it.context.hideKeyboard(it)
            (requireActivity().application as App).router.exit()

        }
    }

    fun closeEnteringSearch() {

        binding.imageSourceButtonClose.setOnClickListener {
            with(binding.editTextSearchSource) {
                text.clear()
                clearFocus()
                isCursorVisible = false

            }

            viewModel.changeFlagOnChangeKeyBoardFlag(isShow = false)
            activity?.hideKeyboard()
            viewModel.clearFlow()
            sourceSearchAdapter.setItems(emptyList())


        }

    }

    fun renderQuery(text: String) {
        if (text == binding.editTextSearchSource.text.toString()) {

        } else {
            binding.editTextSearchSource.setText(text)
        }
    }

    fun renderKeyboard(isShow: Boolean) {
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
            is SideEffects.ClickEffectArticle -> {
                (requireActivity().application as App).router.navigateTo(
                    Screens.fullArticleHeadlinesFragment(
                        sideEffects.article
                    )
                )
            }

            else -> {}
        }

    }


    companion object {

        const val ARG = "ARG"

        @JvmStatic
        fun newInstance(source: String) = SearchSourceArticlesFragment().apply {
            arguments = Bundle().apply {

                putString(ARG, source)

            }
        }


    }

}