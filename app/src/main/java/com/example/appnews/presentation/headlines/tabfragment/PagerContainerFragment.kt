package com.example.appnews.presentation.headlines.tabfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.App
import com.example.appnews.Screens
import com.example.appnews.core.Category
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.core.ShareDataClass
import com.example.appnews.core.viewclasses.SharedDataType

import com.example.appnews.databinding.FragmentPagerContainerBinding
import com.example.appnews.presentation.headlines.tabfragment.PagerContainerViewModel.Companion.CATEGORY
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.HeadlinesAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class PagerContainerFragment : Fragment() {

    private lateinit var headlineAdapter: HeadlinesAdapter

    var totalPages = 0

    var isLastPage = false

    private val viewModel by viewModels<PagerContainerViewModel> { PagerContainerViewModel.Factory }


    private var _binding: FragmentPagerContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPagerContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        initViews()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.headlinesNewsFlow.collect {
                        headlineAdapter.setItems(it.articles)
                        totalPages = (it.totalResults / PAGE_SIZE) + 1
                    }
                }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }

//                launch {
//                    viewModel.sharedClass.reviewSearchSideEffect.collect {
//                        sendCountryCode((it as SharedDataType.Filter).country)
//                    }
//                }

                launch {
                    viewModel.isLastPageFlow.collect {
                        isLastPage = it
                    }

                }
            }
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


    var isScrolling = false


    val scrollListener = object : RecyclerView.OnScrollListener() {


        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = binding.recycleviewHeadlines.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isHasVisibleItems = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE



            if ((lastVisibleItemPosition + 4 >= totalItemCount) && isTotalMoreThanVisible
                && isHasVisibleItems && isScrolling && isNotLoadingAndNotLastPage
            ) {
                viewModel.getHeadlinesNews()
                isScrolling = false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

//    private fun sendCountryCode(country: String) {
//        viewModel.country = country
//        viewModel.getRenewedHeadlinesNews()
//    }



    private fun initViews() = with(binding.recycleviewHeadlines) {
        val manager = LinearLayoutManager(requireContext())
        headlineAdapter = HeadlinesAdapter(viewModel, changeBackgroundColor = false)
        layoutManager = manager
        adapter = headlineAdapter
        itemAnimator = null
        addOnScrollListener(scrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recycleviewHeadlines.removeOnScrollListener(scrollListener)
    }

    companion object {
        fun newInstance(position: Int): PagerContainerFragment {
            return PagerContainerFragment().apply {
                arguments = bundleOf(CATEGORY to Category.values()[position].category)
            }
        }
    }

}