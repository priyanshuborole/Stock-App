package com.example.stockapp.data.repository

import com.example.stockapp.common.ApiState
import com.example.stockapp.data.csv.CSVParser
import com.example.stockapp.data.csv.IntraDayInfoParser
import com.example.stockapp.data.remote.StockApiDataSource
import com.example.stockapp.domain.models.overview.CompanyOverview
import com.example.stockapp.domain.models.overview.IntraDayInfo
import com.example.stockapp.domain.models.stocks.Tickers
import com.example.stockapp.domain.models.ticker_search.TickerSearch
import com.example.stockapp.domain.repository.StockApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StockApiRepositoryImpl(
    private val stockApiDataSource: StockApiDataSource,
    private val intraDayInfoParser: IntraDayInfoParser,
) : StockApiRepository {

    override suspend fun getTopGainersLosersAndMostActivelyTraded(): Flow<ApiState<Tickers>> =
        flow {
            emit(ApiState.Loading())
            try {
                val result = stockApiDataSource.getTopGainersLosersAndMostActivelyTraded()
                if (result.isSuccessful) {
                    result.body()?.let {
                        emit(ApiState.Success(it))
                    }
                } else {
                    emit(ApiState.Error("Something went wrong"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiState.Error("Something went wrong with ${e.message}"))
            }
        }

    override suspend fun getCompanyOverview(symbol: String): Flow<ApiState<CompanyOverview>> =
        flow {
            emit(ApiState.Loading())
            try {
                val result = stockApiDataSource.getCompanyOverview(symbol = symbol)
                if (result.isSuccessful) {
                    result.body()?.let {
                        emit(ApiState.Success(it))
                    }
                } else {
                    emit(ApiState.Error("Something went wrong"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiState.Error("Something went wrong with ${e.message}"))
            }
        }

    override suspend fun getTickerSearchMatches(keywords: String): Flow<ApiState<TickerSearch>> =
        flow {
            emit(ApiState.Loading(showLoading = true))
            try {
                val result = stockApiDataSource.getTickerSearchMatches(keywords = keywords)
                if (result.isSuccessful) {
                    result.body()?.let {
                        emit(ApiState.Success(it))
                    }
                } else {
                    emit(ApiState.Error("Something went wrong"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(ApiState.Error("Something went wrong with ${e.message}"))
            }
        }

    override suspend fun getIntraDayInfo(symbol: String): Flow<ApiState<List<IntraDayInfo>>> =
        flow {
            emit(ApiState.Loading())
            try {
                val response = stockApiDataSource.getIntraDayInfo(symbol = symbol)
                val results = intraDayInfoParser.parse(response.byteStream())
                emit(ApiState.Success(results))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(
                    ApiState.Error(
                        message = "Couldn't load intraDay info"
                    )
                )
            }
        }

}