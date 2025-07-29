package dev.abhimanyu.lendingtracker.core.domain.usecase.transaction

import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

data class PersonPendingAmount(
    val personId: Long,
    val personName: String,
    val totalLent: BigDecimal,
    val totalRepaid: BigDecimal,
    val pendingAmount: BigDecimal // totalLent - totalRepaid
)

class GetPendingAmountsByPersonUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<PersonPendingAmount>> {
        return transactionRepository.getAllTransactions().map { transactions ->
            transactions
                .filter { it.personName.isNotBlank() }
                .groupBy { it.personId }
                .mapNotNull { (personId, personTransactions) ->
                    val personName = personTransactions.first().personName
                    
                    val totalLent = personTransactions
                        .filter { it.type == TransactionType.LENT }
                        .sumOf { it.amount }
                    
                    val totalRepaid = personTransactions
                        .filter { it.type == TransactionType.REPAYMENT }
                        .sumOf { it.amount }
                    
                    val pendingAmount = totalLent - totalRepaid
                    
                    // Only return people with pending amounts > 0
                    if (pendingAmount > BigDecimal.ZERO) {
                        PersonPendingAmount(
                            personId = personId,
                            personName = personName,
                            totalLent = totalLent,
                            totalRepaid = totalRepaid,
                            pendingAmount = pendingAmount
                        )
                    } else {
                        null
                    }
                }
                .sortedByDescending { it.pendingAmount } // Show highest pending amounts first
        }
    }
}