package com.example.stockapp.ui.ticker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.databinding.ItemTickerSearchBinding
import com.example.stockapp.domain.models.ticker_search.BestMatcher

class TickerSearchAdapter : ListAdapter<BestMatcher, TickerSearchAdapter.TickerViewHolder>(TickerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerViewHolder {
        val binding = ItemTickerSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TickerViewHolder, position: Int) {
        val ticker = getItem(position)
        holder.bind(ticker)
    }

    class TickerViewHolder(private val binding: ItemTickerSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ticker: BestMatcher) {
            binding.tvSymbol.text = ticker.symbol
            binding.tvName.text = ticker.name
            binding.tvRegion.text = ticker.region
            binding.tvType.text = ticker.type
        }
    }

    class TickerDiffCallback : DiffUtil.ItemCallback<BestMatcher>() {
        override fun areItemsTheSame(oldItem: BestMatcher, newItem: BestMatcher): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: BestMatcher, newItem: BestMatcher): Boolean {
            return oldItem == newItem
        }
    }
}