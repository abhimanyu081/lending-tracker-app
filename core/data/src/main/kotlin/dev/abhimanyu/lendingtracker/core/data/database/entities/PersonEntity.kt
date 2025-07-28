package dev.abhimanyu.lendingtracker.core.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String? = null,
    val address: String? = null,
    val notes: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)