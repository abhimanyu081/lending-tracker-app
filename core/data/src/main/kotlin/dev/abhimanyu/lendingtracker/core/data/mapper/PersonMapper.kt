package dev.abhimanyu.lendingtracker.core.data.mapper

import dev.abhimanyu.lendingtracker.core.data.database.entities.PersonEntity
import dev.abhimanyu.lendingtracker.core.domain.model.Person

fun PersonEntity.toDomain(): Person {
    return Person(
        id = id,
        name = name,
        phone = phone,
        email = email,
        address = address,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Person.toEntity(): PersonEntity {
    return PersonEntity(
        id = id,
        name = name,
        phone = phone,
        email = email,
        address = address,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}