package dev.abhimanyu.lendingtracker.feature.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhimanyu.lendingtracker.core.domain.model.Person
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.repository.PersonRepository
import dev.abhimanyu.lendingtracker.core.domain.usecase.person.GetAllPersonsUseCase
import dev.abhimanyu.lendingtracker.core.domain.usecase.person.AddPersonUseCase
import dev.abhimanyu.lendingtracker.core.domain.usecase.transaction.AddTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTransactionUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val persons: List<Person> = emptyList(),
    val selectedPerson: Person? = null
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getAllPersonsUseCase: GetAllPersonsUseCase,
    private val addPersonUseCase: AddPersonUseCase,
    private val personRepository: PersonRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()
    
    init {
        loadPersons()
    }
    
    private fun loadPersons() {
        viewModelScope.launch {
            getAllPersonsUseCase().collect { persons ->
                _uiState.value = _uiState.value.copy(persons = persons)
            }
        }
    }
    
    fun addTransaction(
        personName: String,
        amount: String,
        type: TransactionType,
        purpose: String? = null,
        interestRate: String? = null,
        dueDate: String? = null,
        notes: String? = null,
        personPhone: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Find person or create if doesn't exist
            val person = _uiState.value.persons.find { it.name.equals(personName, ignoreCase = true) }
            val personId = person?.id ?: run {
                // Auto-create person from contact info
                try {
                    val newPersonResult = addPersonUseCase(
                        name = personName,
                        phone = personPhone ?: "",
                        email = "",
                        address = "",
                        notes = "Added from contact picker"
                    )
                    newPersonResult.getOrThrow()
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to create person: ${e.message}"
                    )
                    return@launch
                }
            }
            
            addTransactionUseCase(
                personId = personId,
                amount = amount,
                type = type,
                purpose = purpose,
                interestRate = interestRate,
                dueDate = dueDate,
                notes = notes
            ).fold(
                onSuccess = { transactionId ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to add transaction"
                    )
                }
            )
        }
    }
    
    fun clearState() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isSuccess = false,
            errorMessage = null
        )
    }
}