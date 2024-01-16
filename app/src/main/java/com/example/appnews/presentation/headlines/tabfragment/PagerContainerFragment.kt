package com.example.appnews.presentation.headlines.tabfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.Screens
import com.example.appnews.core.ARG_OBJECT
import com.example.appnews.data.dataclasses.Article
import com.example.appnews.databinding.FragmentHeadlinesBinding
import com.example.appnews.databinding.FragmentPagerContainerBinding
import com.example.appnews.presentation.headlines.HeadlinesFragment
import com.example.appnews.presentation.headlines.HeadlinesViewModel
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.HeadlinesAdapter
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.launch


class PagerContainerFragment : Fragment() {


	private lateinit var adapter: HeadlinesAdapter

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

//		arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
			initViews()
//			        arguments?.getInt(ARG_OBJECT)
					viewLifecycleOwner.lifecycleScope.launch {
						viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
							launch { viewModel.headlinesNewsFlow.collect { adapter.setItems(it.articles) } }
							launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
						}
					}


		//}
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

	private fun initViews() {
		val manager = LinearLayoutManager(requireContext())
		adapter = HeadlinesAdapter(viewModel)
		binding.recycleviewHeadlines.layoutManager = manager
		binding.recycleviewHeadlines.adapter = adapter
	}

}