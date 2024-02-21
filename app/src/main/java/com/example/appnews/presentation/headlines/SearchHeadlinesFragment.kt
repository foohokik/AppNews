package com.example.appnews.presentation.headlines

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.core.Category
import com.example.appnews.core.PAGE_SIZE
import com.example.appnews.databinding.FragmentHeadlinesBinding
import com.example.appnews.databinding.FragmentSearchHeadlinesBinding
import com.example.appnews.presentation.headlines.tabfragment.PagerContainerFragment
import com.example.appnews.presentation.headlines.tabfragment.PagerContainerViewModel
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.navigation.OnBackPressedListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchHeadlinesFragment : Fragment(), OnBackPressedListener {

    private lateinit var headlineAdapter: HeadlinesAdapter

    private var _binding: FragmentSearchHeadlinesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SearchHeadlinesViewModel> { SearchHeadlinesViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchHeadlinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        backArrow()
        closeEnteringSearch()

        binding.editTextSearchHeadlines.doOnTextChanged { text, start, before, count ->

                text?.let {
                    if (text.toString().isNotEmpty()) {
                        viewModel.getSearchNews(searchQuery =  text.toString())

                    }
                }
            }



        initViews()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.searchHeadlinesViewModel.collect { headlineAdapter.setItems(it.articles)
                    }
                }
               // launch { viewModel.sideEffects.collect { handleSideEffects(it) } }

                launch { viewModel.showKeyboard.collect(::renderKeyboard) }


                launch {
                    viewModel.queryFlow.collect { renderQuery(it) }
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed() {
        (requireActivity().application as App).router.exit()
    }

    fun renderQuery (text:String) {
        if (text == binding.editTextSearchHeadlines.text.toString()){

        } else { binding.editTextSearchHeadlines.setText(text) }
    }


    fun backArrow() {
        binding.imageButtonBackSearch.setOnClickListener {
            (requireActivity().application as App).router.exit()
        }
    }

    fun closeEnteringSearch() {

        binding.imageButtonClose.setOnClickListener {
            with(binding.editTextSearchHeadlines) {
                text.clear()
                clearFocus()
                isCursorVisible = false

            }

            viewModel.changeFlagonChangeKeyBoardFlag(isShow = false)

        }

    }

    fun renderKeyboard(isShow: Boolean) {
        if (isShow) {
           binding.editTextSearchHeadlines.context.showKeyBoard( binding.editTextSearchHeadlines)
        } else {
            binding.editTextSearchHeadlines.context.hideKeyboard( binding.editTextSearchHeadlines)
        }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Context.showKeyBoard(view: View?) {
        view?.let  {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }


    private fun initViews() = with(binding.recycleviewHeadlinesSearch) {
        val manager = LinearLayoutManager(requireContext())
        headlineAdapter = HeadlinesAdapter(viewModel, changeBackgroundColor = true)
        layoutManager = manager
        adapter = headlineAdapter
        itemAnimator = null
        //addOnScrollListener(scrollListener)
    }


}