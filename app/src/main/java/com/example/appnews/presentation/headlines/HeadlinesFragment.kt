package com.example.appnews.presentation.headlines

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.Screens
import com.example.appnews.core.viewclasses.SharedDataType
import com.example.appnews.databinding.FragmentHeadlinesBinding
import com.example.appnews.presentation.customGetSerializable
import com.example.appnews.presentation.headlines.tabfragment.ViewPagerAdapter
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.text.FieldPosition

@ExperimentalBadgeUtils @SuppressLint("UnsafeExperimentalUsageError")
class HeadlinesFragment : Fragment(), OnBackPressedListener {

    private var _binding: FragmentHeadlinesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    val badgeDrawable by lazy { BadgeDrawable.create(requireContext()) }
    private val viewModel by activityViewModels<HeadlinesViewModel> { HeadlinesViewModel.Factory }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setFragmentResultListener("request_key") { key, bundle ->
            val data = bundle.customGetSerializable<SharedDataType.Filter>("data")
            data?.let { viewModel.onWriteData(it) }

        }

        initViewPager()
        val tabLayout = binding.tabLayout


        binding.materialToolbarHeadlines.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.headlines_toolbar, menu)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.searchView -> {

                        (requireActivity().application as App).router.navigateTo(Screens.searchHeadlinesFragment())
                    }

                    R.id.filterHeadlines -> {

                        (requireActivity().application as App).router.navigateTo(Screens.filterFragment())
                    }
                }
                return true
            }
        }, viewLifecycleOwner)



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

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //viewModel.setCurrentTab(position)
            }

        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.filterDataFlow.collect(::setBadge)
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

    fun initViewPager() {

        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = viewPagerAdapter

    }

    fun setBadge (data: SharedDataType.Filter) {

        BadgeUtils.attachBadgeDrawable (badgeDrawable, binding.materialToolbarHeadlines, R.id.filterHeadlines)
        if (data.count == 0) {
            badgeDrawable.isVisible = false
        } else {
            badgeDrawable.isVisible = true
            badgeDrawable.number = data.count

        }
    }

}