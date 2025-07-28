package dev.abhimanyu.lendingtracker.core.domain.repository

import dev.abhimanyu.lendingtracker.core.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun getAllPersons(): Flow<List<Person>>
    suspend fun getPersonById(id: Long): Person?
    fun searchPersons(query: String): Flow<List<Person>>
    suspend fun insertPerson(person: Person): Long
    suspend fun updatePerson(person: Person)
    suspend fun deletePerson(person: Person)
    suspend fun deletePersonById(id: Long)
}