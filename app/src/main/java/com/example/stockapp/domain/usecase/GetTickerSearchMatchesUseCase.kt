package com.example.stockapp.domain.usecase

import com.example.stockapp.common.ApiState
import com.example.stockapp.domain.models.ticker_search.TickerSearch
import com.example.stockapp.domain.repository.StockApiRepository
import kotlinx.coroutines.flow.Flow

class GetTickerSearchMatchesUseCase(
    private val stockApiRepository: StockApiRepository
) {
    suspend operator fun invoke(keywords: String) : Flow<ApiState<TickerSearch>> = stockApiRepository.getTickerSearchMatches(keywords)
}