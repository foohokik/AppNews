package com.example.appnews.presentation.headlines

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnews.App
import com.example.appnews.Screens
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.databinding.FragmentSearchHeadlinesBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.hideKeyboard
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.showKeyBoard
import com.example.appnews.presentation.viewModelFactory
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


class SearchHeadlinesFragment : Fragment(), OnBackPressedListener {

    @Inject
    lateinit var router: Router
    @Inject
    internal lateinit var viewModelProvider: Provider<SearchHeadlinesViewModel>

    private lateinit var headlineAdapter: HeadlinesAdapter
    private var _binding: FragmentSearchHeadlinesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by  viewModelFactory { viewModelProvider.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchHeadlinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backArrow()
        closeEnteringSearch()
        binding.editTextSearchHeadlines.doOnTextChanged { text, _, _, _ ->
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed() {
        router.exit()
    }

    private fun networkState (networkStatus: NetworkStatus) {
        when(networkStatus) {
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
                    viewErrorSearchHeadlines.setText("No internet connection")
                }
            }
            NetworkStatus.Unknown -> {}
        }
    }

  private fun renderQuery(text: String) {
        if (text == binding.editTextSearchHeadlines.text.toString()) {

        } else {
            binding.editTextSearchHeadlines.setText(text)
        }
    }

   private fun backArrow() {
        binding.imageButtonBackSearch.setOnClickListener {
            router.exit()
        }
    }

   private fun closeEnteringSearch() {
        binding.imageButtonClose.setOnClickListener {
            with(binding.editTextSearchHeadlines) {
                text.clear()
                clearFocus()
                isCursorVisible = false

            }
            viewModel.changeFlagonChangeKeyBoardFlag(isShow = false)
            activity?.hideKeyboard()
            viewModel.clearFlow()
            headlineAdapter.setItems(emptyList())
        }
    }

    fun renderKeyboard(isShow: Boolean) {
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

    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.ErrorEffect -> {}
            is SideEffects.ClickEffectArticle -> {
                router.navigateTo(
                    Screens.fullArticleHeadlinesFragment(
                        sideEffects.article
                    )
                )
            }
            else -> {}
        }
    }
}