package com.app.comtracker.feature.history

import com.app.comtracker.domain.model.TrackerHistory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HistoryListState(
    val histories: ImmutableList<TrackerHistory> = persistentListOf(),
    val filters: ImmutableList<String> = persistentListOf(
        "All",
        "Pending",
        "Success",
        "Sms",
        "Call"
    ),
    val selectedFilterIndex: Int = 0
)