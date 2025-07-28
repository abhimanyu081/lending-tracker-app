package dev.abhimanyu.lendingtracker.core.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import dev.abhimanyu.lendingtracker.core.data.database.converters.BigDecimalConverter
import dev.abhimanyu.lendingtracker.core.data.database.converters.DateConverter
import dev.abhimanyu.lendingtracker.core.data.database.dao.PersonDao
import dev.abhimanyu.lendingtracker.core.data.database.dao.TransactionDao
import dev.abhimanyu.lendingtracker.core.data.database.entities.PersonEntity
import dev.abhimanyu.lendingtracker.core.data.database.entities.TransactionEntity

@Database(
    entities = [
        PersonEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    BigDecimalConverter::class
)
abstract class LendingTrackerDatabase : RoomDatabase() {
    
    abstract fun personDao(): PersonDao
    abstract fun transactionDao(): TransactionDao
    
    companion object {
        const val DATABASE_NAME = "lending_tracker_database"
        
        @Volatile
        private var INSTANCE: LendingTrackerDatabase? = null
        
        fun getDatabase(context: Context): LendingTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LendingTrackerDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}