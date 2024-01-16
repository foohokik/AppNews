package com.example.appnews.presentation.headlines

import android.os.Bundle
import android.util.Log
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
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.google.android.material.tabs.TabLayoutMediator
import java.text.FieldPosition

class HeadlinesFragment : Fragment(), OnBackPressedListener {

	private var _binding: FragmentHeadlinesBinding? = null
	private val binding get() = _binding!!

	private lateinit var viewPagerAdapter: ViewPagerAdapter
	private lateinit var viewPager: ViewPager2


	private val viewModel by activityViewModels<HeadlinesViewModel> { HeadlinesViewModel.Factory }


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewPagerAdapter = ViewPagerAdapter(this)
		viewPager = binding.pager
		viewPager.adapter = viewPagerAdapter
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

		viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){

			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				Log.d("LOG", "t "+position)
				viewModel.setCurrentTab(position)
			}

		})

	}



	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun onBackPressed() {
		(requireActivity().application as App).router.exit()
	}

}