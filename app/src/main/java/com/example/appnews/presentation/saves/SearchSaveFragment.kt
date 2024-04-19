package com.example.appnews.presentation.saves

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.databinding.FragmentSearchSaveBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.hideKeyboard
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.showKeyBoard
import com.example.appnews.presentation.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SearchSaveFragment : Fragment(R.layout.fragment_search_save), OnBackPressedListener {

    @Inject
    internal lateinit var viewModelProvider: Provider<SearchSaveViewModel>

    private val binding by viewBinding(FragmentSearchSaveBinding::bind)

    private val viewModel by viewModelFactory { viewModelProvider.get() }
    private lateinit var saveAdapter: HeadlinesAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backArrow()
        closeEnteringSearch()
        editTextChange()
        initViews()
        observe()
    }
    override fun onBackPressed() {
      viewModel.navigateToBack()
    }
    private fun observe() {
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
    private fun editTextChange() {
        binding.editTextSearchSave.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (text.toString().isNotEmpty()) {
                    viewModel.getSearchSavedArticles(searchQuery = text.toString())
                }
            }
        }
    }

    private fun initViews() = with(binding.rvSaveSearch) {
        val manager = LinearLayoutManager(requireContext())
        saveAdapter = HeadlinesAdapter(viewModel, changeBackgroundColor = true)
        layoutManager = manager
        adapter = saveAdapter
        itemAnimator = null
    }
   private fun backArrow() {
        binding.imageSaveButtonBackSearch.setOnClickListener {
            it.context.hideKeyboard(it)
            viewModel.navigateToBack()
        }
    }

   private fun closeEnteringSearch() {
        binding.imageSaveButtonClose.setOnClickListener {
            with(binding.editTextSearchSave) {
                text.clear()
                clearFocus()
                isCursorVisible = false
            }
            activity?.hideKeyboard()
            viewModel.clearFlowAndOnChangeKeyBoardFlag()
            saveAdapter.setItems(emptyList())
        }
    }

  private fun renderQuery(text: String) {
        if (text != binding.editTextSearchSave.text.toString()) {
            binding.editTextSearchSave.setText(text)
        }
    }

    private fun renderKeyboard(isShow: Boolean) {
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
            else -> {}
        }
    }

}