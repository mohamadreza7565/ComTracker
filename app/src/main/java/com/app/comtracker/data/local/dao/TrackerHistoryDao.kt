package com.app.comtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.comtracker.data.model.TrackerHistoryDTO

@Dao
interface TrackerHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tracker: TrackerHistoryDTO): Long

    @Query("select * from tbl_tracker_history")
    suspend fun getList(): List<TrackerHistoryDTO>

    @Query("select * from tbl_tracker_history where type = 'sms'")
    suspend fun getSmsHistoryList(): List<TrackerHistoryDTO>

    @Query("select * from tbl_tracker_history where type = 'call'")
    suspend fun getCallHistoryList(): List<TrackerHistoryDTO>

    @Query("select * from tbl_tracker_history where is_uploaded = 0")
    suspend fun getNotUploadedList(): List<TrackerHistoryDTO>

    @Query("update tbl_tracker_history SET is_uploaded = 1 where id = :id")
    suspend fun setUploadedFlag(id: Long): List<TrackerHistoryDTO>

    @Update
    suspend fun update(tracker: TrackerHistoryDTO)

}