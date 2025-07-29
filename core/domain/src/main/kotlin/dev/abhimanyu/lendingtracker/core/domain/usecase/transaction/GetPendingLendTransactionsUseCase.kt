package dev.abhimanyu.lendingtracker.core.domain.usecase.transaction

import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionStatus
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPendingLendTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return transactionRepository.getAllTransactions()
            .map { transactions ->
                transactions.filter { 
                    it.type == TransactionType.LENT && 
                    it.status == TransactionStatus.PENDING &&
                    it.personName.isNotBlank()
                }
            }
    }
}