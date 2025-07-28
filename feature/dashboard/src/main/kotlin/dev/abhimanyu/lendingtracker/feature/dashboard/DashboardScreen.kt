package dev.abhimanyu.lendingtracker.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import dev.abhimanyu.lendingtracker.core.design.components.QuickActionButton
import dev.abhimanyu.lendingtracker.core.design.components.SummaryCard
import dev.abhimanyu.lendingtracker.core.design.theme.*
import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onLendMoneyClick: () -> Unit = {},
    onBorrowMoneyClick: () -> Unit = {},
    onAddPersonClick: () -> Unit = {},
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
                            title = "Total Borrowed",
                            amount = "₹${uiState.totalBorrowed}",
                            subtitle = if (uiState.pendingBorrowed > BigDecimal.ZERO) "₹${uiState.pendingBorrowed} pending" else "All repaid",
                            amountColor = MoneyNegative,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            item {
                if (!uiState.isLoading) {
                    val totalPending = uiState.pendingLent + uiState.pendingBorrowed
                    SummaryCard(
                        title = "Pending Collections",
                        amount = "₹${uiState.pendingLent}",
                        subtitle = if (uiState.recentTransactions.isNotEmpty()) "From ${uiState.recentTransactions.distinctBy { it.personName }.size} people" else "No pending collections",
                        amountColor = MoneyPending,
                        modifier = Modifier.fillMaxWidth()
                    )
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        text = "LEND\nMONEY",
                        emoji = "💸",
                        onClick = onLendMoneyClick,
                        modifier = Modifier.weight(1f),
                        isPrimary = true
                    )
                    QuickActionButton(
                        text = "BORROW\nMONEY",
                        emoji = "💰",
                        onClick = onBorrowMoneyClick,
                        modifier = Modifier.weight(1f),
                        isPrimary = false
                    )
                }
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionButton(
                        text = "ADD\nPERSON",
                        emoji = "👤",
                        onClick = onAddPersonClick,
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
            
            // Real recent transactions
            if (uiState.recentTransactions.isEmpty() && !uiState.isLoading) {
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
                                text = "No transactions yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Start by adding a person and creating your first transaction",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            } else {
                items(uiState.recentTransactions) { transaction ->
                    RealTransactionItem(transaction = transaction)
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
    modifier: Modifier = Modifier
) {
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
                    text = "${transaction.type.name.lowercase().replaceFirstChar { it.uppercase() }} • ${formatTimeAgo(transaction.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${transaction.amount}",
                    style = MoneyTextStyle,
                    color = if (transaction.type == TransactionType.LENT) MoneyPositive else MoneyNegative
                )
                Text(
                    text = transaction.status.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = when (transaction.status.name) {
                        "PENDING" -> MoneyPending
                        "COMPLETED" -> Success
                        "OVERDUE" -> Error
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    }
                )
            }
        }
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

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    LendingTrackerTheme {
        DashboardScreen()
    }
}