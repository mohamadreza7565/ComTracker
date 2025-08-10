package com.app.comtracker.utilities

import androidx.compose.runtime.Composable
import kotlin.apply

sealed class State<T>(
    val data: T?,
    var message: String? = null
) {
    class Init<T>(data: T) : State<T>(data = data)
    class Success<T>(data: T?) : State<T>(data = data)
    class Loading<T>(data: T?) : State<T>(data = data)

    class Error<T>(message: String?, data: T?) :
        State<T>(message = message, data = data)
}

fun <T> T?.pushToLoading() = State.Loading(data = this)
fun <T> T?.pushToSuccess() = State.Success(data = this)
fun <T> T?.pushToError(message: String?) =
    State.Error<T>(message = message, data = this)

@Composable
infix fun <T> State<T>.onSuccess(
    block: @Composable (data: T) -> Unit
) = this.apply {
    if (this is State.Success) {
        if (data != null) {
            block(data)
        }
    }
}

@Composable
infix fun <T> State<T>.onLoading(
    block: @Composable () -> Unit
) = this.apply { if (this is State.Loading) block() }

@Composable
infix fun <T> State<T>.onError(
    block: @Composable (message: String?) -> Unit
) = this.apply { if (this is State.Error) block(this.message) }

@Composable
fun <T> State<T>.Draw(
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (data: T) -> Unit,
    onInit: @Composable () -> Unit = {},
    onError: @Composable (message: String?) -> Unit
) = when (this) {
    is State.Init -> onInit()
    is State.Loading -> onLoading()
    is State.Success -> if (this.data != null) {
        onSuccess(this.data)
    } else {
        Unit
    }

    is State.Error -> onError(this.message)
}
