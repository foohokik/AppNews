package com.example.appnews.presentation.headlines

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.core.util.toKotlinPair
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.databinding.FragmentFilterBinding
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class FilterFragment : Fragment(), OnBackPressedListener {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FilterViewModel> { FilterViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

                        if (it.first.isNotEmpty() && it.second.isNotEmpty()) {
                            convertDateAndSetTextView(it)
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
    fun convertDateAndSetTextView(period: kotlin.Pair<String, String>) {

        val startDate = period.first
        val endDate = period.second

        if (startDate == endDate) {
            val equalForBothDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val formatter = DateTimeFormatter.ofPattern("MMM d yyyy")
            val convertDate = equalForBothDate.format(formatter)
            binding.tvDateRange.text = convertDate
        } else {
            val parsedStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val parsedEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val formatter = DateTimeFormatter.ofPattern("MMM d")
            val convertStartDate = parsedStartDate.format(formatter)
            val convertEndDate = parsedEndDate.format(formatter)
            val year = startDate.substring(0, 4)
            val finishDateRangeToTextView = "$convertStartDate - $convertEndDate, $year"
            binding.tvDateRange.text = finishDateRangeToTextView
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed() {
        (requireActivity().application as App).router.exit()
    }

    fun backArrow() {
        binding.imageButtonBackFilter.setOnClickListener {
            (requireActivity().application as App).router.exit()
        }
    }

    private fun handleSideEffect(country: String) {
        setFragmentResult("request_key", bundleOf("country" to country))
        (requireActivity().application as App).router.exit()
    }

    fun saveResultFilterIcon() {
        binding.imageButtonOk.setOnClickListener {
            viewModel.sendResultCountry()
        }
    }

    fun initDatePicker() {
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

}