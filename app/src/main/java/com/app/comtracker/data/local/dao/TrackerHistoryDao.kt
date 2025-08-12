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

    @Query("select * from tbl_tracker_history where id = :id")
    suspend fun get(id: Long): TrackerHistoryDTO

    @Query("DELETE FROM tbl_tracker_history where id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM tbl_tracker_history ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getList(limit: Int, offset: Int): List<TrackerHistoryDTO>

    @Query("select * from tbl_tracker_history where type = 2 ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getSmsHistoryList(limit: Int, offset: Int): List<TrackerHistoryDTO>

    @Query("select * from tbl_tracker_history where type = 1 ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getCallHistoryList(limit: Int, offset: Int): List<TrackerHistoryDTO>

    @Query("select * from tbl_tracker_history where is_uploaded = 0 ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getNotUploadedList(limit: Int, offset: Int): List<TrackerHistoryDTO>

    @Query("select * from tbl_tracker_history where is_uploaded = 1 ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getUploadedList(limit: Int, offset: Int): List<TrackerHistoryDTO>

    @Query("update tbl_tracker_history SET is_uploaded = 1 where id = :id")
    suspend fun setUploadedFlag(id: Long)

    @Query("update tbl_tracker_history SET retry_count = :count where id = :id")
    suspend fun updateRetryCount(id: Long, count: Long)

    @Update
    suspend fun update(tracker: TrackerHistoryDTO)

    @Query("DELETE FROM tbl_tracker_history where type = 1")
    suspend fun deleteAllCall()

    @Query("DELETE FROM tbl_tracker_history where type = 2")
    suspend fun deleteAllSms()

    @Query("DELETE FROM tbl_tracker_history where is_uploaded = 1")
    suspend fun deleteAllUploaded()

    @Query("DELETE FROM tbl_tracker_history where is_uploaded = 0")
    suspend fun deleteAllNotUploaded()

    @Query("DELETE FROM tbl_tracker_history")
    suspend fun deleteAll()

}