package com.example.stockapp.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.common.ApiState
import com.example.stockapp.domain.models.overview.CompanyOverview
import com.example.stockapp.domain.models.overview.IntraDayInfo
import com.example.stockapp.domain.usecase.GetCompanyOverviewUseCase
import com.example.stockapp.domain.usecase.GetIntraDayInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getCompanyOverviewUseCase: GetCompanyOverviewUseCase,
    private val getIntraDayInfoUseCase: GetIntraDayInfoUseCase
) : ViewModel() {

    private val _companyOverview = MutableStateFlow<ApiState<CompanyOverview>>(ApiState.Loading())
    val companyOverview: StateFlow<ApiState<CompanyOverview>> = _companyOverview

    private val _intraDayInfo = MutableStateFlow<ApiState<List<IntraDayInfo>>>(ApiState.Loading())
    val intraDayInfo: StateFlow<ApiState<List<IntraDayInfo>>> = _intraDayInfo

    fun getCompanyOverview(symbol: String) {
        viewModelScope.launch {
            getCompanyOverviewUseCase(symbol).collect { apiState ->
                _companyOverview.update { apiState }
            }
        }
    }

    fun getIntraDayInfo(symbol: String) {
        viewModelScope.launch {
            getIntraDayInfoUseCase(symbol).collect { apiState ->
                _intraDayInfo.update { apiState }
            }
        }
    }
}