package com.app.comtracker.data.repository

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.core.network.onError
import com.app.comtracker.core.network.onSuccess
import com.app.comtracker.data.local.dao.TrackerHistoryDao
import com.app.comtracker.data.model.TrackBodyModelDTO
import com.app.comtracker.data.model.TrackDTO
import com.app.comtracker.data.model.TrackerHistoryDTO
import com.app.comtracker.data.remote.TrackerRemoteSource
import jakarta.inject.Inject

internal class TrackerRepositoryImpl @Inject constructor(
    private val local: TrackerHistoryDao,
    private val remote: TrackerRemoteSource
) : TrackerRepository {
    override suspend fun singleTrack(localId: Long): ApiResponse<TrackDTO> {

        val trackerHistory = local.get(id = localId)
        val body =
            TrackBodyModelDTO(
                leadDateTime = trackerHistory.createdAt,
                text = trackerHistory.message,
                phoneNumber = trackerHistory.phoneNumber,
                leadType = trackerHistory.type,
                storeId = 1
            )


        return remote.singleTrack(body = body).onSuccess {
            setUploadedFlag(localId)

        }.onError { _, _ ->
            updateRetryCountTrackerHistory(id = localId, count = trackerHistory.retryCount + 1)
        }
    }

    override suspend fun postHistories(): ApiResponse<TrackDTO> {
        val trackerHistories = getNotUploadedList(page = 1, pageSize = 100)
        if (trackerHistories.isEmpty()) {
            return ApiResponse.Error(message = "EMPTY")
        }
        val body = trackerHistories.map { trackerHistory ->
            TrackBodyModelDTO(
                leadDateTime = trackerHistory.createdAt,
                text = trackerHistory.message,
                phoneNumber = trackerHistory.phoneNumber,
                leadType = trackerHistory.type,
                storeId = 1
            )
        }
        return remote.multiTrack(body = body).onSuccess {
            trackerHistories.forEach {
                setUploadedFlag(id = it.id)
            }

        }.onError { _, _ ->
            trackerHistories.forEach {
                updateRetryCountTrackerHistory(id = it.id, it.retryCount + 1)
            }
        }
    }

    override suspend fun addHistory(tracker: TrackerHistoryDTO): Long {
        return local.insert(tracker)
    }

    override suspend fun getHistory(pageSize: Int, page: Int): List<TrackerHistoryDTO> {
        val offset = (page - 1) * pageSize
        return local.getList(limit = pageSize, offset = offset)
    }

    override suspend fun getSmsHistory(pageSize: Int, page: Int): List<TrackerHistoryDTO> {
        val offset = (page - 1) * pageSize
        return local.getSmsHistoryList(limit = pageSize, offset = offset)
    }

    override suspend fun getCallHistory(pageSize: Int, page: Int): List<TrackerHistoryDTO> {
        val offset = (page - 1) * pageSize
        return local.getCallHistoryList(limit = pageSize, offset = offset)
    }

    override suspend fun getNotUploadedList(pageSize: Int, page: Int): List<TrackerHistoryDTO> {
        val offset = (page - 1) * pageSize
        return local.getNotUploadedList(limit = pageSize, offset = offset)
    }

    override suspend fun getUploadedList(pageSize: Int, page: Int): List<TrackerHistoryDTO> {
        val offset = (page - 1) * pageSize
        return local.getUploadedList(limit = pageSize, offset = offset)
    }

    override suspend fun setUploadedFlag(id: Long) {
        local.setUploadedFlag(id = id)
    }

    override suspend fun updateRetryCountTrackerHistory(id: Long, count: Long) {
        local.updateRetryCount(id = id, count = count)
    }
}