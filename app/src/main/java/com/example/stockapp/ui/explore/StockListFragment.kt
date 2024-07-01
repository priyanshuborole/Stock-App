package com.example.stockapp.ui.explore

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stockapp.R
import com.example.stockapp.common.ApiState
import com.example.stockapp.databinding.FragmentStockListBinding
import com.example.stockapp.domain.models.stocks.MostActivelyTraded
import com.example.stockapp.domain.models.stocks.TickerMarketType
import com.example.stockapp.domain.models.stocks.TopGainer
import com.example.stockapp.domain.models.stocks.TopLoser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StockListFragment : Fragment() {

    private val viewModel: ExploreStocksViewModel by activityViewModels()
    private lateinit var binding: FragmentStockListBinding
    private lateinit var adapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStockListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tickerMarketType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("tickerMarketType", TickerMarketType::class.java)
        } else {
            arguments?.getSerializable("tickerMarketType") as? TickerMarketType
        }
        tickerMarketType?.let {
            observeViewModel(it)
        }
    }

    private fun observeViewModel(tickerMarketType: TickerMarketType) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                when (tickerMarketType) {
                    TickerMarketType.TOP_GAINERS -> {
                        viewModel.topGainers.collect { state ->
                            handleState(state, ::updateUI)
                        }
                    }

                    TickerMarketType.TOP_LOSERS -> {
                        viewModel.topLosers.collect { state ->
                            handleState(state, ::updateUI)
                        }
                    }

                    TickerMarketType.MOST_ACTIVELY_TRADED -> {
                        viewModel.mostActivelyTraded.collect { state ->
                            handleState(state, ::updateUI)
                        }
                    }
                }
            }
        }
    }

    private fun <T> handleState(state: ApiState<T>, updateUI: (T) -> Unit) {
        when (state) {
            is ApiState.Success -> {
                state.data?.let { updateUI(it) }
            }

            is ApiState.Error -> {
                showError(state.message)
            }

            is ApiState.Loading -> {
                showLoading()
            }
        }
    }

    private fun updateUI(data: List<Any>) {
        if (data.isEmpty()) {
            binding.tvError.text = getString(R.string.no_results_found)
        }
        else {
            binding.tvError.text = getString(R.string.Empty)
        }
        binding.progressBar.visibility = View.GONE
        setupRecyclerView(data)
    }

    private fun showError(message: String?) {
        Toast.makeText(context, message ?: "Unknown error", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(items: List<Any>) {
        adapter = ExploreAdapter(items) { symbol ->
            val action = ExploreFragmentDirections.actionExploreFragmentToProductFragment(symbol)
            findNavController().navigate(action)
        }
        binding.rvTickers.adapter = adapter
        binding.rvTickers.layoutManager = GridLayoutManager(context, 2)
    }

    companion object {
        fun newInstance(tickerMarketType: TickerMarketType): StockListFragment {
            return StockListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("tickerMarketType", tickerMarketType)
                }
            }
        }
    }
}