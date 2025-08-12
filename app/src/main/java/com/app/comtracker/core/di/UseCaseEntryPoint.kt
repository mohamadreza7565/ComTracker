package com.app.comtracker.core.di

import com.app.comtracker.domain.usecases.AddTrackerHistoryUseCase
import com.app.comtracker.domain.usecases.PostHistoriesTrackUseCase
import com.app.comtracker.domain.usecases.PostSingleTrackUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UseCaseEntryPoint {
    fun postSingleTrackUseCase(): PostSingleTrackUseCase
    fun postHistoriesTrackUseCase(): PostHistoriesTrackUseCase
    fun addHistoryUseCase(): AddTrackerHistoryUseCase
}