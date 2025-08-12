package com.app.comtracker.domain.usecases

import com.app.comtracker.data.repository.TrackerRepository
import com.app.comtracker.domain.model.TrackerHistory
import com.app.comtracker.domain.model.TrackerHistoryType
import com.app.comtracker.domain.utils.getPersianDate
import com.app.comtracker.domain.utils.getTrackerHistoryTypeByKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class GetCallTrackerHistoryListUseCase @Inject constructor(
    private val trackerRepository: TrackerRepository
) {
    suspend operator fun invoke(page: Int, pageSize: Int): ImmutableList<TrackerHistory> {
        return trackerRepository.getCallHistory(
            page = page,
            pageSize = pageSize
        ).map { response ->
            TrackerHistory(
                id = response.id,
                isUploaded = response.isUploaded == 1,
                type = getTrackerHistoryTypeByKey(response.type),
                message = response.message,
                phoneNumber = response.phoneNumber,
                createdAt = getPersianDate(response.createdAt),
                retryCount = response.retryCount
            )
        }.toImmutableList()
    }
}