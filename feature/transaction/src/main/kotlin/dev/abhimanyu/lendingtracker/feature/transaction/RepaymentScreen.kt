package dev.abhimanyu.lendingtracker.feature.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abhimanyu.lendingtracker.core.design.theme.LendingTrackerTheme
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionStatus
import dev.abhimanyu.lendingtracker.core.domain.usecase.transaction.PersonPendingAmount
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepaymentScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Repayment") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        RepaymentForm(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        )
    }
}

@Composable
fun RepaymentForm(
    viewModel: AddTransactionViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPersonPending by remember { mutableStateOf<PersonPendingAmount?>(null) }
    var repaymentAmount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showPersonDropdown by remember { mutableStateOf(false) }
    
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Get pending amounts by person from real database
    val pendingAmountsByPerson = uiState.pendingAmountsByPerson
    
    // Handle success state
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
            viewModel.clearState()
        }
    }
    
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💰",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Record Repayment",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "Record money received back from a loan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // Select Person with Pending Amount
        Text(
            text = "Select Person to Repay",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        // Person Selection Dropdown
        Box {
            OutlinedTextField(
                value = selectedPersonPending?.let { 
                    "${it.personName} - ₹${it.pendingAmount} pending" 
                } ?: "",
                onValueChange = { },
                label = { Text("Select Person") },
                placeholder = { Text("Choose person with pending amount") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPersonDropdown = true },
                singleLine = true,
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
            
            DropdownMenu(
                expanded = showPersonDropdown,
                onDismissRequest = { showPersonDropdown = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (pendingAmountsByPerson.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No pending amounts found") },
                        onClick = { },
                        enabled = false
                    )
                } else {
                    pendingAmountsByPerson.forEach { personPending ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = personPending.personName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Total Pending: ₹${personPending.pendingAmount}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Lent: ₹${personPending.totalLent} • Repaid: ₹${personPending.totalRepaid}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            },
                            onClick = {
                                selectedPersonPending = personPending
                                showPersonDropdown = false
                            }
                        )
                    }
                }
            }
        }
        
        // Repayment Amount
        OutlinedTextField(
            value = repaymentAmount,
            onValueChange = { repaymentAmount = it },
            label = { Text("Repayment Amount") },
            placeholder = { Text("₹ 0.00") },
            leadingIcon = {
                Text(
                    text = "₹",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            isError = selectedPersonPending?.let { personPending ->
                repaymentAmount.toBigDecimalOrNull()?.let { amount ->
                    amount > personPending.pendingAmount
                } ?: false
            } ?: false,
            supportingText = {
                selectedPersonPending?.let { personPending ->
                    repaymentAmount.toBigDecimalOrNull()?.let { amount ->
                        if (amount > personPending.pendingAmount) {
                            Text(
                                text = "Amount cannot exceed pending amount of ₹${personPending.pendingAmount}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        )
        
        // Notes
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (Optional)") },
            placeholder = { Text("Add any notes about this repayment") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
        )
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            
            Button(
                onClick = {
                    selectedPersonPending?.let { personPending ->
                        println("DEBUG: Adding repayment for person - ID: ${personPending.personId}, Person: ${personPending.personName}, Amount: $repaymentAmount")
                        viewModel.addRepaymentForPerson(
                            personId = personPending.personId,
                            personName = personPending.personName,
                            amount = repaymentAmount,
                            notes = notes
                        )
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selectedPersonPending != null && 
                         repaymentAmount.isNotBlank() && 
                         !uiState.isLoading &&
                         (repaymentAmount.toBigDecimalOrNull()?.let { amount ->
                             selectedPersonPending?.let { personPending -> amount <= personPending.pendingAmount }
                         } ?: false),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                } else {
                    Text("Record Repayment")
                }
            }
        }
        
        // Show person's pending details if selected
        selectedPersonPending?.let { personPending ->
            val repaymentAmountDecimal = repaymentAmount.toBigDecimalOrNull() ?: BigDecimal.ZERO
            val remainingBalance = personPending.pendingAmount - repaymentAmountDecimal
            val isOverpayment = repaymentAmountDecimal > personPending.pendingAmount
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "${personPending.personName}'s Lending Summary",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total Lent: ₹${personPending.totalLent}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Already Repaid: ₹${personPending.totalRepaid}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "Pending Amount: ₹${personPending.pendingAmount}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    
                    // Show calculation if repayment amount is entered
                    if (repaymentAmount.isNotBlank() && repaymentAmountDecimal > BigDecimal.ZERO) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        if (isOverpayment) {
                            Text(
                                text = "⚠️ Overpayment by ₹${repaymentAmountDecimal - personPending.pendingAmount}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(
                                text = "Remaining Balance: ₹$remainingBalance",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (remainingBalance == BigDecimal.ZERO) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.onSurface
                            )
                            if (remainingBalance == BigDecimal.ZERO) {
                                Text(
                                    text = "✅ Loan will be fully repaid",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Error message
        uiState.errorMessage?.let { errorMessage ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepaymentScreenPreview() {
    LendingTrackerTheme {
        RepaymentScreen(onNavigateBack = {})
    }
}