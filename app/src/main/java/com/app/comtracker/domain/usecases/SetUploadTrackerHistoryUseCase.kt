package com.app.comtracker.domain.usecases

import com.app.comtracker.data.repository.TrackerRepository
import javax.inject.Inject

class SetUploadTrackerHistoryUseCase @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(id: Long) {
        trackerRepository.setUploadedFlag(id = id)
    }
}