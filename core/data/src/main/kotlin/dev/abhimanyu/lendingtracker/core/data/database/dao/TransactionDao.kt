package dev.abhimanyu.lendingtracker.core.data.database.dao

import androidx.room.*
import dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionEntity
import dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus
import dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface TransactionDao {
    
    @Query("""
        SELECT t.*, p.name as personName, p.phone as personPhone 
        FROM transactions t 
        INNER JOIN persons p ON t.personId = p.id 
        ORDER BY t.createdAt DESC
    """)
    fun getAllTransactionsWithPerson(): Flow<List<TransactionWithPerson>>
    
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?
    
    @Query("SELECT * FROM transactions WHERE personId = :personId ORDER BY createdAt DESC")
    fun getTransactionsByPerson(personId: Long): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY createdAt DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE status = :status ORDER BY createdAt DESC")
    fun getTransactionsByStatus(status: TransactionStatus): Flow<List<TransactionEntity>>
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND status != 'CANCELLED'")
    suspend fun getTotalAmountByType(type: TransactionType): BigDecimal?
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND status = 'PENDING'")
    suspend fun getPendingAmountByType(type: TransactionType): BigDecimal?
    
    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity): Long
    
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)
    
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
    
    @Query("UPDATE transactions SET status = :status WHERE id = :id")
    suspend fun updateTransactionStatus(id: Long, status: TransactionStatus)
}

data class TransactionWithPerson(
    val id: Long,
    val personId: Long,
    val amount: BigDecimal,
    val type: TransactionType,
    val purpose: String?,
    val interestRate: BigDecimal?,
    val dueDate: java.util.Date?,
    val notes: String?,
    val status: TransactionStatus,
    val createdAt: java.util.Date,
    val updatedAt: java.util.Date,
    val personName: String,
    val personPhone: String
)