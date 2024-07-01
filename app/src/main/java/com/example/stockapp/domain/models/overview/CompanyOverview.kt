package com.example.stockapp.domain.models.overview

import com.google.gson.annotations.SerializedName

data class CompanyOverview(
    @SerializedName("200DayMovingAverage")
    val day200MovingAverage: String,
    @SerializedName("50DayMovingAverage")
    val day50MovingAverage: String,
    @SerializedName("52WeekHigh")
    val week52High: String,
    @SerializedName("52WeekLow")
    val week52Low: String,
    @SerializedName("Address")
    val address: String,
    @SerializedName("AnalystRatingBuy")
    val analystRatingBuy: String,
    @SerializedName("AnalystRatingHold")
    val analystRatingHold: String,
    @SerializedName("AnalystRatingSell")
    val analystRatingSell: String,
    @SerializedName("AnalystRatingStrongBuy")
    val analystRatingStrongBuy: String,
    @SerializedName("AnalystRatingStrongSell")
    val analystRatingStrongSell: String,
    @SerializedName("AnalystTargetPrice")
    val analystTargetPrice: String,
    @SerializedName("AssetType")
    val assetType: String,
    @SerializedName("Beta")
    val beta: String,
    @SerializedName("BookValue")
    val bookValue: String,
    @SerializedName("CIK")
    val cik: String,
    @SerializedName("Country")
    val country: String,
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("DilutedEPSTTM")
    val dilutedEpsTtm: String,
    @SerializedName("DividendDate")
    val dividendDate: String,
    @SerializedName("DividendPerShare")
    val dividendPerShare: String,
    @SerializedName("DividendYield")
    val dividendYield: String,
    @SerializedName("EBITDA")
    val ebitda: String,
    @SerializedName("EPS")
    val eps: String,
    @SerializedName("EVToEBITDA")
    val evToEbitda: String,
    @SerializedName("EVToRevenue")
    val evToRevenue: String,
    @SerializedName("ExDividendDate")
    val exDividendDate: String,
    @SerializedName("Exchange")
    val exchange: String,
    @SerializedName("FiscalYearEnd")
    val fiscalYearEnd: String,
    @SerializedName("ForwardPE")
    val forwardPe: String,
    @SerializedName("GrossProfitTTM")
    val grossProfitTtm: String,
    @SerializedName("Industry")
    val industry: String,
    @SerializedName("LatestQuarter")
    val latestQuarter: String,
    @SerializedName("MarketCapitalization")
    val marketCapitalization: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("OperatingMarginTTM")
    val operatingMarginTtm: String,
    @SerializedName("PEGRatio")
    val pegRatio: String,
    @SerializedName("PERatio")
    val peRatio: String,
    @SerializedName("PriceToBookRatio")
    val priceToBookRatio: String,
    @SerializedName("PriceToSalesRatioTTM")
    val priceToSalesRatioTtm: String,
    @SerializedName("ProfitMargin")
    val profitMargin: String,
    @SerializedName("QuarterlyEarningsGrowthYOY")
    val quarterlyEarningsGrowthYoy: String,
    @SerializedName("QuarterlyRevenueGrowthYOY")
    val quarterlyRevenueGrowthYoy: String,
    @SerializedName("ReturnOnAssetsTTM")
    val returnOnAssetsTtm: String,
    @SerializedName("ReturnOnEquityTTM")
    val returnOnEquityTtm: String,
    @SerializedName("RevenuePerShareTTM")
    val revenuePerShareTtm: String,
    @SerializedName("RevenueTTM")
    val revenueTtm: String,
    @SerializedName("Sector")
    val sector: String,
    @SerializedName("SharesOutstanding")
    val sharesOutstanding: String,
    @SerializedName("Symbol")
    val symbol: String,
    @SerializedName("TrailingPE")
    val trailingPe: String
)
