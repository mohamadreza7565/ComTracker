package com.app.comtracker.domain.usecases

import android.os.Build
import com.app.comtracker.data.model.TrackerHistoryDTO
import com.app.comtracker.data.repository.TrackerRepository
import com.app.comtracker.domain.model.TrackerHistoryType
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AddTrackerHistoryUseCase @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(
        type: TrackerHistoryType,
        phoneNumber: String,
        message: String = ""
    ): Long {

        var createdAt = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            createdAt = now.format(formatter)
        }else{
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val now = Date()
            createdAt = sdf.format(now)
        }

        return trackerRepository.addHistory(
            TrackerHistoryDTO(
                id = 0,
                isUploaded = 0,
                retryCount = 1,
                phoneNumber = phoneNumber,
                message = if (type == TrackerHistoryType.SMS) message else "",
                createdAt = createdAt,
                type = type.key
            )
        )
    }
}