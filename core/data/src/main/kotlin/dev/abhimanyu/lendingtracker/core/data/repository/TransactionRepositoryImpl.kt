package dev.abhimanyu.lendingtracker.core.data.repository

import dev.abhimanyu.lendingtracker.core.data.database.dao.TransactionDao
import dev.abhimanyu.lendingtracker.core.data.mapper.toDomain
import dev.abhimanyu.lendingtracker.core.data.mapper.toEntity
import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionStatus
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactionsWithPerson().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)?.toDomain()
    }
    
    override fun getTransactionsByPerson(personId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByPerson(personId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> {
        val entityType = when (type) {
            TransactionType.LENT -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.LENT
            TransactionType.BORROWED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.BORROWED
            TransactionType.REPAYMENT -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.REPAYMENT
        }
        return transactionDao.getTransactionsByType(entityType).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getTransactionsByStatus(status: TransactionStatus): Flow<List<Transaction>> {
        val entityStatus = when (status) {
            TransactionStatus.PENDING -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.PENDING
            TransactionStatus.COMPLETED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.COMPLETED
            TransactionStatus.OVERDUE -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.OVERDUE
            TransactionStatus.CANCELLED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.CANCELLED
        }
        return transactionDao.getTransactionsByStatus(entityStatus).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getTotalAmountByType(type: TransactionType): BigDecimal {
        val entityType = when (type) {
            TransactionType.LENT -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.LENT
            TransactionType.BORROWED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.BORROWED
            TransactionType.REPAYMENT -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.REPAYMENT
        }
        return transactionDao.getTotalAmountByType(entityType) ?: BigDecimal.ZERO
    }
    
    override suspend fun getPendingAmountByType(type: TransactionType): BigDecimal {
        val entityType = when (type) {
            TransactionType.LENT -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.LENT
            TransactionType.BORROWED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.BORROWED
            TransactionType.REPAYMENT -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.REPAYMENT
        }
        return transactionDao.getPendingAmountByType(entityType) ?: BigDecimal.ZERO
    }
    
    override suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.toEntity())
    }
    
    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }
    
    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction.toEntity())
    }
    
    override suspend fun updateTransactionStatus(id: Long, status: TransactionStatus) {
        val entityStatus = when (status) {
            TransactionStatus.PENDING -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.PENDING
            TransactionStatus.COMPLETED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.COMPLETED
            TransactionStatus.OVERDUE -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.OVERDUE
            TransactionStatus.CANCELLED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.CANCELLED
        }
        transactionDao.updateTransactionStatus(id, entityStatus)
    }
}