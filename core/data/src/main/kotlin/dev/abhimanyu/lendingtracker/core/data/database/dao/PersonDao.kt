package dev.abhimanyu.lendingtracker.core.data.database.dao

import androidx.room.*
import dev.abhimanyu.lendingtracker.core.data.database.entities.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    
    @Query("SELECT * FROM persons ORDER BY name ASC")
    fun getAllPersons(): Flow<List<PersonEntity>>
    
    @Query("SELECT * FROM persons WHERE id = :id")
    suspend fun getPersonById(id: Long): PersonEntity?
    
    @Query("SELECT * FROM persons WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%'")
    fun searchPersons(query: String): Flow<List<PersonEntity>>
    
    @Insert
    suspend fun insertPerson(person: PersonEntity): Long
    
    @Update
    suspend fun updatePerson(person: PersonEntity)
    
    @Delete
    suspend fun deletePerson(person: PersonEntity)
    
    @Query("DELETE FROM persons WHERE id = :id")
    suspend fun deletePersonById(id: Long)
}