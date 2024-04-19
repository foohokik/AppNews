package com.example.appnews.presentation.source

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.core.networkstatus.NetworkStatus
import com.example.appnews.databinding.FragmentSourceBinding
import com.example.appnews.presentation.SideEffects
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.source.adaptersources.SourceAdapter
import com.example.appnews.presentation.viewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SourceFragment : Fragment(R.layout.fragment_source), OnBackPressedListener {

    @Inject
    internal lateinit var viewModelProvider: Provider<SourceViewModel>

    private val binding by viewBinding(FragmentSourceBinding::bind)

    private lateinit var adapterSource: SourceAdapter
    private val viewModel by viewModelFactory { viewModelProvider.get() }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.sourceFlow.collect {
                        adapterSource.setItems(it.sources)
                    }
                }
                launch { viewModel.sideEffects.collect { handleSideEffects(it) } }
                launch { viewModel.networkStatus.collect { networkState(it) } }
            }
        }
    }

    private fun networkState(networkStatus: NetworkStatus) {
        when (networkStatus) {
            NetworkStatus.Connected -> {
                with(binding) {
                    viewModel.getSources()
                    rvSources.visibility = View.VISIBLE
                    viewErrorSources.visibility = View.INVISIBLE
                }
            }

            NetworkStatus.Disconnected -> {
                with(binding) {
                    rvSources.visibility = View.GONE
                    viewErrorSources.visibility = View.VISIBLE
                    viewErrorSources.setText(resources.getString(
                        R.string.no_connection))
                }
            }

            NetworkStatus.Unknown -> {}
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
            else -> {}
        }
    }

    override fun onBackPressed() {
        viewModel.navigateToBack()
    }

}