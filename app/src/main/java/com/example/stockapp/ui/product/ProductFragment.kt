package com.example.stockapp.ui.product

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.stockapp.common.ApiState
import com.example.stockapp.databinding.FragmentProductBinding
import com.example.stockapp.domain.models.overview.CompanyOverview
import com.example.stockapp.domain.models.overview.IntraDayInfo
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var binding: FragmentProductBinding
    private val args: ProductFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCompanyOverview(args.symbol)
        viewModel.getIntraDayInfo(args.symbol)
        observeViewModel()
        observeIntraDayInfo()
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.companyOverview.collect { apiState ->
                    when (apiState) {
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is ApiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            updateUI(apiState.data)
                        }

                        is ApiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), apiState.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun observeIntraDayInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.intraDayInfo.collect { apiState ->
                    when (apiState) {
                        is ApiState.Loading -> {}

                        is ApiState.Success -> {
                            apiState.data?.let { setupLineChart(it) }
                        }

                        is ApiState.Error -> {}
                    }
                }
            }
        }
    }

    private fun updateUI(data: CompanyOverview?) {
        binding.apply {
            tvCompanyName.text = data?.symbol.orEmpty()
            tvStockInfo.text = data?.assetType.orEmpty()
            val price = "$ ${data?.peRatio.orEmpty()}"
            tvStockPrice.text = price
            stockChange.text = data?.profitMargin.orEmpty()
            val title = " About ${data?.symbol}"
            tvAboutTitle.text = title
            tvAboutDescription.text = data?.description.orEmpty()
            tvIndustry.text = data?.industry.orEmpty()
            tvSector.text = data?.sector.orEmpty()
            val bookValue = "Book Value - ${data?.bookValue.orEmpty()}"
            tvBookValue.text = bookValue
            val dividendYield = "Dividend Yield - ${data?.dividendYield.orEmpty()}"
            tvDividendYield.text = dividendYield
            val fiveTwoWeekHigh = "52 Week High - ${data?.week52High.orEmpty()}"
            tv52WeekHigh.text = fiveTwoWeekHigh
            val fiveTwoWeekLow = "52 Week Low - ${data?.week52Low.orEmpty()}"
            tv52WeekLow.text = fiveTwoWeekLow
            val fiftyDayMovingAverage = "50 Day Moving Average - ${data?.day50MovingAverage.orEmpty()}"
            tv50DayMovingAverage.text = fiftyDayMovingAverage
            val twoHundredDayMovingAverage = "200 Day Moving Average - ${data?.day200MovingAverage.orEmpty()}"
            tv200DayMovingAverage.text = twoHundredDayMovingAverage
        }
    }

    private fun setupLineChart(intraDayInfoList : List<IntraDayInfo>) {
        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()

        intraDayInfoList.forEachIndexed { index, data ->
            entries.add(Entry(index.toFloat(), data.close.toFloat()))
            labels.add(data.date.toString().substring(11))
        }

        val dataSet = LineDataSet(entries, "Stock Price")

        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.RED

        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData
        binding.lineChart.description.text = "Stock Prices"
        binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.lineChart.invalidate()

    }
}