package com.example.stockapp.ui.ticker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.common.ApiState
import com.example.stockapp.domain.models.ticker_search.TickerSearch
import com.example.stockapp.domain.usecase.GetTickerSearchMatchesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class TickerSearchViewModel @Inject constructor(
    getTickerSearchMatchesUseCase: GetTickerSearchMatchesUseCase
) : ViewModel() {

    private val _searchResults = MutableStateFlow<ApiState<TickerSearch>>(ApiState.Loading(showLoading = false))
    val searchResults: StateFlow<ApiState<TickerSearch>> = _searchResults.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private val _selectedChipType = MutableStateFlow("All")


    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .flatMapLatest { keywords ->
                    getTickerSearchMatchesUseCase(keywords)
                }
                .combine(_selectedChipType) { searchResults, selectedChipType ->
                    filterByType(searchResults, selectedChipType)
                }
                .collect { state ->
                    _searchResults.value = state
                }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedChipType(type: String) {
        _selectedChipType.value = type
    }

    private fun filterByType(searchResults: ApiState<TickerSearch>, type: String): ApiState<TickerSearch> {
        return when (searchResults) {
            is ApiState.Success -> {
                val filteredResults: TickerSearch = if (type == "All") {
                    searchResults.data ?: TickerSearch(emptyList())
                } else {
                    val filteredList = searchResults.data?.bestMatches?.filter { it.type == type }
                    if (filteredList != null) {
                        TickerSearch(filteredList)
                    }
                    else{
                        TickerSearch(emptyList())
                    }
                }
                ApiState.Success(filteredResults)
            }
            else -> searchResults
        }
    }
}