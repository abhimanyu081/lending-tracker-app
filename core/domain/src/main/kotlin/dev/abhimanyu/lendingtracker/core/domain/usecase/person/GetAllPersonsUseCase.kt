package dev.abhimanyu.lendingtracker.core.domain.usecase.person

import dev.abhimanyu.lendingtracker.core.domain.model.Person
import dev.abhimanyu.lendingtracker.core.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPersonsUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    operator fun invoke(): Flow<List<Person>> {
        return personRepository.getAllPersons()
    }
}