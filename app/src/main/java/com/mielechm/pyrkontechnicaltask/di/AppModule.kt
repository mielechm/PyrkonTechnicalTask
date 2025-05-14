package com.mielechm.pyrkontechnicaltask.di

import android.content.Context
import androidx.room.Room
import com.mielechm.pyrkontechnicaltask.data.model.PyrkonTechTaskDao
import com.mielechm.pyrkontechnicaltask.data.model.PyrkonTechTaskDatabase
import com.mielechm.pyrkontechnicaltask.repositories.DefaultLocalDbGuestRepository
import com.mielechm.pyrkontechnicaltask.repositories.LocalDbGuestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePyrkonTechTaskDb(@ApplicationContext context: Context): PyrkonTechTaskDatabase =
        Room.databaseBuilder(
            context,
            PyrkonTechTaskDatabase::class.java,
            PyrkonTechTaskDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration(false).build()

    @Singleton
    @Provides
    fun providePyrkonTechTaskDao(pyrkonTechTaskDatabase: PyrkonTechTaskDatabase): PyrkonTechTaskDao =
        pyrkonTechTaskDatabase.pyrkonTechTaskDao()

    @Singleton
    @Provides
    fun provideLocalDbGuestRepository(dao: PyrkonTechTaskDao) =
        DefaultLocalDbGuestRepository(dao) as LocalDbGuestRepository
}