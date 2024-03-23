package com.example.appnews.presentation.source

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.Screens
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.core.viewclasses.SharedDataType
import com.example.appnews.data.dataclassesresponse.ArticlesUI
import com.example.appnews.databinding.FragmentFullArticleHeadlinesBinding
import com.example.appnews.databinding.FragmentSourceArticlesListBinding
import com.example.appnews.presentation.headlines.FullArticleHeadlinesFragment
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.hideKeyboard
import com.example.appnews.presentation.navigation.OnBackPressedListener
import kotlinx.coroutines.launch

class SourceArticlesListFragment : Fragment(), OnBackPressedListener {

    private var _binding: FragmentSourceArticlesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var sourceArticlesAdapter: HeadlinesAdapter


    private val viewModel by viewModels<SourceArticlesListViewModel> { SourceArticlesListViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSourceArticlesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews()
        backArrow()
        navigateToSearch()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.newsFlow.collect{
                        sourceArticlesAdapter.setItems(it.articles)
                    }
                }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
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

    fun backArrow() {
        binding.imageButtonBackSourceList.setOnClickListener {
            (requireActivity().application as App).router.exit()
        }
    }

    fun navigateToSearch () {
        binding.imageButtonSearchSource.setOnClickListener {
            viewModel.navigateToSearch()
        }
    }

    companion object {

        const val ARG = "ARG"

        @JvmStatic
        fun newInstance(source: String) = SourceArticlesListFragment().apply {
            arguments = Bundle().apply {

                putString(ARG, source)

            }
        }

}

    override fun onBackPressed() {
        (requireActivity().application as App).router.exit()
    }

}