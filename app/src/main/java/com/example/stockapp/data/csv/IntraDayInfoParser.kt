package com.example.stockapp.data.csv

import com.example.stockapp.domain.models.overview.IntraDayInfo
import com.example.stockapp.domain.models.overview.IntraDayInfoDto
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntraDayInfoParser{
    suspend fun parse(stream: InputStream): List<IntraDayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            try {
                val list = csvReader
                    .readAll()
                    .drop(1)
                    .mapNotNull { line ->
                        val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                        val close = line.getOrNull(4) ?: return@mapNotNull null
                        val dto = IntraDayInfoDto(timestamp, close.toDouble())
                        dto.toIntraDayInfo()
                    }
                    .sortedBy {
                        it.date.hour
                    }
                    .also {
                        csvReader.close()
                    }
                list
            } catch (e: Exception) {
                e.printStackTrace()
                csvReader.close()
                emptyList()
            }
        }
    }
}