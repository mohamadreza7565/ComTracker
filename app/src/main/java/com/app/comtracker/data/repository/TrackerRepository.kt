package com.app.comtracker.data.repository

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.data.model.TrackDTO
import com.app.comtracker.data.model.TrackerHistoryDTO

interface TrackerRepository {
    suspend fun singleTrack(): ApiResponse<TrackDTO>
    suspend fun addHistory(tracker: TrackerHistoryDTO): Long
    suspend fun getHistory(): List<TrackerHistoryDTO>
    suspend fun getSmsHistory(): List<TrackerHistoryDTO>
    suspend fun getCallHistory(): List<TrackerHistoryDTO>
    suspend fun getNotUploadedList(): List<TrackerHistoryDTO>
    suspend fun getUploadedList(): List<TrackerHistoryDTO>
    suspend fun setUploadedFlag(id: Long)
    suspend fun updateRetryCountTrackerHistory(id: Long, count: Long)
}