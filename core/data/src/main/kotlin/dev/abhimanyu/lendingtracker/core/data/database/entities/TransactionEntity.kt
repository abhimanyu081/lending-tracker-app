package dev.abhimanyu.lendingtracker.core.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["personId"]),
        Index(value = ["parentTransactionId"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val personId: Long,
    val amount: BigDecimal,
    val type: TransactionType,
    val parentTransactionId: Long? = null,
    val purpose: String? = null,
    val interestRate: BigDecimal? = null,
    val dueDate: Date? = null,
    val notes: String? = null,
    val status: TransactionStatus = TransactionStatus.PENDING,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class TransactionType {
    LENT,     // Money you lent to someone
    BORROWED, // Money you borrowed from someone
    REPAYMENT // Money received back from a LENT transaction
}

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    OVERDUE,
    CANCELLED
}