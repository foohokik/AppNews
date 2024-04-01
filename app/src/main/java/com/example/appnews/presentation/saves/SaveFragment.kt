package com.example.appnews.presentation.saves

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.Screens
import com.example.appnews.databinding.FragmentSaveBinding
import com.example.appnews.presentation.headlines.SearchHeadlinesViewModel
import com.example.appnews.presentation.headlines.tabfragment.SideEffects
import com.example.appnews.presentation.headlines.tabfragment.adapterRV.HeadlinesAdapter
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.viewModelFactory
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SaveFragment : Fragment(), OnBackPressedListener {

    @Inject
    lateinit var router: Router
    @Inject
    internal lateinit var viewModelProvider: Provider<SaveViewModel>

    private var _binding: FragmentSaveBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModelFactory { viewModelProvider.get() }
    private lateinit var adapterSave: HeadlinesAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        binding.materialToolbarSave.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.save_fragment_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.searchViewSaveFragment -> {
                        router.navigateTo(Screens.searchSaveFragment())
                    }

                }
                return true
            }
        }, viewLifecycleOwner)

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

    private fun initViews() = with(binding.rvSave) {
        val manager = LinearLayoutManager(requireContext())
        adapterSave = HeadlinesAdapter(viewModel, changeBackgroundColor = false)
        layoutManager = manager
        adapter = adapterSave
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

    override fun onBackPressed() {
        router.exit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}