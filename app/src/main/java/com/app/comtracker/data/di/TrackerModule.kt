package com.app.comtracker.data.di

import com.app.comtracker.data.repository.TrackerRepository
import com.app.comtracker.data.repository.TrackerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TrackerModule {

    @Binds
    abstract fun bindTrackerRepository(
        trackerRepositoryImpl: TrackerRepositoryImpl
    ): TrackerRepository
}
