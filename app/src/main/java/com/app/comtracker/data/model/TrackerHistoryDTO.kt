package com.app.comtracker.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "tbl_tracker_history")
data class TrackerHistoryDTO(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "is_uploaded", defaultValue = "0")
    val isUploaded: Int,
    @ColumnInfo(name = "type", defaultValue = "")
    val type: String,
    @ColumnInfo(name = "phone_number", defaultValue = "")
    val phoneNumber: String,
    @ColumnInfo(name = "message", defaultValue = "")
    val message: String,
    @ColumnInfo(name = "created_at", defaultValue = "")
    val createdAt: String,
    @ColumnInfo(name = "retry_count", defaultValue = "1")
    val retryCount: Long
)
