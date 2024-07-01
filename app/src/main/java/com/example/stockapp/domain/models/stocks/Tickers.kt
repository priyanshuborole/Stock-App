package com.example.stockapp.domain.models.stocks

import com.google.gson.annotations.SerializedName

data class Tickers(
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("metadata")
    val metadata: String,
    @SerializedName("most_actively_traded")
    val mostActivelyTraded: List<MostActivelyTraded>,
    @SerializedName("top_gainers")
    val topGainers: List<TopGainer>,
    @SerializedName("top_losers")
    val topLosers: List<TopLoser>
)

enum class TickerMarketType (val displayName: String){
    TOP_GAINERS("Top Gainers"),
    TOP_LOSERS("Top Losers"),
    MOST_ACTIVELY_TRADED("Most Active")
}