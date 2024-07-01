package com.example.stockapp.domain.repository

import com.example.stockapp.common.ApiState
import com.example.stockapp.domain.models.overview.CompanyOverview
import com.example.stockapp.domain.models.overview.IntraDayInfo
import com.example.stockapp.domain.models.stocks.Tickers
import com.example.stockapp.domain.models.ticker_search.TickerSearch
import kotlinx.coroutines.flow.Flow

interface StockApiRepository {

    suspend fun getTopGainersLosersAndMostActivelyTraded() : Flow<ApiState<Tickers>>

    suspend fun getCompanyOverview(symbol: String) : Flow<ApiState<CompanyOverview>>

    suspend fun getTickerSearchMatches(keywords: String) : Flow<ApiState<TickerSearch>>

    suspend fun getIntraDayInfo(symbol: String): Flow<ApiState<List<IntraDayInfo>>>

}