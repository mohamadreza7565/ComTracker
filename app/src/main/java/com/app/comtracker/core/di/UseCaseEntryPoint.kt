package com.app.comtracker.core.di

import com.app.comtracker.domain.usecases.AddTrackerHistoryUseCase
import com.app.comtracker.domain.usecases.GetNotUploadedTrackerHistoryListUseCase
import com.app.comtracker.domain.usecases.PostSingleTrackUseCase
import com.app.comtracker.domain.usecases.SetUploadTrackerHistoryUseCase
import com.app.comtracker.domain.usecases.UpdateRetryCountTrackerHistoryUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UseCaseEntryPoint {
    fun updateRetryCountTrackerHistoryUseCase(): UpdateRetryCountTrackerHistoryUseCase
    fun postSingleTrackUseCase(): PostSingleTrackUseCase
    fun addHistoryUseCase(): AddTrackerHistoryUseCase
    fun setUploadTrackerHistoryUseCase(): SetUploadTrackerHistoryUseCase
    fun getNotUploadedTrackerHistoryListUseCase(): GetNotUploadedTrackerHistoryListUseCase
}