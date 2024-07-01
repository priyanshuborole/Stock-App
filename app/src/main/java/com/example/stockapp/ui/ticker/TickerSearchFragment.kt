package com.example.stockapp.ui.ticker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.stockapp.R
import com.example.stockapp.common.ApiState
import com.example.stockapp.databinding.FragmentTickerSearchBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TickerSearchFragment : Fragment() {

    private lateinit var binding: FragmentTickerSearchBinding
    private val viewModel: TickerSearchViewModel by viewModels()
    private lateinit var adapter: TickerSearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTickerSearchBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
        showSoftKeyboard(binding.etSearch)
        setupRecyclerView()

        binding.etSearch.doAfterTextChanged { text ->
            viewModel.setSearchQuery(text.toString())
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()){
                val chip = group.findViewById<Chip>(checkedIds[0])
                val chipText = chip.text.toString()
                viewModel.setSelectedChipType(chipText)
            }
        }


        observeSearchResult()

    }

    private fun observeSearchResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect { state ->
                    when (state) {
                        is ApiState.Loading -> {
                            if (state.showLoading){
                                binding.progressBar.visibility = View.VISIBLE
                            }
                        }

                        is ApiState.Success -> {
                            val bestMatches = state.data?.bestMatches
                            if (bestMatches.isNullOrEmpty()) {
                                binding.tvError.text = getString(R.string.no_results_found)
                            }
                            else {
                                binding.tvError.text = getString(R.string.Empty)
                            }
                            adapter.submitList(bestMatches)
                            binding.progressBar.visibility = View.GONE
                        }

                        is ApiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.text = state.message
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = TickerSearchAdapter()
        binding.rvTickers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTickers.adapter = adapter
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}