package com.app.comtracker.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.comtracker.core.network.execute
import com.app.comtracker.domain.usecases.GetTrackerHistoryListUseCase
import com.app.comtracker.utilities.State
import com.app.comtracker.utilities.pushToSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel @Inject constructor(
    private val getTrackerHistoryListUseCase: GetTrackerHistoryListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State<HistoryListState>>(State.Init(HistoryListState()))
    val state = _state.asStateFlow()

    init {
        getHistories()
    }

    private fun getHistories() {
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

}