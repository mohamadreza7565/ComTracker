package com.app.comtracker.domain.model

data class TrackerHistory(
    val id: Int,
    val isUploaded: Boolean,
    val type: TrackerHistoryType,
    val phoneNumber: String,
    val message: String,
    val createdAt: String
)

enum class TrackerHistoryType() {
    SMS, CALL
}
