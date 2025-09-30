package com.app.comtracker.domain.usecases

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.core.network.mapper
import com.app.comtracker.data.repository.TrackerRepository
import com.app.comtracker.domain.model.TrackModel
import javax.inject.Inject

class PostSingleTrackUseCase @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(
        localId: Long
    ): ApiResponse<TrackModel> {
        return trackerRepository.singleTrack(localId = localId).mapper { response ->
            TrackModel(
                success = response?.success == true,
                status = response?.status ?: "0",
            )
        }
    }
}