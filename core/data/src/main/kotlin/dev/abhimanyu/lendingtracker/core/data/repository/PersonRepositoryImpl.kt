package dev.abhimanyu.lendingtracker.core.data.repository

import dev.abhimanyu.lendingtracker.core.data.database.dao.PersonDao
import dev.abhimanyu.lendingtracker.core.data.mapper.toDomain
import dev.abhimanyu.lendingtracker.core.data.mapper.toEntity
import dev.abhimanyu.lendingtracker.core.domain.model.Person
import dev.abhimanyu.lendingtracker.core.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepositoryImpl @Inject constructor(
    private val personDao: PersonDao
) : PersonRepository {
    
    override fun getAllPersons(): Flow<List<Person>> {
        return personDao.getAllPersons().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun getPersonById(id: Long): Person? {
        return personDao.getPersonById(id)?.toDomain()
    }
    
    override fun searchPersons(query: String): Flow<List<Person>> {
        return personDao.searchPersons(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun insertPerson(person: Person): Long {
        return personDao.insertPerson(person.toEntity())
    }
    
    override suspend fun updatePerson(person: Person) {
        personDao.updatePerson(person.toEntity())
    }
    
    override suspend fun deletePerson(person: Person) {
        personDao.deletePerson(person.toEntity())
    }
    
    override suspend fun deletePersonById(id: Long) {
        personDao.deletePersonById(id)
    }
}