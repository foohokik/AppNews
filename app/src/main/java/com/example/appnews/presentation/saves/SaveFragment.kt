package com.example.appnews.presentation.saves

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.databinding.FragmentSaveBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.headlines.headlines_adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SaveFragment : Fragment(R.layout.fragment_save), OnBackPressedListener {

    @Inject
    internal lateinit var viewModelProvider: Provider<SaveViewModel>

    private val binding by viewBinding(FragmentSaveBinding::bind)

    private val viewModel by viewModelFactory { viewModelProvider.get() }
    private lateinit var adapterSave: HeadlinesAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initToolBar()
        observe()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getArticles.collect {
                        adapterSave.setItems(it)
                    }
                }
                launch {
                    viewModel.sideEffects.collect { handleSideEffects(it) }
                }
            }
        }
    }

    private fun initToolBar() {
        binding.materialToolbarSave.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.save_fragment_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.searchViewSaveFragment -> {
                        viewModel.navigateToSearch()
                    }
                }
                return true
            }
        }, viewLifecycleOwner)
    }

    private fun initViews() = with(binding.rvSave) {
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout = true
        manager.stackFromEnd = true
        adapterSave = HeadlinesAdapter(viewModel, changeBackgroundColor = false)
        layoutManager = manager
        adapter = adapterSave
        itemAnimator = null
    }

    private fun handleSideEffects(sideEffects: SideEffects) {
        when (sideEffects) {
            is SideEffects.ErrorEffect -> {}
            else -> {}
        }
    }

    override fun onBackPressed() {
        viewModel.navigateToBack()
    }

}