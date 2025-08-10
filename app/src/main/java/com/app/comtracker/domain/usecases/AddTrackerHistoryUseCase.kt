package com.app.comtracker.domain.usecases

import com.app.comtracker.data.model.TrackerHistoryDTO
import com.app.comtracker.data.repository.TrackerRepository
import com.app.comtracker.domain.model.TrackerHistoryType
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

class AddTrackerHistoryUseCase @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(
        type: TrackerHistoryType,
        phoneNumber: String,
        message: String = ""
    ): Long {
        val persianDate = PersianDate()
        val dateText = "${persianDate.shYear}/${persianDate.shMonth}/${persianDate.shDay} - ${persianDate.hour}:${persianDate.minute}"

        return trackerRepository.addHistory(
            TrackerHistoryDTO(
                id = 0,
                isUploaded = 0,
                retryCount = 1,
                phoneNumber = phoneNumber,
                message = if (type == TrackerHistoryType.SMS) message else "",
                createdAt = dateText,
                type = type.name.lowercase()
            )
        )
    }
}