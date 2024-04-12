package com.example.appnews.presentation.saves

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
import com.example.appnews.databinding.FragmentSearchSaveBinding
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

class SearchSaveFragment : Fragment(), OnBackPressedListener {

    @Inject
    lateinit var router: Router
    @Inject
    internal lateinit var viewModelProvider: Provider<SearchSaveViewModel>

    private var _binding: FragmentSearchSaveBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModelFactory { viewModelProvider.get() }
    private lateinit var saveAdapter: HeadlinesAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchSaveBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backArrow()
        closeEnteringSearch()
        binding.editTextSearchSave.doOnTextChanged { text, start, before, count ->
            text?.let {
                if (text.toString().isNotEmpty()) {
                    viewModel.getSearchSavedArticles(searchQuery = text.toString())
                }
            }
        }

        initViews()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.searchSaveViewModel.collect {
                        saveAdapter.setItems(it)
                    }
                }

                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
                launch { viewModel.showKeyboard.collect(::renderKeyboard) }
                launch {viewModel.queryFlow.collect { renderQuery(it) } }
            }
        }

    }

    override fun onBackPressed() {
      router.exit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding.rvSaveSearch) {
        val manager = LinearLayoutManager(requireContext())
        saveAdapter = HeadlinesAdapter(viewModel, changeBackgroundColor = true)
        layoutManager = manager
        adapter = saveAdapter
        itemAnimator = null
    }
    fun backArrow() {
        binding.imageSaveButtonBackSearch.setOnClickListener {
            it.context.hideKeyboard(it)
            router.exit()

        }
    }

    fun closeEnteringSearch() {

        binding.imageSaveButtonClose.setOnClickListener {
            with(binding.editTextSearchSave) {
                text.clear()
                clearFocus()
                isCursorVisible = false

            }

            viewModel.changeFlagOnChangeKeyBoardFlag(isShow = false)
            activity?.hideKeyboard()
            viewModel.clearFlow()
            saveAdapter.setItems(emptyList())
        }

    }

    fun renderQuery(text: String) {
        if (text == binding.editTextSearchSave.text.toString()) {

        } else {
            binding.editTextSearchSave.setText(text)
        }
    }

    fun renderKeyboard(isShow: Boolean) {
        if (isShow) {
            with(binding.editTextSearchSave) {
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