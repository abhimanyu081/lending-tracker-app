package dev.abhimanyu.lendingtracker.core.domain.usecase.transaction

import dev.abhimanyu.lendingtracker.core.domain.model.PersonSummary
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

class GetPersonSummariesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<PersonSummary>> {
        return transactionRepository.getAllTransactions().map { transactions ->
            transactions
                .filter { it.personName.isNotBlank() }
                .groupBy { it.personId }
                .map { (personId, personTransactions) ->
                    val personName = personTransactions.first().personName
                    
                    val totalLent = personTransactions
                        .filter { it.type == TransactionType.LENT }
                        .sumOf { it.amount }
                    
                    val totalRepaid = personTransactions
                        .filter { it.type == TransactionType.REPAYMENT }
                        .sumOf { it.amount }
                    
                    val netBalance = totalLent - totalRepaid
                    val lastTransactionDate = personTransactions.maxOf { it.createdAt }
                    
                    PersonSummary(
                        personId = personId,
                        personName = personName,
                        totalLent = totalLent,
                        totalRepaid = totalRepaid,
                        netBalance = netBalance,
                        lastTransactionDate = lastTransactionDate,
                        transactionCount = personTransactions.size
                    )
                }
                .filter { it.totalLent > BigDecimal.ZERO } // Only show people you've lent money to
                .sortedByDescending { it.lastTransactionDate }
        }
    }
}