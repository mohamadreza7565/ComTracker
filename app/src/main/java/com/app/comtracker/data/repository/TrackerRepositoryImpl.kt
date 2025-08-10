package com.app.comtracker.data.repository

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.data.model.TrackDTO
import com.app.comtracker.data.remote.TrackerRemoteSource
import jakarta.inject.Inject

internal class TrackerRepositoryImpl @Inject constructor(
    private val remote: TrackerRemoteSource
) : TrackerRepository {
    override suspend fun singleTrack(): ApiResponse<TrackDTO> {
        return remote.singleTrack();
    }
}