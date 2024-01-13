package com.example.appnews.presentation.saves

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appnews.App
import com.example.appnews.databinding.FragmentSaveBinding
import com.example.appnews.presentation.navigation.OnBackPressedListener

class SaveFragment : Fragment(), OnBackPressedListener {

	private var _binding: FragmentSaveBinding? = null
	private val binding get() = _binding!!

	private lateinit var viewModel: SaveViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentSaveBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(this).get(SaveViewModel::class.java)
	}

	override fun onBackPressed() {
		(requireActivity().application as App).router.exit()
	}

}