package com.app.comtracker.data.repository

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.data.model.TrackBodyModelDTO
import com.app.comtracker.data.model.TrackDTO
import com.app.comtracker.data.model.TrackerHistoryDTO

interface TrackerRepository {
    suspend fun singleTrack(localId: Long): ApiResponse<TrackDTO>
    suspend fun postHistories(): ApiResponse<TrackDTO>
    suspend fun addHistory(tracker: TrackerHistoryDTO): Long
    suspend fun getHistory(pageSize: Int, page: Int): List<TrackerHistoryDTO>
    suspend fun getSmsHistory(pageSize: Int, page: Int): List<TrackerHistoryDTO>
    suspend fun getCallHistory(pageSize: Int, page: Int): List<TrackerHistoryDTO>
    suspend fun getNotUploadedList(pageSize: Int, page: Int): List<TrackerHistoryDTO>
    suspend fun getUploadedList(pageSize: Int, page: Int): List<TrackerHistoryDTO>
    suspend fun setUploadedFlag(id: Long)
    suspend fun updateRetryCountTrackerHistory(id: Long, count: Long)
}