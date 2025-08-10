package com.app.comtracker.domain.usecases

import com.app.comtracker.data.repository.TrackerRepository
import com.app.comtracker.domain.model.TrackerHistory
import com.app.comtracker.domain.model.TrackerHistoryType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class GetUploadedTrackerHistoryListUseCase @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(): ImmutableList<TrackerHistory> {
        return trackerRepository.getUploadedList().map { response ->
            TrackerHistory(
                id = response.id,
                isUploaded = response.isUploaded == 1,
                type = TrackerHistoryType.valueOf(response.type.uppercase()),
                message = response.message,
                phoneNumber = response.phoneNumber,
                createdAt = response.createdAt,
                retryCount = response.retryCount
            )
        }.toImmutableList()
    }
}