package dev.abhimanyu.lendingtracker.core.domain.model

import java.math.BigDecimal
import java.util.Date

data class Transaction(
    val id: Long = 0,
    val personId: Long,
    val personName: String = "",
    val amount: BigDecimal,
    val type: TransactionType,
    val purpose: String? = null,
    val interestRate: BigDecimal? = null,
    val dueDate: Date? = null,
    val notes: String? = null,
    val status: TransactionStatus = TransactionStatus.PENDING,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class TransactionType {
    LENT,    // Money you lent to someone
    BORROWED // Money you borrowed from someone
}

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    OVERDUE,
    CANCELLED
}