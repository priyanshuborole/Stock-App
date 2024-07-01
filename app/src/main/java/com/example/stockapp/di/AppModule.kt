package com.example.stockapp.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.stockapp.common.Constants.Companion.BASE_URL
import com.example.stockapp.data.csv.IntraDayInfoParser
import com.example.stockapp.data.remote.StockApiDataSource
import com.example.stockapp.data.repository.DataStoreRepositoryImpl
import com.example.stockapp.data.repository.StockApiRepositoryImpl
import com.example.stockapp.domain.repository.DataStoreRepository
import com.example.stockapp.domain.usecase.GetTopGainersLosersAndMostActivelyTradedUseCase
import com.example.stockapp.domain.repository.StockApiRepository
import com.example.stockapp.domain.usecase.GetCompanyOverviewUseCase
import com.example.stockapp.domain.usecase.GetIntraDayInfoUseCase
import com.example.stockapp.domain.usecase.GetTickerSearchMatchesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024
        val httpCacheDirectory = File(context.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, @ApplicationContext context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log the body of the request and response
        }
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context)) {
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build()
                } else {
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24).build()
                }
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideStockApi(okHttpClient: OkHttpClient): StockApiDataSource {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StockApiDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideIntraDayInfoParser(): IntraDayInfoParser {
        return IntraDayInfoParser()
    }

    @Provides
    @Singleton
    fun provideStockApiRepository(stockApi: StockApiDataSource, intraDayInfoParser: IntraDayInfoParser): StockApiRepository {
        return StockApiRepositoryImpl(stockApi,intraDayInfoParser)
    }

    @Provides
    @Singleton
    fun provideGetTopGainersLosersAndMostActivelyTradedUseCase(stockApiRepository: StockApiRepository): GetTopGainersLosersAndMostActivelyTradedUseCase {
        return GetTopGainersLosersAndMostActivelyTradedUseCase(stockApiRepository)
    }

    @Provides
    @Singleton
    fun provideGetCompanyOverviewUseCase(stockApiRepository: StockApiRepository): GetCompanyOverviewUseCase {
        return GetCompanyOverviewUseCase(stockApiRepository)
    }

    @Provides
    @Singleton
    fun provideGetTickerSearchMatchesUseCase(stockApiRepository: StockApiRepository): GetTickerSearchMatchesUseCase {
        return GetTickerSearchMatchesUseCase(stockApiRepository)
    }

    @Provides
    @Singleton
    fun provideGetIntraDayInfoUseCase(stockApiRepository: StockApiRepository): GetIntraDayInfoUseCase {
        return GetIntraDayInfoUseCase(stockApiRepository)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepositoryImpl(context)
    }



    private fun hasNetwork(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
        return result
    }
}