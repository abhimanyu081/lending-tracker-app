package dev.abhimanyu.lendingtracker.core.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.abhimanyu.lendingtracker.core.data.database.LendingTrackerDatabase
import dev.abhimanyu.lendingtracker.core.data.database.dao.PersonDao
import dev.abhimanyu.lendingtracker.core.data.database.dao.TransactionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LendingTrackerDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LendingTrackerDatabase::class.java,
            LendingTrackerDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun providePersonDao(database: LendingTrackerDatabase): PersonDao {
        return database.personDao()
    }
    
    @Provides
    fun provideTransactionDao(database: LendingTrackerDatabase): TransactionDao {
        return database.transactionDao()
    }
}