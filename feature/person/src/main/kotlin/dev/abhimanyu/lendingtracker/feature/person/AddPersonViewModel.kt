package dev.abhimanyu.lendingtracker.feature.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhimanyu.lendingtracker.core.domain.usecase.person.AddPersonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddPersonUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddPersonViewModel @Inject constructor(
    private val addPersonUseCase: AddPersonUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddPersonUiState())
    val uiState: StateFlow<AddPersonUiState> = _uiState.asStateFlow()
    
    fun addPerson(
        name: String,
        phone: String,
        email: String? = null,
        address: String? = null,
        notes: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            addPersonUseCase(
                name = name,
                phone = phone,
                email = email,
                address = address,
                notes = notes
            ).fold(
                onSuccess = { personId ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to add person"
                    )
                }
            )
        }
    }
    
    fun clearState() {
        _uiState.value = AddPersonUiState()
    }
}