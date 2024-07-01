package com.example.stockapp.domain.usecase

import com.example.stockapp.common.ApiState
import com.example.stockapp.domain.models.overview.CompanyOverview
import com.example.stockapp.domain.repository.StockApiRepository
import kotlinx.coroutines.flow.Flow

class GetCompanyOverviewUseCase (
    private val stockApiRepository: StockApiRepository
) {
    suspend operator fun invoke(symbol: String): Flow<ApiState<CompanyOverview>> = stockApiRepository.getCompanyOverview(symbol)
}