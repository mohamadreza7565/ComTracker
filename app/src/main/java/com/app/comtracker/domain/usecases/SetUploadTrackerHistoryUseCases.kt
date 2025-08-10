package com.app.comtracker.domain.usecases

import com.app.comtracker.data.repository.TrackerRepository
import com.app.comtracker.domain.model.TrackerHistory
import com.app.comtracker.domain.model.TrackerHistoryType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class SetUploadTrackerHistoryUseCases @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(id: Long) {
        trackerRepository.setUploadedFlag(id = id)
    }
}