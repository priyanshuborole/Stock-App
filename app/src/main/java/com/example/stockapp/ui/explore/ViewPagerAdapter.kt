package com.example.stockapp.ui.explore

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.stockapp.domain.models.stocks.TickerMarketType

class ViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return StockListFragment.newInstance(TickerMarketType.entries[position])
    }
}