package dev.abhimanyu.lendingtracker.core.domain.usecase.person

import dev.abhimanyu.lendingtracker.core.domain.model.Person
import dev.abhimanyu.lendingtracker.core.domain.repository.PersonRepository
import java.util.Date
import javax.inject.Inject

class AddPersonUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    suspend operator fun invoke(
        name: String,
        phone: String,
        email: String? = null,
        address: String? = null,
        notes: String? = null
    ): Result<Long> {
        return try {
            if (name.isBlank()) {
                return Result.failure(IllegalArgumentException("Name cannot be empty"))
            }
            if (phone.isBlank()) {
                return Result.failure(IllegalArgumentException("Phone cannot be empty"))
            }
            
            val person = Person(
                name = name.trim(),
                phone = phone.trim(),
                email = email?.trim()?.takeIf { it.isNotBlank() },
                address = address?.trim()?.takeIf { it.isNotBlank() },
                notes = notes?.trim()?.takeIf { it.isNotBlank() },
                createdAt = Date(),
                updatedAt = Date()
            )
            
            val personId = personRepository.insertPerson(person)
            Result.success(personId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}