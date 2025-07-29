package dev.abhimanyu.lendingtracker.core.domain.usecase.transaction

import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: dev.abhimanyu.lendingtracker.core.domain.model.Transaction): Result<Unit> {
        return try {
            transactionRepository.deleteTransaction(transaction)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}