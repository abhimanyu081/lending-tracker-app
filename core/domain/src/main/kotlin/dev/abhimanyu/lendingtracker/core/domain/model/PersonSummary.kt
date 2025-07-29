package dev.abhimanyu.lendingtracker.core.domain.model

import java.math.BigDecimal

data class PersonSummary(
    val personId: Long,
    val personName: String,
    val totalLent: BigDecimal,
    val totalRepaid: BigDecimal,
    val netBalance: BigDecimal, // totalLent - totalRepaid
    val lastTransactionDate: java.util.Date,
    val transactionCount: Int
)