package com.example.appnews.presentation.headlines.filter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.util.Pair
import androidx.core.util.toKotlinPair
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appnews.App
import com.example.appnews.R
import com.example.appnews.databinding.FragmentFilterBinding
import com.example.appnews.presentation.navigation.OnBackPressedListener
import com.example.appnews.presentation.viewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@RequiresApi(Build.VERSION_CODES.O)
class FilterFragment : Fragment(R.layout.fragment_filter), OnBackPressedListener {

    @Inject
    internal lateinit var viewModelProvider: Provider<FilterViewModel>

    private val binding by viewBinding(FragmentFilterBinding::bind)

    private val viewModel by viewModelFactory { viewModelProvider.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveResultFilterIcon()
        initButtons()
        observe()
        backArrow()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTextViewDate(date: String) {
        binding.tvDateRange.text = date
    }
    override fun onBackPressed() {
        viewModel.navigateToBack()
    }

    private fun initButtons() {
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
    }

    private fun observe() {
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
            }
        }
    }

    private fun backArrow() {
        binding.imageButtonBackFilter.setOnClickListener {
            viewModel.navigateToBack()
        }
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

}