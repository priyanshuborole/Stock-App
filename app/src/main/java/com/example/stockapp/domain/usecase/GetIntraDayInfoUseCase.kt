package com.example.stockapp.domain.usecase

import com.example.stockapp.domain.repository.StockApiRepository

class GetIntraDayInfoUseCase(
    private val stockApiRepository: StockApiRepository
) {
    suspend operator fun invoke(symbol: String) = stockApiRepository.getIntraDayInfo(symbol)
}