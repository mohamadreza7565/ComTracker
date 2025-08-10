package com.app.comtracker.domain.usecases

import com.app.comtracker.data.repository.TrackerRepository
import javax.inject.Inject

class UpdateRetryCountTrackerHistoryUseCase @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(id: Long, count: Long) {
        trackerRepository.updateRetryCountTrackerHistory(id = id, count = count)
    }
}