package dev.abhimanyu.lendingtracker.feature.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import dev.abhimanyu.lendingtracker.core.domain.usecase.transaction.DeleteTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonDetailUiState())
    val uiState: StateFlow<PersonDetailUiState> = _uiState.asStateFlow()

    fun loadPersonTransactions(personId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                transactionRepository.getAllTransactions().collect { allTransactions ->
                    val transactions = allTransactions.filter { it.personId == personId }
                    val sortedTransactions = transactions.sortedByDescending { it.createdAt }
                    
                    val totalLent = transactions
                        .filter { it.type == TransactionType.LENT }
                        .sumOf { it.amount }
                    
                    val totalRepaid = transactions
                        .filter { it.type == TransactionType.REPAYMENT }
                        .sumOf { it.amount }
                    
                    val netBalance = totalLent - totalRepaid
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        transactions = sortedTransactions,
                        totalLent = totalLent,
                        totalRepaid = totalRepaid,
                        netBalance = netBalance,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load transactions"
                )
            }
        }
    }
    
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(transaction)
                // Transactions will automatically refresh through the flow
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete transaction: ${e.message}"
                )
            }
        }
    }
}

data class PersonDetailUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val totalLent: BigDecimal = BigDecimal.ZERO,
    val totalRepaid: BigDecimal = BigDecimal.ZERO,
    val netBalance: BigDecimal = BigDecimal.ZERO,
    val errorMessage: String? = null
)