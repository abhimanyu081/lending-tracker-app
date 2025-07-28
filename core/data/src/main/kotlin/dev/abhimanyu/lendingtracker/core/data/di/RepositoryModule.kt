package dev.abhimanyu.lendingtracker.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.abhimanyu.lendingtracker.core.data.repository.PersonRepositoryImpl
import dev.abhimanyu.lendingtracker.core.data.repository.TransactionRepositoryImpl
import dev.abhimanyu.lendingtracker.core.domain.repository.PersonRepository
import dev.abhimanyu.lendingtracker.core.domain.repository.TransactionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindPersonRepository(
        personRepositoryImpl: PersonRepositoryImpl
    ): PersonRepository
    
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository
}