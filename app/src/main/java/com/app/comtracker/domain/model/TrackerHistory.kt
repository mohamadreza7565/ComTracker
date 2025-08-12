package com.app.comtracker.domain.model

data class TrackerHistory(
    val id: Long,
    val retryCount: Long,
    val isUploaded: Boolean,
    val type: TrackerHistoryType,
    val phoneNumber: String,
    val message: String,
    val createdAt: String
)

enum class TrackerHistoryType(val key: Int) {
    SMS(key = 2), CALL(key = 1), OTHER(key = 0)
}
