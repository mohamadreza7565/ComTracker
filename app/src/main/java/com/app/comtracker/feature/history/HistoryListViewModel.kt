package com.app.comtracker.feature.history

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

    init {
        getAllHistories()
    }

    private fun getAllHistories() {
        execute(
            block = { getTrackerHistoryListUseCase() },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    _state.update {
                        it.data?.copy(histories = response).pushToSuccess()
                    }
                }
            }
        )
    }


    private fun getNotUploadedHistories() {
        execute(
            block = { getNotUploadedTrackerHistoryListUseCase() },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    _state.update {
                        it.data?.copy(histories = response).pushToSuccess()
                    }
                }
            }
        )
    }

    private fun getUploadedHistories() {
        execute(
            block = { getUploadedTrackerHistoryListUseCase() },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    _state.update {
                        it.data?.copy(histories = response).pushToSuccess()
                    }
                }
            }
        )
    }

    private fun getSmsTrackerHistoryList() {
        execute(
            block = { getSmsTrackerHistoryListUseCase() },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    _state.update {
                        it.data?.copy(histories = response).pushToSuccess()
                    }
                }
            }
        )
    }

    private fun getCallTrackerHistoryList() {
        execute(
            block = { getCallTrackerHistoryListUseCase() },
            scope = viewModelScope,
            callBack = {
                onSuccess { response ->
                    _state.update {
                        it.data?.copy(histories = response).pushToSuccess()
                    }
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
            block = { postSingleTrackUseCase() },
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

}