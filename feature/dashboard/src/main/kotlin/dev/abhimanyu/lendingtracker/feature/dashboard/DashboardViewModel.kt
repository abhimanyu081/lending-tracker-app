package dev.abhimanyu.lendingtracker.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.usecase.transaction.GetDashboardDataUseCase
import dev.abhimanyu.lendingtracker.core.domain.usecase.transaction.DashboardData
import dev.abhimanyu.lendingtracker.core.domain.usecase.transaction.DeleteTransactionUseCase
import dev.abhimanyu.lendingtracker.core.domain.usecase.transaction.GetPersonSummariesUseCase
import dev.abhimanyu.lendingtracker.core.domain.model.PersonSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val totalLent: BigDecimal = BigDecimal.ZERO,
    val totalBorrowed: BigDecimal = BigDecimal.ZERO,
    val pendingLent: BigDecimal = BigDecimal.ZERO,
    val pendingBorrowed: BigDecimal = BigDecimal.ZERO,
    val recentTransactions: List<Transaction> = emptyList(),
    val personSummaries: List<PersonSummary> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getPersonSummariesUseCase: GetPersonSummariesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    init {
        loadDashboardData()
    }
    
    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                getDashboardDataUseCase().collect { dashboardData ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        totalLent = dashboardData.totalLent,
                        totalBorrowed = dashboardData.totalBorrowed,
                        pendingLent = dashboardData.pendingLent,
                        pendingBorrowed = dashboardData.pendingBorrowed,
                        recentTransactions = dashboardData.recentTransactions,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load dashboard data"
                )
            }
        }
        
        viewModelScope.launch {
            getPersonSummariesUseCase().collect { personSummaries ->
                _uiState.value = _uiState.value.copy(
                    personSummaries = personSummaries
                )
            }
        }
    }
    
    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadDashboardData()
    }
    
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(transaction)
                // Data will automatically refresh through the flow
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete transaction: ${e.message}"
                )
            }
        }
    }
}