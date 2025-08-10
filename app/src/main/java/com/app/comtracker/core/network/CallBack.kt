package com.app.comtracker.core.network

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.apply

class CallBack<T> {
    var debounceJob: Job? = null
    var loading: (isLoading: Boolean) -> Unit = {}
    var success: suspend (T) -> Unit = {}
    var error: suspend (String?, Int) -> Unit = { _, _ -> }

    fun onLoading(block: (isLoading: Boolean) -> Unit) {
        this.loading = block
    }

    fun onSuccess(block: suspend (T) -> Unit) {
        this.success = block
    }

    fun onError(block: suspend (String?, Int) -> Unit) {
        this.error = block
    }
}

fun <T> execute(
    block: suspend () -> T,
    scope: CoroutineScope,
    callBack: CallBack<T>.() -> Unit
) {
    CallBack<T>().apply(callBack).apply {
        val handler = CoroutineExceptionHandler { _, throwable ->
            scope.launch {
                error.invoke(throwable.message, 0)
            }
        }
        loading(true)
        scope.launch(handler) {
            val response = block()
            loading(false)
            success(response)
        }
    }
}

fun <T> api(
    block: suspend () -> ApiResponse<T>,
    scope: CoroutineScope,
    callBack: CallBack<T>.() -> Unit
): Job {
    CallBack<T>().apply(callBack).apply {
        val handler = CoroutineExceptionHandler { _, throwable ->
            scope.launch {
                error.invoke(throwable.message, 0)
            }
        }
        loading(true)
        return scope.launch(handler) {
            val response = block()
            loading(false)
            when (response) {
                is ApiResponse.Success<*> -> success(response.data as T)
                is ApiResponse.Error<*> -> error(response.message, response.status)
                else -> {}
            }
        }
    }
}

fun <T> debounceApi(
    block: suspend () -> ApiResponse<T>,
    scope: CoroutineScope,
    job: Job?,
    timeMillis: Long = 300L,
    callBack: CallBack<T>.() -> Unit,
): Job {
    CallBack<T>().apply(callBack).apply {
        job?.cancel()
        val handler = CoroutineExceptionHandler { _, throwable ->
            scope.launch {
                error.invoke(throwable.message, 0)
            }
        }
        loading(true)
        return scope.launch(handler) {
            delay(timeMillis)
            val response = block()
            loading(false)
            when (response) {
                is ApiResponse.Success<*> -> success(response.data as T)
                is ApiResponse.Error<*> -> error(response.message, response.status)
                else -> {}
            }
        }
    }
}
