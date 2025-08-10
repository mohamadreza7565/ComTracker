package com.app.comtracker.core.di

import com.app.comtracker.domain.usecases.PostSingleTrackUseCases
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UseCaseEntryPoint {
    fun postSingleTrackUseCases(): PostSingleTrackUseCases
}