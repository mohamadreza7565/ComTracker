package com.app.comtracker.feature.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.comtracker.core.network.api
import com.app.comtracker.core.network.execute
import com.app.comtracker.domain.model.TrackerHistory
import com.app.comtracker.domain.usecases.GetCallTrackerHistoryListUseCase
import com.app.comtracker.domain.usecases.GetNotUploadedTrackerHistoryListUseCase
import com.app.comtracker.domain.usecases.GetSmsTrackerHistoryListUseCase
import com.app.comtracker.domain.usecases.GetTrackerHistoryListUseCase
import com.app.comtracker.domain.usecases.GetUploadedTrackerHistoryListUseCase
import com.app.comtracker.domain.usecases.PostSingleTrackUseCase
import com.app.comtracker.domain.usecases.SetUploadTrackerHistoryUseCase
import com.app.comtracker.domain.usecases.UpdateRetryCountTrackerHistoryUseCase
import com.app.comtracker.utilities.State
import com.app.comtracker.utilities.pushToSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel @Inject constructor(
    private val getTrackerHistoryListUseCase: GetTrackerHistoryListUseCase,
    private val getNotUploadedTrackerHistoryListUseCase: GetNotUploadedTrackerHistoryListUseCase,
    private val getUploadedTrackerHistoryListUseCase: GetUploadedTrackerHistoryListUseCase,
    private val getSmsTrackerHistoryListUseCase: GetSmsTrackerHistoryListUseCase,
    private val getCallTrackerHistoryListUseCase: GetCallTrackerHistoryListUseCase,
    private val postSingleTrackUseCase: PostSingleTrackUseCase,
    private val setUploadTrackerHistoryUseCase: SetUploadTrackerHistoryUseCase,
    private val updateRetryCountTrackerHistoryUseCase: UpdateRetryCountTrackerHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<HistoryListState>>(State.Init(HistoryListState()))
    val state = _state.asStateFlow()

    private val pageSize = 200

    init {
        getAllHistories()
    }

    fun getAllHistories(loadMore: Boolean = false) {

        val page = when (loadMore) {
            true -> (state.value.data?.page ?: 1) + 1
            false -> 1
        }

        execute(
            block = { getTrackerHistoryListUseCase(page = page, pageSize = pageSize) },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    updateList(
                        response = response,
                        page = page,
                        loadMore = loadMore
                    )
                }
            }
        )
    }


    private fun getNotUploadedHistories(loadMore: Boolean = false) {

        val page = when (loadMore) {
            true -> (state.value.data?.page ?: 1) + 1
            false -> 1
        }

        execute(
            block = { getNotUploadedTrackerHistoryListUseCase(page = page, pageSize = pageSize) },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    updateList(
                        response = response,
                        page = page,
                        loadMore = loadMore
                    )
                }
            }
        )
    }

    private fun getUploadedHistories(loadMore: Boolean = false) {

        val page = when (loadMore) {
            true -> (state.value.data?.page ?: 1) + 1
            false -> 1
        }

        execute(
            block = { getUploadedTrackerHistoryListUseCase(page = page, pageSize = pageSize) },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    updateList(
                        response = response,
                        page = page,
                        loadMore = loadMore
                    )
                }
            }
        )
    }

    private fun getSmsTrackerHistoryList(loadMore: Boolean = false) {

        val page = when (loadMore) {
            true -> (state.value.data?.page ?: 1) + 1
            false -> 1
        }

        execute(
            block = { getSmsTrackerHistoryListUseCase(page = page, pageSize = pageSize) },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    updateList(
                        response = response,
                        page = page,
                        loadMore = loadMore
                    )
                }
            }
        )
    }

    private fun getCallTrackerHistoryList(loadMore: Boolean = false) {
        val page = when (loadMore) {
            true -> (state.value.data?.page ?: 1) + 1
            false -> 1
        }

        execute(
            block = { getCallTrackerHistoryListUseCase(page = page, pageSize = pageSize) },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    updateList(
                        response = response,
                        page = page,
                        loadMore = loadMore
                    )
                }
            }
        )
    }

    fun updateFilter(index: Int) {
        _state.update {
            it.data?.copy(selectedFilterIndex = index).pushToSuccess()
        }
        when (index) {
            0 -> getAllHistories()
            1 -> getNotUploadedHistories()
            2 -> getUploadedHistories()
            3 -> getSmsTrackerHistoryList()
            4 -> getCallTrackerHistoryList()
        }
    }

    fun retry(index: Int, history: TrackerHistory) {
        val histories = state.value.data?.histories.orEmpty().toMutableList()
        if (histories.isEmpty()) return
        api(
            scope = viewModelScope,
            block = { postSingleTrackUseCase(localId = history.id) },
            callBack = {
                onSuccess {
                    setUploadTrackerHistoryUseCase(history.id)
                    histories[index] = histories[index].copy(isUploaded = true)
                    _state.update {
                        it.data?.copy(histories = histories.toImmutableList()).pushToSuccess()
                    }
                }
                onError { _, _ ->
                    val retryCount = history.retryCount + 1
                    updateRetryCountTrackerHistoryUseCase(
                        id = history.id,
                        count = retryCount
                    )
                    histories[index] = histories[index].copy(retryCount = retryCount)
                    _state.update {
                        it.data?.copy(histories = histories.toImmutableList()).pushToSuccess()
                    }
                }
            }
        )
    }

    private fun updateList(
        response: ImmutableList<TrackerHistory>,
        loadMore: Boolean,
        page: Int
    ) {
        val histories = state.value.data?.histories.orEmpty().toMutableList()
        if (!loadMore) histories.clear()
        histories.addAll(response)
        _state.update {
            it.data?.copy(
                isLastPage = response.isEmpty(),
                page = page,
                histories = histories.toImmutableList()
            ).pushToSuccess()
        }
    }

}