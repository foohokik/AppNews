package com.example.appnews.presentation.headlines.tabfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.Screens
import com.example.appnews.core.ARG_OBJECT
import com.example.appnews.core.Category
import com.example.appnews.core.PAGE_SIZE

import com.example.appnews.databinding.FragmentHeadlinesBinding
import com.example.appnews.databinding.FragmentPagerContainerBinding
import com.example.appnews.presentation.headlines.HeadlinesFragment
import com.example.appnews.presentation.headlines.HeadlinesViewModel
import com.example.appnews.presentation.headlines.tabfragment.PagerContainerViewModel.Companion.CATEGORY
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.HeadlinesAdapter
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.launch


class PagerContainerFragment : Fragment() {


	private lateinit var adapter: HeadlinesAdapter



	//private val category: String by lazy { requireArguments().getString(CATEGORY).orEmpty() }

	private val viewModel by viewModels<PagerContainerViewModel> { PagerContainerViewModel.Factory }


	private var _binding: FragmentPagerContainerBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
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
					viewModel.headlinesNewsFlow.collect { adapter.setItems(it.articles) }
					}
				launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
			}
		}

	}

	private fun handleSideEffects(sideEffects: SideEffects) {
		when (sideEffects) {
			is SideEffects.ErrorEffect -> {}
			is SideEffects.ClickEffect -> {
				(requireActivity().application as App).router.navigateTo(
					Screens.fullArticleFragment(
						sideEffects.article
					)
				)
			}
		}

	}



	var isLoading = false
	var isLastPage = false
	var isScrolling = false



	val scrollListener = object : RecyclerView.OnScrollListener() {


		override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
			super.onScrolled(recyclerView, dx, dy)

			val layoutManager = binding.recycleviewHeadlines.layoutManager as LinearLayoutManager
			val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
			val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
			val visibleItemCount = layoutManager.childCount
			val totalItemCount = layoutManager.itemCount

			val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
			val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
			val isHasVisibleItems = firstVisibleItemPosition >= 0
			val isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE

			if ((lastVisibleItemPosition + 4 >=totalItemCount) && isTotalMoreThanVisible
				&& isHasVisibleItems && isScrolling) {
				viewModel.getHeadlinesNews()
				isScrolling = false
			}

		}

		override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
			super.onScrollStateChanged(recyclerView, newState)
			if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
				isScrolling = true
			}
		}
	}


	private fun initViews() {
		val manager = LinearLayoutManager(requireContext())
		adapter = HeadlinesAdapter(viewModel)
		binding.recycleviewHeadlines.layoutManager = manager
		binding.recycleviewHeadlines.adapter = adapter
		binding.recycleviewHeadlines.addOnScrollListener(scrollListener)
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