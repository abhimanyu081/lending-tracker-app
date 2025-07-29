package dev.abhimanyu.lendingtracker.core.domain.usecase.transaction

import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        personId: Long,
        amount: String,
        type: TransactionType,
        purpose: String? = null,
        interestRate: String? = null,
        dueDate: String? = null,
        notes: String? = null,
        parentTransactionId: Long? = null
    ): Result<Long> {
        return try {
            if (personId <= 0) {
                return Result.failure(IllegalArgumentException("Invalid person selected"))
            }
            
            val amountDecimal = try {
                BigDecimal(amount.trim())
            } catch (e: NumberFormatException) {
                return Result.failure(IllegalArgumentException("Invalid amount format"))
            }
            
            if (amountDecimal <= BigDecimal.ZERO) {
                return Result.failure(IllegalArgumentException("Amount must be greater than zero"))
            }
            
            val interestRateDecimal = interestRate?.trim()?.takeIf { it.isNotBlank() }?.let {
                try {
                    BigDecimal(it)
                } catch (e: NumberFormatException) {
                    return Result.failure(IllegalArgumentException("Invalid interest rate format"))
                }
            }
            
            val dueDateParsed = dueDate?.trim()?.takeIf { it.isNotBlank() }?.let {
                try {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it)
                } catch (e: Exception) {
                    return Result.failure(IllegalArgumentException("Invalid date format. Use DD/MM/YYYY"))
                }
            }
            
            val transaction = Transaction(
                personId = personId,
                amount = amountDecimal,
                type = type,
                purpose = purpose?.trim()?.takeIf { it.isNotBlank() },
                interestRate = interestRateDecimal,
                dueDate = dueDateParsed,
                notes = notes?.trim()?.takeIf { it.isNotBlank() },
                parentTransactionId = parentTransactionId,
                createdAt = Date(),
                updatedAt = Date()
            )
            
            val transactionId = transactionRepository.insertTransaction(transaction)
            Result.success(transactionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}