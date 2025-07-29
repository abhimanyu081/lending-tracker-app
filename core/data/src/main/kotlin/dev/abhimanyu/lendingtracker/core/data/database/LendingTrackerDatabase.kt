package dev.abhimanyu.lendingtracker.core.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2,
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
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add parentTransactionId column to transactions table
                database.execSQL("ALTER TABLE transactions ADD COLUMN parentTransactionId INTEGER")
            }
        }
        
        fun getDatabase(context: Context): LendingTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LendingTrackerDatabase::class.java,
                    DATABASE_NAME
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration() // For development - removes this in production
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}