package com.app.comtracker.data.repository

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.data.local.dao.TrackerHistoryDao
import com.app.comtracker.data.model.TrackDTO
import com.app.comtracker.data.model.TrackerHistoryDTO
import com.app.comtracker.data.remote.TrackerRemoteSource
import jakarta.inject.Inject

internal class TrackerRepositoryImpl @Inject constructor(
    private val local: TrackerHistoryDao,
    private val remote: TrackerRemoteSource
) : TrackerRepository {
    override suspend fun singleTrack(): ApiResponse<TrackDTO> {
        return remote.singleTrack()
    }

    override suspend fun addHistory(tracker: TrackerHistoryDTO): Long {
        return local.insert(tracker)
    }

    override suspend fun getHistory(): List<TrackerHistoryDTO> {
        return local.getList()
    }

    override suspend fun getSmsHistory(): List<TrackerHistoryDTO> {
        return local.getSmsHistoryList()
    }

    override suspend fun getCallHistory(): List<TrackerHistoryDTO> {
        return local.getCallHistoryList()
    }

    override suspend fun getNotUploadedList(): List<TrackerHistoryDTO> {
        return local.getNotUploadedList()
    }

    override suspend fun getUploadedList(): List<TrackerHistoryDTO> {
        return local.getUploadedList()
    }

    override suspend fun setUploadedFlag(id: Long) {
        local.setUploadedFlag(id = id)
    }

    override suspend fun updateRetryCountTrackerHistory(id: Long, count: Long) {
        local.updateRetryCount(id = id, count = count)
    }
}