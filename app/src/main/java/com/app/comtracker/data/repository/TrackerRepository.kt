package com.app.comtracker.data.repository

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.data.model.TrackDTO

interface TrackerRepository {
    suspend fun singleTrack(): ApiResponse<TrackDTO>
}