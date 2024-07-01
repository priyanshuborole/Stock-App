package com.example.stockapp.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.stockapp.R
import com.example.stockapp.databinding.FragmentExploreBinding
import com.example.stockapp.domain.models.stocks.TickerMarketType
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var binding: FragmentExploreBinding
    private val viewModel: ExploreStocksViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTopGainersLosersAndMostActivelyTraded()
        adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> TickerMarketType.TOP_GAINERS.displayName
                1 -> TickerMarketType.TOP_LOSERS.displayName
                2 -> TickerMarketType.MOST_ACTIVELY_TRADED.displayName
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()

        binding.tvSearch.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.searchBar to "ticker_search")
            findNavController().navigate(R.id.action_exploreFragment_to_tickerSearchFragment, null, null, extras)
        }
    }
}