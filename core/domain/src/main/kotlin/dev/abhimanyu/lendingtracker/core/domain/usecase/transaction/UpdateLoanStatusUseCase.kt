package dev.abhimanyu.lendingtracker.core.domain.usecase.transaction

import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionStatus
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import javax.inject.Inject

class UpdateLoanStatusUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(lendTransactionId: Long): Result<Unit> {
        return try {
            // Get the original LEND transaction
            val lendTransaction = transactionRepository.getTransactionById(lendTransactionId)
                ?: return Result.failure(Exception("Loan transaction not found"))
            
            // Get all REPAYMENT transactions for this loan
            val allTransactions = transactionRepository.getAllTransactions().first()
            val repayments = allTransactions.filter { 
                it.type == TransactionType.REPAYMENT && it.parentTransactionId == lendTransactionId 
            }
            
            // Calculate total repaid amount
            val totalRepaid = repayments.sumOf { it.amount }
            val loanAmount = lendTransaction.amount
            
            // Determine new status
            val newStatus = when {
                totalRepaid >= loanAmount -> TransactionStatus.COMPLETED
                totalRepaid > BigDecimal.ZERO -> TransactionStatus.PENDING // Partially repaid but still pending
                else -> TransactionStatus.PENDING
            }
            
            // Update the loan status if it has changed
            if (lendTransaction.status != newStatus) {
                val updatedTransaction = lendTransaction.copy(
                    status = newStatus,
                    updatedAt = java.util.Date()
                )
                transactionRepository.updateTransaction(updatedTransaction)
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}