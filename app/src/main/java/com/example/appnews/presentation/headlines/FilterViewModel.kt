package com.example.appnews.presentation.headlines

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.appnews.App
import com.example.appnews.core.FilterTypes
import com.example.appnews.core.ModelFilterButtons
import com.example.appnews.data.repository.NewsRepository
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
class FilterViewModel(private val newsRepository: NewsRepository) : ViewModel() {


    private val _buttonPopularIsPressed = MutableStateFlow(ModelFilterButtons(FilterTypes.POPULAR, false))
    val buttonPopularIsPressed = _buttonPopularIsPressed.asStateFlow()

    private val _buttonNewIsPressed = MutableStateFlow(ModelFilterButtons(FilterTypes.NEW, false))
    val buttonNewIsPressed = _buttonNewIsPressed.asStateFlow()

    private val _buttonRelevantIsPressed = MutableStateFlow(ModelFilterButtons(FilterTypes.RELEVANT, false))
    val buttonRelevantIsPressed = _buttonRelevantIsPressed.asStateFlow()

    private val _buttonRussianIsPressed = MutableStateFlow(ModelFilterButtons(FilterTypes.RUSSIAN, false))
    val buttonRussianIsPressed = _buttonRussianIsPressed.asStateFlow()

    private val _buttonEnglishIsPressed = MutableStateFlow(ModelFilterButtons(FilterTypes.ENGLISH, false))
    val buttonEnglishIsPressed = _buttonEnglishIsPressed.asStateFlow()

    private val _buttonDeutschIsPressed = MutableStateFlow(ModelFilterButtons(FilterTypes.DEUTSCH, false))
    val buttonDeutschIsPressed = _buttonDeutschIsPressed.asStateFlow()

    private val _dateRange: MutableStateFlow<String> = MutableStateFlow("")
    val dateRange = _dateRange.asStateFlow()

    private val _sideEffectChange = Channel<String>()
    val sideEffectsChange = _sideEffectChange.receiveAsFlow()

    private fun determineCountryValue(): String {
        val listOfCountryFlow = listOf<ModelFilterButtons>(_buttonEnglishIsPressed.value, _buttonDeutschIsPressed.value, _buttonRussianIsPressed.value)

        val element = listOfCountryFlow.find {
            it.isPressed
        }
        return element?.type?.code.orEmpty()

    }


    fun sendResultCountry() {
        viewModelScope.launch {
            _sideEffectChange.send(determineCountryValue())
        }

    }

    fun changeIsPressedFlagPopular() {
        val isCondition = !_buttonPopularIsPressed.value.isPressed
        _buttonPopularIsPressed.value = _buttonPopularIsPressed.value
                .copy(isPressed = isCondition)
        if (isCondition) {
            _buttonRelevantIsPressed.value = _buttonRelevantIsPressed.value.copy(isPressed = false)
            _buttonNewIsPressed.value = _buttonNewIsPressed.value.copy(isPressed = false)
        }

    }


    fun onChangeDate(period: Pair<Long, Long>) {
        val startDate = period.first
        val endDate = period.second
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateString = sdf.format(Date(startDate))
        val endDateString = sdf.format(Date(endDate))

        if (startDateString == endDateString) {
            val equalForBothDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val formatter = DateTimeFormatter.ofPattern("MMM d yyyy")
            val convertDate = equalForBothDate.format(formatter)
            _dateRange.value = convertDate
        } else {
            val parsedStartDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val parsedEndDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val formatter = DateTimeFormatter.ofPattern("MMM d")
            val convertStartDate = parsedStartDate.format(formatter)
            val convertEndDate = parsedEndDate.format(formatter)
            val year = startDateString.substring(0, 4)
            val finishDateRangeToTextView = "$convertStartDate - $convertEndDate, $year"
            _dateRange.value = finishDateRangeToTextView
        }

    }

    fun changeIsPressedFlagNew() {
        val isCondition = !_buttonNewIsPressed.value.isPressed
        _buttonNewIsPressed.value = _buttonNewIsPressed.value
                .copy(isPressed = isCondition)
        if (isCondition) {
            _buttonRelevantIsPressed.value = _buttonRelevantIsPressed.value.copy(isPressed = false)
            _buttonPopularIsPressed.value = _buttonPopularIsPressed.value.copy(isPressed = false)
        }
    }

    fun changeIsPressedFlagRelevant() {

        val isCondition = !_buttonRelevantIsPressed.value.isPressed
        _buttonRelevantIsPressed.value = _buttonRelevantIsPressed.value
                .copy(isPressed = isCondition)
        if (isCondition) {
            _buttonNewIsPressed.value = _buttonNewIsPressed.value.copy(isPressed = false)
            _buttonPopularIsPressed.value = _buttonPopularIsPressed.value.copy(isPressed = false)
        }
    }

    fun changeIsPressedFlagRussian() {

        val isCondition = !_buttonRussianIsPressed.value.isPressed
        _buttonRussianIsPressed.value = _buttonRussianIsPressed.value.copy(isPressed = isCondition)
        if (isCondition) {
            _buttonEnglishIsPressed.value = _buttonEnglishIsPressed.value.copy(isPressed = false)
            _buttonDeutschIsPressed.value = _buttonDeutschIsPressed.value.copy(isPressed = false)
        }

    }

    fun changeIsPressedFlagEnglish() {

        val isCondition = !_buttonEnglishIsPressed.value.isPressed
        _buttonEnglishIsPressed.value = _buttonEnglishIsPressed.value.copy(isPressed = isCondition)
        if (isCondition) {
            _buttonRussianIsPressed.value = _buttonRussianIsPressed.value.copy(isPressed = false)
            _buttonDeutschIsPressed.value = _buttonDeutschIsPressed.value.copy(isPressed = false)
        }

    }

    fun changeIsPressedFlagDeutsch() {

        val isCondition = !_buttonDeutschIsPressed.value.isPressed
        _buttonDeutschIsPressed.value = _buttonDeutschIsPressed.value.copy(isPressed = isCondition)
        if (isCondition) {
            _buttonRussianIsPressed.value = _buttonRussianIsPressed.value.copy(isPressed = false)
            _buttonEnglishIsPressed.value = _buttonEnglishIsPressed.value.copy(isPressed = false)
        }

    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
            ): T {
                val application =
                        checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                extras.createSavedStateHandle()

                return FilterViewModel(
                        (application as App).newsRepository
                ) as T
            }
        }
    }

}