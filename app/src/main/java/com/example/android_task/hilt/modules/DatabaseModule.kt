package com.example.android_task.hilt.modules

import android.content.Context
import androidx.room.Room
import com.example.android_task.data.dao.SelectTaskDAO
import com.example.android_task.utils.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    // No @Singleton needed here if the DB instance is a Singleton,
    // it will return the same DAO instance from the same DB instance
    fun provideUserDao(database: AppDatabase): SelectTaskDAO {
        return database.taskDao()
    }

}