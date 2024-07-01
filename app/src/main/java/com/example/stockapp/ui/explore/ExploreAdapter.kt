package com.example.stockapp.ui.explore

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.R
import com.example.stockapp.databinding.ItemTickerBinding
import com.example.stockapp.domain.models.stocks.MostActivelyTraded
import com.example.stockapp.domain.models.stocks.TopGainer
import com.example.stockapp.domain.models.stocks.TopLoser

class ExploreAdapter(
    private var items: List<Any>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MOST_ACTIVELY_TRADED = 1
        private const val TYPE_TOP_GAINERS = 2
        private const val TYPE_TOP_LOSERS = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOST_ACTIVELY_TRADED -> {
                val binding = ItemTickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MostActivelyTradedViewHolder(binding)
            }
            TYPE_TOP_GAINERS -> {
                val binding = ItemTickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TopGainerViewHolder(binding)
            }
            TYPE_TOP_LOSERS -> {
                val binding = ItemTickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TopLoserViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_MOST_ACTIVELY_TRADED -> {
                val item = items[position] as MostActivelyTraded
                (holder as MostActivelyTradedViewHolder).bind(item)
            }
            TYPE_TOP_GAINERS -> {
                val item = items[position] as TopGainer
                (holder as TopGainerViewHolder).bind(item)
            }
            TYPE_TOP_LOSERS -> {
                val item = items[position] as TopLoser
                (holder as TopLoserViewHolder).bind(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is MostActivelyTraded -> TYPE_MOST_ACTIVELY_TRADED
            is TopGainer -> TYPE_TOP_GAINERS
            is TopLoser -> TYPE_TOP_LOSERS
            else -> throw IllegalArgumentException("Invalid type of data at position $position")
        }
    }

    inner class MostActivelyTradedViewHolder(val binding: ItemTickerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MostActivelyTraded) {
            binding.tvTickerName.text = item.ticker
            val price = "$ ${item.price}"
            binding.tvPrice.text = price
            binding.tvChangePercentage.apply {
                if (item.changePercentage.startsWith("-")) {
                    setTextColor(Color.RED)
                } else {
                    setTextColor(context.getColor(R.color.green))
                }
                text = item.changePercentage
            }
            binding.root.setOnClickListener {
                onItemClicked(item.ticker)
            }
        }
    }

    inner class TopGainerViewHolder(val binding: ItemTickerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TopGainer) {
            binding.tvTickerName.text = item.ticker
            val price = "$ ${item.price}"
            binding.tvPrice.text = price
            binding.tvChangePercentage.apply {
                text = item.changePercentage
                setTextColor(context.getColor(R.color.green))
            }
            binding.root.setOnClickListener {
                onItemClicked(item.ticker)
            }
        }
    }

    inner class TopLoserViewHolder(val binding: ItemTickerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TopLoser) {
            binding.tvTickerName.text = item.ticker
            val price = "$ ${item.price}"
            binding.tvPrice.text = price
            binding.tvChangePercentage.apply {
                text = item.changePercentage
                setTextColor(Color.RED)
            }
            binding.root.setOnClickListener {
                onItemClicked(item.ticker)
            }
        }
    }
}
