package com.example.stockapp.data.remote

import com.example.stockapp.common.Constants.Companion.API_KEY
import com.example.stockapp.common.Constants.Companion.COMPANY_OVERVIEW_FUNCTION
import com.example.stockapp.common.Constants.Companion.SYMBOL_SEARCH_FUNCTION
import com.example.stockapp.common.Constants.Companion.TIME_SERIES_INTRADAY_FUNCTION
import com.example.stockapp.common.Constants.Companion.TOP_GAINERS_LOSERS_MOST_ACTIVE_FUNCTION
import com.example.stockapp.domain.models.overview.CompanyOverview
import com.example.stockapp.domain.models.stocks.Tickers
import com.example.stockapp.domain.models.ticker_search.TickerSearch
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiDataSource {

    @GET("/query")
    suspend fun getTopGainersLosersAndMostActivelyTraded(
        @Query("function")
        function: String = TOP_GAINERS_LOSERS_MOST_ACTIVE_FUNCTION,
        @Query("apikey")
        apikey: String = API_KEY
    ) : Response<Tickers>

    @GET("/query")
    suspend fun getCompanyOverview(
        @Query("function")
        function: String = COMPANY_OVERVIEW_FUNCTION,
        @Query("symbol")
        symbol: String,
        @Query("apikey")
        apikey: String = API_KEY
    ) : Response<CompanyOverview>

    @GET("/query")
    suspend fun getTickerSearchMatches(
        @Query("function")
        function: String = SYMBOL_SEARCH_FUNCTION,
        @Query("keywords")
        keywords: String,
        @Query("apikey")
        apikey: String = API_KEY
    ) : Response<TickerSearch>

    @GET("/query")
    suspend fun getIntraDayInfo(
        @Query("function")
        function: String = TIME_SERIES_INTRADAY_FUNCTION,
        @Query("symbol")
        symbol: String,
        @Query("interval")
        interval: String = "5min",
        @Query("apikey")
        apiKey: String = API_KEY,
        @Query("datatype")
        datatype: String = "csv"
    ): ResponseBody
}