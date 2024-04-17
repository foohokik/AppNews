package com.example.appnews.presentation.headlines

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.Screens
import com.example.appnews.core.shared.SharedDataType
import com.example.appnews.databinding.FragmentHeadlinesBinding
import com.example.appnews.presentation.customGetSerializable
import com.example.appnews.presentation.headlines.tabfragment.ViewPagerAdapter
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.viewModelFactory
import com.github.terrakok.cicerone.Router
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@ExperimentalBadgeUtils
@SuppressLint("UnsafeExperimentalUsageError")
class HeadlinesFragment : Fragment(), OnBackPressedListener {

    @Inject
    internal lateinit var viewModelProvider: Provider<HeadlinesViewModel>

    private var _binding: FragmentHeadlinesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    val badgeDrawable by lazy { BadgeDrawable.create(requireContext()) }

    private val viewModel by viewModelFactory { viewModelProvider.get() }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewPager()
        initToolBar()
        initTabLayout()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed() {
        viewModel.navigateToBack()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.filterDataFlow.collect(::setBadge)
                }
            }
        }
    }
    private fun initTabLayout(){
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            when (pos) {
                0 -> with(tab) {
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.business_icon)
                    text = "Business"
                }

                1 -> with(tab) {
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.general_icon)
                    text = "General"
                }

                2 -> with(tab) {
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.science_icon)
                    text = "Science"
                }
            }
        }.attach()
    }
    private fun initToolBar() {
        binding.materialToolbarHeadlines.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.headlines_toolbar, menu)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.searchView -> {
                        viewModel.navigateToSearch()
                    }

                    R.id.filterHeadlines -> {
                        viewModel.navigateToFilter()
                    }
                }
                return true
            }
        }, viewLifecycleOwner)
    }

    private fun initViewPager() {
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = viewPagerAdapter
    }

    private fun setBadge(data: SharedDataType.Filter) {
        BadgeUtils.attachBadgeDrawable(
            badgeDrawable,
            binding.materialToolbarHeadlines,
            R.id.filterHeadlines
        )
        if (data.count == 0) {
            badgeDrawable.isVisible = false
        } else {
            badgeDrawable.isVisible = true
            badgeDrawable.number = data.count
        }
    }

}