package dev.abhimanyu.lendingtracker.core.data.mapper

import dev.abhimanyu.lendingtracker.core.data.database.dao.TransactionWithPerson
import dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionEntity
import dev.abhimanyu.lendingtracker.core.domain.model.Transaction
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionStatus
import dev.abhimanyu.lendingtracker.core.domain.model.TransactionType

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        personId = personId,
        amount = amount,
        type = when (type) {
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.LENT -> TransactionType.LENT
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.BORROWED -> TransactionType.BORROWED
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.REPAYMENT -> TransactionType.REPAYMENT
        },
        parentTransactionId = parentTransactionId,
        purpose = purpose,
        interestRate = interestRate,
        dueDate = dueDate,
        notes = notes,
        status = when (status) {
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.PENDING -> TransactionStatus.PENDING
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.COMPLETED -> TransactionStatus.COMPLETED
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.OVERDUE -> TransactionStatus.OVERDUE
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.CANCELLED -> TransactionStatus.CANCELLED
        },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun TransactionWithPerson.toDomain(): Transaction {
    return Transaction(
        id = id,
        personId = personId,
        personName = personName,
        amount = amount,
        type = when (type) {
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.LENT -> TransactionType.LENT
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.BORROWED -> TransactionType.BORROWED
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.REPAYMENT -> TransactionType.REPAYMENT
        },
        parentTransactionId = parentTransactionId,
        purpose = purpose,
        interestRate = interestRate,
        dueDate = dueDate,
        notes = notes,
        status = when (status) {
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.PENDING -> TransactionStatus.PENDING
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.COMPLETED -> TransactionStatus.COMPLETED
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.OVERDUE -> TransactionStatus.OVERDUE
            dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.CANCELLED -> TransactionStatus.CANCELLED
        },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        personId = personId,
        amount = amount,
        type = when (type) {
            TransactionType.LENT -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.LENT
            TransactionType.BORROWED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.BORROWED
            TransactionType.REPAYMENT -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionType.REPAYMENT
        },
        parentTransactionId = parentTransactionId,
        purpose = purpose,
        interestRate = interestRate,
        dueDate = dueDate,
        notes = notes,
        status = when (status) {
            TransactionStatus.PENDING -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.PENDING
            TransactionStatus.COMPLETED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.COMPLETED
            TransactionStatus.OVERDUE -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.OVERDUE
            TransactionStatus.CANCELLED -> dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionStatus.CANCELLED
        },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}