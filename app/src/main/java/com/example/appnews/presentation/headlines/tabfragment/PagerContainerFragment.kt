package com.example.appnews.presentation.headlines.tabfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.App
import com.example.appnews.Screens
import com.example.appnews.core.Category
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.databinding.FragmentPagerContainerBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.viewModelFactory
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import javax.inject.Inject

class PagerContainerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: PagerContainerViewModel.Factory

    private lateinit var headlineAdapter: HeadlinesAdapter
    private var totalPages = 0
    var isLastPage = false
    private val category: String by lazy { requireArguments().getString(CATEGORY) as String }

    private var _binding: FragmentPagerContainerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PagerContainerViewModel by viewModelFactory {
        viewModelFactory.create(category = category)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        observe()
    }
    private fun observe(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.headlinesNewsFlow.collect {
                        headlineAdapter.setItems(it.articles)
                        totalPages = (it.totalResults / PAGE_SIZE) + 1
                    }
                }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
                launch { viewModel.networkStatus.collect { networkState(it) } }
                launch { viewModel.isLastPageFlow.collect { isLastPage = it } }
            }
        }
    }

    private var itWasDisconnected = false
    private fun networkState(networkStatus: NetworkStatus) {
        when (networkStatus) {
            NetworkStatus.Connected -> {
                with(binding) {
                    if (itWasDisconnected) {
                        viewModel.getHeadlinesNews()
                    }
                    recycleviewHeadlines.visibility = View.VISIBLE
                    viewError.visibility = View.INVISIBLE
                }
            }

            NetworkStatus.Disconnected -> {
                itWasDisconnected = true
                with(binding) {
                    recycleviewHeadlines.visibility = GONE
                    viewError.visibility = View.VISIBLE
                    viewError.setText("No internet connection")
                }
            }

            NetworkStatus.Unknown -> {}
        }
    }

    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.ErrorEffect -> {
                Toast.makeText(requireContext(), "Ошибка: " + sideEffects.err, Toast.LENGTH_LONG)
                    .show()
            }
            is SideEffects.ExceptionEffect -> {
                Toast.makeText(
                    requireContext(),
                    "Ошибка: " + sideEffects.throwable.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = binding.recycleviewHeadlines.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLastPage
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
        const val CATEGORY = "category"
        fun newInstance(position: Int): PagerContainerFragment {
            return PagerContainerFragment().apply {
                arguments = bundleOf(CATEGORY to Category.values()[position].category)
            }
        }
    }

}