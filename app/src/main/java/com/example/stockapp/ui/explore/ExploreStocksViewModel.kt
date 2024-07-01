package com.example.stockapp.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.common.ApiState
import com.example.stockapp.domain.models.stocks.MostActivelyTraded
import com.example.stockapp.domain.models.stocks.TickerMarketType
import com.example.stockapp.domain.models.stocks.TopGainer
import com.example.stockapp.domain.models.stocks.TopLoser
import com.example.stockapp.domain.usecase.GetTopGainersLosersAndMostActivelyTradedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreStocksViewModel @Inject constructor(
    private val getTopGainersLosersAndMostActivelyTradedUseCase: GetTopGainersLosersAndMostActivelyTradedUseCase
) : ViewModel() {

    private val _topGainers = MutableStateFlow<ApiState<List<TopGainer>>>(ApiState.Loading())
    val topGainers: StateFlow<ApiState<List<TopGainer>>> = _topGainers

    private val _topLosers = MutableStateFlow<ApiState<List<TopLoser>>>(ApiState.Loading())
    val topLosers: StateFlow<ApiState<List<TopLoser>>> = _topLosers

    private val _mostActivelyTraded = MutableStateFlow<ApiState<List<MostActivelyTraded>>>(ApiState.Loading())
    val mostActivelyTraded: StateFlow<ApiState<List<MostActivelyTraded>>> = _mostActivelyTraded

    fun getTopGainersLosersAndMostActivelyTraded() = viewModelScope.launch {
        val (gainersFlow, losersFlow, mostActivelyTradedFlow) = getTopGainersLosersAndMostActivelyTradedUseCase()
        launch {
            gainersFlow.collect { apiState ->
                _topGainers.update { apiState }
            }
        }
        launch {
            losersFlow.collect { apiState ->
                _topLosers.update { apiState }
            }
        }
        launch {
            mostActivelyTradedFlow.collect { apiState ->
                _mostActivelyTraded.update { apiState }
            }
        }
    }
}