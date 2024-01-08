package com.example.appnews.presentation.headlines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.databinding.FragmentHeadlinesBinding
import com.example.appnews.presentation.headlines.tabfragment.ViewPagerAdapter
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.tabs.TabLayoutMediator

class HeadlinesFragment : Fragment() {

    val routerHeadlines get() = (requireActivity().application as App).cicerone.router
    val navigatorHolderHeadlines get() = (requireActivity().application as App).cicerone.getNavigatorHolder()

    val headlinesNavigator = AppNavigator(requireActivity(), R.id.headlines_root)




    private var _binding:FragmentHeadlinesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2



    private  val viewModel by activityViewModels<HeadlinesViewModel> { HeadlinesViewModel.Factory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlinesBinding.inflate(inflater, container, false )
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = viewPagerAdapter
        val tabLayout = binding.tabLayout


           TabLayoutMediator(tabLayout, viewPager) { tab, pos ->

               when (pos){
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        navigatorHolderHeadlines.setNavigator(headlinesNavigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolderHeadlines.removeNavigator()
    }







}