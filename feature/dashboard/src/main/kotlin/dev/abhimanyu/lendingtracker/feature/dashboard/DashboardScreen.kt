package dev.abhimanyu.lendingtracker.feature.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abhimanyu.lendingtracker.core.design.components.QuickActionButton
import dev.abhimanyu.lendingtracker.core.design.components.SummaryCard
import dev.abhimanyu.lendingtracker.core.design.theme.*
import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.model.PersonSummary
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onLendMoneyClick: () -> Unit = {},
    onRecordRepaymentClick: () -> Unit = {},
    onPersonClick: (Long, String) -> Unit = { _, _ -> },
    onViewHistoryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lending Tracker",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Summary Cards Section
            item {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            item {
                if (uiState.isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryCard(
                            title = "Total Lent",
                            amount = "₹${uiState.totalLent}",
                            subtitle = if (uiState.pendingLent > BigDecimal.ZERO) "₹${uiState.pendingLent} pending" else "All collected",
                            amountColor = MoneyPositive,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Pending Collections",
                            amount = "₹${uiState.pendingLent}",
                            subtitle = if (uiState.recentTransactions.isNotEmpty()) "From ${uiState.recentTransactions.distinctBy { it.personName }.size} people" else "No pending",
                            amountColor = MoneyPending,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Quick Actions Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            item {
                QuickActionButton(
                    text = "LEND\nMONEY",
                    emoji = "💸",
                    onClick = onLendMoneyClick,
                    modifier = Modifier.fillMaxWidth(),
                    isPrimary = true
                )
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        text = "RECORD\nREPAYMENT",
                        emoji = "💰",
                        onClick = onRecordRepaymentClick,
                        modifier = Modifier.weight(1f),
                        isPrimary = false
                    )
                    QuickActionButton(
                        text = "VIEW\nHISTORY",
                        emoji = "📋",
                        onClick = onViewHistoryClick,
                        modifier = Modifier.weight(1f),
                        isPrimary = false
                    )
                }
            }
            
            // Recent Transactions Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            // Person summaries
            if (uiState.personSummaries.isEmpty() && !uiState.isLoading) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📝",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No lending history yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Start by lending money to someone",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            } else {
                items(uiState.personSummaries) { personSummary ->
                    PersonSummaryItem(
                        personSummary = personSummary,
                        onClick = { 
                            onPersonClick(personSummary.personId, personSummary.personName)
                        }
                    )
                }
            }
            
            // Error message
            uiState.errorMessage?.let { errorMessage ->
                item {
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
    }
}

@Composable
fun RealTransactionItem(
    transaction: Transaction,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👤",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = transaction.personName.ifBlank { "Unknown Person" },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${getTransactionTypeDisplay(transaction.type)} • ${formatTimeAgo(transaction.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                if (transaction.purpose?.isNotBlank() == true) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Purpose: ${transaction.purpose}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "₹${transaction.amount}",
                        style = MoneyTextStyle,
                        color = when (transaction.type) {
                            TransactionType.LENT -> MoneyPositive
                            TransactionType.REPAYMENT -> Success
                            else -> MoneyNegative
                        }
                    )
                    // Show transaction type instead of status for better clarity
                    Text(
                        text = when (transaction.type) {
                            TransactionType.LENT -> "Loan"
                            TransactionType.REPAYMENT -> "Received"
                            TransactionType.BORROWED -> "Borrowed"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = when (transaction.type) {
                            TransactionType.LENT -> MoneyPending
                            TransactionType.REPAYMENT -> Success
                            TransactionType.BORROWED -> MoneyNegative
                        }
                    )
                }
                
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete transaction",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Helper function to format time ago
fun formatTimeAgo(date: Date): String {
    val now = Date()
    val diffInMillis = now.time - date.time
    val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
    
    return when {
        diffInDays == 0L -> "Today"
        diffInDays == 1L -> "Yesterday"
        diffInDays < 7 -> "$diffInDays days ago"
        diffInDays < 30 -> "${diffInDays / 7} weeks ago"
        diffInDays < 365 -> "${diffInDays / 30} months ago"
        else -> "${diffInDays / 365} years ago"
    }
}

// Helper function to display transaction types
fun getTransactionTypeDisplay(type: TransactionType): String {
    val display = when (type) {
        TransactionType.LENT -> "Lent"
        TransactionType.REPAYMENT -> "Repayment received"
        TransactionType.BORROWED -> "Borrowed"
    }
    println("DEBUG: getTransactionTypeDisplay - Type: $type -> Display: $display")
    return display
}

@Composable
fun PersonSummaryItem(
    personSummary: PersonSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👤",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = personSummary.personName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${personSummary.transactionCount} transactions • ${formatTimeAgo(personSummary.lastTransactionDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                if (personSummary.totalRepaid > BigDecimal.ZERO) {
                    Text(
                        text = "Repaid: ₹${personSummary.totalRepaid}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Success
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${personSummary.netBalance}",
                    style = MoneyTextStyle,
                    color = if (personSummary.netBalance > BigDecimal.ZERO) MoneyPending else Success
                )
                Text(
                    text = if (personSummary.netBalance > BigDecimal.ZERO) "Outstanding" else "Fully Repaid",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (personSummary.netBalance > BigDecimal.ZERO) MoneyPending else Success
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    LendingTrackerTheme {
        DashboardScreen()
    }
}