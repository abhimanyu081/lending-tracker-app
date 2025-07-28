package dev.abhimanyu.lendingtracker.core.domain.repository

import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionStatus
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Long): Transaction?
    fun getTransactionsByPerson(personId: Long): Flow<List<Transaction>>
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    fun getTransactionsByStatus(status: TransactionStatus): Flow<List<Transaction>>
    suspend fun getTotalAmountByType(type: TransactionType): BigDecimal
    suspend fun getPendingAmountByType(type: TransactionType): BigDecimal
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun updateTransactionStatus(id: Long, status: TransactionStatus)
}