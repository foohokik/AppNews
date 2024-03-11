package com.example.appnews.presentation.source

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnews.App
import com.example.appnews.Screens
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.core.viewclasses.SharedDataType
import com.example.appnews.databinding.FragmentSourceBinding
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.source.AdapterSources.SourceAdapter
import kotlinx.coroutines.launch

class SourceFragment : Fragment(), OnBackPressedListener {

	private var _binding: FragmentSourceBinding? = null
	private val binding get() = _binding!!

	private val viewModel by viewModels<SourceViewModel>{SourceViewModel.Factory}

	private lateinit var adapterSource: SourceAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSourceBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initViews()
		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				launch {
					viewModel.sourceFlow.collect {
						adapterSource.setItems(it.sources)

					}
				}
				launch { viewModel.sideEffects.collect { handleSideEffects(it) } }

			}
		}
	}

	private fun initViews() = with(binding.rvSources) {
		val manager = LinearLayoutManager(requireContext())
		adapterSource = SourceAdapter(viewModel)
		layoutManager = manager
		adapter = adapterSource
		itemAnimator = null

	}

	private fun handleSideEffects(sideEffects: SideEffects) {
		when (sideEffects) {
			is SideEffects.ErrorEffect -> {}
			is SideEffects.ClickSource -> {
				(requireActivity().application as App).router.navigateTo(
					Screens.sourceArticlesListFragment(
						sideEffects.source.id
					)
				)
			}
			else ->{}
		}

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun onBackPressed() {
		(requireActivity().application as App).router.exit()
	}

}