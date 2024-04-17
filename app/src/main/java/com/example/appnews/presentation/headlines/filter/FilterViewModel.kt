package com.example.appnews.presentation.headlines.filter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnews.core.viewclasses.FilterTypes
import com.example.appnews.core.viewclasses.ModelFilterButtons
import com.example.appnews.core.shared.ShareDataClass
import com.example.appnews.core.shared.SharedDataType
import com.github.terrakok.cicerone.Router
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
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class FilterViewModel @Inject constructor(
    private val sharedClass: ShareDataClass,
    private val router: Router
) : ViewModel() {

    private val _buttonPopularIsPressed =
        MutableStateFlow(ModelFilterButtons(FilterTypes.POPULAR, false))
    val buttonPopularIsPressed = _buttonPopularIsPressed.asStateFlow()

    private val _buttonNewIsPressed = MutableStateFlow(ModelFilterButtons(FilterTypes.NEW, false))
    val buttonNewIsPressed = _buttonNewIsPressed.asStateFlow()

    private val _buttonRelevantIsPressed =
        MutableStateFlow(ModelFilterButtons(FilterTypes.RELEVANT, false))
    val buttonRelevantIsPressed = _buttonRelevantIsPressed.asStateFlow()

    private val _buttonRussianIsPressed =
        MutableStateFlow(ModelFilterButtons(FilterTypes.RUSSIAN, false))
    val buttonRussianIsPressed = _buttonRussianIsPressed.asStateFlow()

    private val _buttonEnglishIsPressed =
        MutableStateFlow(ModelFilterButtons(FilterTypes.ENGLISH, false))
    val buttonEnglishIsPressed = _buttonEnglishIsPressed.asStateFlow()

    private val _buttonDeutschIsPressed =
        MutableStateFlow(ModelFilterButtons(FilterTypes.DEUTSCH, false))
    val buttonDeutschIsPressed = _buttonDeutschIsPressed.asStateFlow()

    private val _dateRange: MutableStateFlow<String> = MutableStateFlow("")
    val dateRange = _dateRange.asStateFlow()

    private var countOfChosenFilter = 0

    private val listOfCountryFlow = listOf(
        _buttonEnglishIsPressed.value,
        _buttonDeutschIsPressed.value, _buttonRussianIsPressed.value
    )

    private val listOfSortFlow = listOf(
        _buttonRelevantIsPressed.value,
        _buttonNewIsPressed.value, _buttonPopularIsPressed.value
    )

    init {
        viewModelScope.launch {
            sharedClass.reviewSearchSideEffect.collect(::determineButtonState)
        }
    }

    private fun determineButtonState(dataStateType: SharedDataType) {
        val country = listOfCountryFlow.find {
            it.type.code == (dataStateType as SharedDataType.Filter).country
        }
        when (country?.type?.code) {
            "us" -> _buttonEnglishIsPressed.value =
                _buttonEnglishIsPressed.value.copy(isPressed = true)

            "ru" -> _buttonRussianIsPressed.value =
                _buttonRussianIsPressed.value.copy(isPressed = true)

            "de" -> _buttonDeutschIsPressed.value =
                _buttonDeutschIsPressed.value.copy(isPressed = true)
        }

        val sortElement = listOfSortFlow.find {
            it.type.code == (dataStateType as SharedDataType.Filter).sotrBy
        }
        when (sortElement?.type?.code) {
            "popular" -> _buttonPopularIsPressed.value =
                _buttonPopularIsPressed.value.copy(isPressed = true)

            "relevant" -> _buttonRelevantIsPressed.value =
                _buttonRelevantIsPressed.value.copy(isPressed = true)

            "new" -> _buttonNewIsPressed.value = _buttonNewIsPressed.value.copy(isPressed = true)
        }
        _dateRange.value = (dataStateType as SharedDataType.Filter).date
        countOfChosenFilter = dataStateType.count
    }

    private fun determineCountryValue(): String {
        val listOfCountryFlow = listOf(
            _buttonEnglishIsPressed.value,
            _buttonDeutschIsPressed.value, _buttonRussianIsPressed.value
        )
        val country = listOfCountryFlow.find {
            it.isPressed
        }

        if (country != null) {
            countOfChosenFilter++
        }
        return country?.type?.code.orEmpty()
    }

    private fun determineSortValue(): String {
        val listOfSortFlow = listOf<ModelFilterButtons>(
            _buttonRelevantIsPressed.value,
            _buttonNewIsPressed.value, _buttonPopularIsPressed.value
        )
        val sortElement = listOfSortFlow.find {
            it.isPressed
        }
        if (sortElement != null) {
            countOfChosenFilter++
        }
        return sortElement?.type?.code.orEmpty()
    }

    private fun determineCountOfChosenFilters(): Int {
        val listOfButton = listOf(
            _buttonRelevantIsPressed.value,
            _buttonNewIsPressed.value, _buttonPopularIsPressed.value, _buttonEnglishIsPressed.value,
            _buttonDeutschIsPressed.value, _buttonRussianIsPressed.value
        )
        val pressedButtons = listOfButton.filter {
            it.isPressed
        }
        countOfChosenFilter = pressedButtons.size
        if (_dateRange.value.length > 0) {
            countOfChosenFilter++
        }

        return countOfChosenFilter
    }

    fun navigateToBack() {
        router.exit()
    }

    fun sendResult() {
        sharedClass.setData(
            SharedDataType.Filter(
                country = determineCountryValue(),
                sotrBy = determineSortValue(),
                date = _dateRange.value,
                count = determineCountOfChosenFilters()
            )
        )
        router.exit()
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
        val isCondition = true
        _buttonRussianIsPressed.value = _buttonRussianIsPressed.value.copy(isPressed = isCondition)
        if (isCondition) {
            _buttonEnglishIsPressed.value = _buttonEnglishIsPressed.value.copy(isPressed = false)
            _buttonDeutschIsPressed.value = _buttonDeutschIsPressed.value.copy(isPressed = false)
        }
    }

    fun changeIsPressedFlagEnglish() {
        val isCondition = true
        _buttonEnglishIsPressed.value = _buttonEnglishIsPressed.value.copy(isPressed = isCondition)
        if (isCondition) {
            _buttonRussianIsPressed.value = _buttonRussianIsPressed.value.copy(isPressed = false)
            _buttonDeutschIsPressed.value = _buttonDeutschIsPressed.value.copy(isPressed = false)
        }
    }

    fun changeIsPressedFlagDeutsch() {
        val isCondition = true
        _buttonDeutschIsPressed.value = _buttonDeutschIsPressed.value.copy(isPressed = isCondition)
        if (isCondition) {
            _buttonRussianIsPressed.value = _buttonRussianIsPressed.value.copy(isPressed = false)
            _buttonEnglishIsPressed.value = _buttonEnglishIsPressed.value.copy(isPressed = false)
        }
    }

    fun onChangeDate(period: Pair<Long, Long>) {
        val startDate = period.first
        val endDate = period.second
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateString = sdf.format(Date(startDate))
        val endDateString = sdf.format(Date(endDate))

        if (startDateString == endDateString) {
            val equalForBothDate =
                LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val formatter = DateTimeFormatter.ofPattern("MMM d yyyy")
            val convertDate = equalForBothDate.format(formatter)
            _dateRange.value = convertDate
        } else {
            val parsedStartDate =
                LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val parsedEndDate =
                LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val formatter = DateTimeFormatter.ofPattern("MMM d")
            val convertStartDate = parsedStartDate.format(formatter)
            val convertEndDate = parsedEndDate.format(formatter)
            val year = startDateString.substring(0, 4)
            val finishDateRangeToTextView = "$convertStartDate - $convertEndDate, $year"
            _dateRange.value = finishDateRangeToTextView
        }
        countOfChosenFilter++
    }

}