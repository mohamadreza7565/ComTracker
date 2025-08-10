package com.app.comtracker.domain.usecases

import com.app.comtracker.data.model.TrackerHistoryDTO
import com.app.comtracker.data.repository.TrackerRepository
import com.app.comtracker.domain.model.TrackerHistoryType
import javax.inject.Inject

class AddTrackerHistoryUseCase @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(
        type: TrackerHistoryType,
        phoneNumber: String,
        message: String = ""
    ): Long {
        return trackerRepository.addHistory(
            TrackerHistoryDTO(
                id = 0,
                isUploaded = 0,
                phoneNumber = phoneNumber,
                message = if (type == TrackerHistoryType.SMS) message else "",
                createdAt = "",
                type = type.name.lowercase()
            )
        )
    }
}