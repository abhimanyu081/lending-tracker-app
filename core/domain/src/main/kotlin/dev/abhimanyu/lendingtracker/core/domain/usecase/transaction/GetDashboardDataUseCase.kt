package dev.abhimanyu.lendingtracker.core.domain.usecase.transaction

import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionStatus
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

data class DashboardData(
    val totalLent: BigDecimal,
    val totalBorrowed: BigDecimal,
    val pendingLent: BigDecimal,
    val pendingBorrowed: BigDecimal,
    val recentTransactions: List<Transaction>
)

class GetDashboardDataUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<DashboardData> {
        return transactionRepository.getAllTransactions().map { transactions ->
            val totalLent = transactions
                .filter { it.type == TransactionType.LENT }
                .fold(BigDecimal.ZERO) { acc, transaction -> acc + transaction.amount }
            
            val totalBorrowed = transactions
                .filter { it.type == TransactionType.BORROWED }
                .fold(BigDecimal.ZERO) { acc, transaction -> acc + transaction.amount }
            
            val pendingLent = transactions
                .filter { it.type == TransactionType.LENT && it.status == TransactionStatus.PENDING }
                .fold(BigDecimal.ZERO) { acc, transaction -> acc + transaction.amount }
            
            val pendingBorrowed = transactions
                .filter { it.type == TransactionType.BORROWED && it.status == TransactionStatus.PENDING }
                .fold(BigDecimal.ZERO) { acc, transaction -> acc + transaction.amount }
            
            val recentTransactions = transactions
                .sortedByDescending { it.createdAt }
                .take(5)
            
            DashboardData(
                totalLent = totalLent,
                totalBorrowed = totalBorrowed,
                pendingLent = pendingLent,
                pendingBorrowed = pendingBorrowed,
                recentTransactions = recentTransactions
            )
        }
    }
}