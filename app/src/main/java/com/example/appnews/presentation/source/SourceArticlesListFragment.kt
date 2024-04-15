package com.example.appnews.presentation.source

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnews.App
import com.example.appnews.Screens
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.databinding.FragmentSourceArticlesListBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class SourceArticlesListFragment : Fragment(), OnBackPressedListener {

    @Inject
    lateinit var viewModelFactory: SourceArticlesListViewModel.Factory

    private var _binding: FragmentSourceArticlesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var sourceArticlesAdapter: HeadlinesAdapter

    private val sourceId: String by lazy {
        requireArguments().getString(SearchSourceArticlesFragment.ARG) as String
    }

    private val viewModel: SourceArticlesListViewModel by viewModelFactory {
        viewModelFactory.create(sourceId = sourceId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSourceArticlesListBinding.inflate(inflater, container, false)
        return binding.root
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
                launch {viewModel.networkStatus.collect{ networkState(it) } }
            }
        }
    }

    private fun networkState (networkStatus: NetworkStatus) {
        when(networkStatus) {
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
                    viewErrorArticlesSources.setText("No internet connection")
                }
            }
            NetworkStatus.Unknown -> {}
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
            is SideEffects.ClickEffectArticle -> {}
            else -> {}
        }
    }

   private fun backArrow() {
        binding.imageButtonBackSourceList.setOnClickListener {
            viewModel.navigateToBack()
        }
    }
    private fun navigateToSearch () {
        binding.imageButtonSearchSource.setOnClickListener {
            viewModel.navigateToSearch()
        }
    }

    override fun onBackPressed() {
        viewModel.navigateToBack()
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

}