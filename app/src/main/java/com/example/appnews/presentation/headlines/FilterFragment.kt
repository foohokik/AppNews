package com.example.appnews.presentation.headlines

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.core.util.toKotlinPair
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.core.shared.SharedDataType
import com.example.appnews.databinding.FragmentFilterBinding
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.viewModelFactory
import com.github.terrakok.cicerone.Router
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


@RequiresApi(Build.VERSION_CODES.O)
class FilterFragment : Fragment(), OnBackPressedListener {

    @Inject
    lateinit var router: Router
    @Inject
    internal lateinit var viewModelProvider: Provider<FilterViewModel>

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by  viewModelFactory { viewModelProvider.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            ivCalendar.setOnClickListener {
                initDatePicker()
            }
            btnPopular.setOnClickListener {
                viewModel.changeIsPressedFlagPopular()
            }

            btnNew.setOnClickListener {
                viewModel.changeIsPressedFlagNew()
            }

            btnRelevant.setOnClickListener {
                viewModel.changeIsPressedFlagRelevant()
            }

            btnEng.setOnClickListener {
                viewModel.changeIsPressedFlagEnglish()
            }

            btnRus.setOnClickListener {
                viewModel.changeIsPressedFlagRussian()
            }

            btnDeutsch.setOnClickListener {
                viewModel.changeIsPressedFlagDeutsch()
            }

        }
        saveResultFilterIcon()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.buttonEnglishIsPressed.collect {
                        binding.btnEng.setData(it)
                    }
                }
                launch {
                    viewModel.buttonRussianIsPressed.collect {
                        binding.btnRus.setData(it)
                    }
                }
                launch {
                    viewModel.buttonDeutschIsPressed.collect {
                        binding.btnDeutsch.setData(it)
                    }
                }
                launch {
                    viewModel.buttonPopularIsPressed.collect {
                        binding.btnPopular.setData(it)
                    }
                }
                launch {
                    viewModel.buttonNewIsPressed.collect {
                        binding.btnNew.setData(it)
                    }
                }
                launch {
                    viewModel.buttonRelevantIsPressed.collect {
                        binding.btnRelevant.setData(it)
                    }
                }
                launch {
                    viewModel.dateRange.collect {
                        if (it.isNotEmpty()) {
                            setTextViewDate(it)
                        }
                    }
                }
                launch {
                    viewModel.sideEffectsChange.collect(::handleSideEffect)
                }
            }
        }

        backArrow()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setTextViewDate(date:String) {
            binding.tvDateRange.text = date
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed() {
        router.exit()
    }

    private fun backArrow() {
        binding.imageButtonBackFilter.setOnClickListener {
            router.exit()
        }
    }

    private fun handleSideEffect(data: SharedDataType.Filter) {
        setFragmentResult("request_key", bundleOf("data" to data))
        router.exit()
    }


    private fun saveResultFilterIcon() {
        binding.imageButtonOk.setOnClickListener {
            viewModel.sendResult()
        }
    }


    private fun initDatePicker() {
        val dateRange: MaterialDatePicker<Pair<Long, Long>> = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setTheme(R.style.CustomThemeOverlay_MaterialCalendar_Fullscreen)
                .setTitleText("Select a date")
                .build()
        dateRange.show(childFragmentManager, "DATE")
        dateRange.addOnPositiveButtonClickListener {
            viewModel.onChangeDate(it.toKotlinPair())
        }

    }

//    companion object {
//
//        const val ARG = "ARG"
//
//        @JvmStatic
//        fun newInstance(data: SharedDataType.Filter: ArticlesUI.Article) = FilterFragment().apply {
//            arguments = Bundle().apply {
//
//                putSerializable(ARG, data)
//
//            }
//        }
//    }

}