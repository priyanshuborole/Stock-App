package com.example.stockapp.domain.usecase

import com.example.stockapp.common.ApiState
import com.example.stockapp.domain.models.stocks.MostActivelyTraded
import com.example.stockapp.domain.models.stocks.TopGainer
import com.example.stockapp.domain.models.stocks.TopLoser
import com.example.stockapp.domain.repository.StockApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTopGainersLosersAndMostActivelyTradedUseCase(
    private val stockApiRepository: StockApiRepository
) {
    suspend operator fun invoke(): Triple<Flow<ApiState<List<TopGainer>>>, Flow<ApiState<List<TopLoser>>>,Flow<ApiState<List<MostActivelyTraded>>>> {
        val resultFlow = stockApiRepository.getTopGainersLosersAndMostActivelyTraded()
        val gainersFlow = resultFlow.map { apiState ->
            when (apiState) {
                is ApiState.Success -> ApiState.Success(apiState.data?.topGainers ?: emptyList())
                is ApiState.Error -> ApiState.Error(apiState.message ?: "Unknown error")
                is ApiState.Loading -> ApiState.Loading()
            }
        }
        val losersFlow = resultFlow.map { apiState ->
            when (apiState) {
                is ApiState.Success -> ApiState.Success(apiState.data?.topLosers ?: emptyList())
                is ApiState.Error -> ApiState.Error(apiState.message ?: "Unknown error")
                is ApiState.Loading -> ApiState.Loading()
            }
        }
        val mostActivelyTradedFlow = resultFlow.map { apiState ->
            when (apiState) {
                is ApiState.Success -> ApiState.Success(apiState.data?.mostActivelyTraded ?: emptyList())
                is ApiState.Error -> ApiState.Error(apiState.message ?: "Unknown error")
                is ApiState.Loading -> ApiState.Loading()
            }
        }

        return Triple(gainersFlow, losersFlow, mostActivelyTradedFlow)
    }
}