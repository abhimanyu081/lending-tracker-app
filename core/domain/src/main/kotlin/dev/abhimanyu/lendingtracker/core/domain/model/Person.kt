package dev.abhimanyu.lendingtracker.core.domain.model

import java.util.Date

data class Person(
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String? = null,
    val address: String? = null,
    val notes: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)