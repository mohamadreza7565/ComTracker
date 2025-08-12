package com.app.comtracker.core.network

sealed class ApiResponse<out T> {


    data class Success<T>(val data: T?, val message: String? = null) : ApiResponse<T>()

    data class Error<T>(
        val data: T? = null,
        val message: String,
        val status: Int = -1
    ) : ApiResponse<T>()
}

inline fun <T> ApiResponse<T>.onSuccess(block: (T?) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Success) {
        block(this.data)
    }
    return this
}

inline fun <T> ApiResponse<T>.onError(block: (message: String, status: Int) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Error) {
        block(this.message, this.status)
    }
    return this
}

suspend fun <R, O> ApiResponse<R>.mapper(
    onError: (suspend (String, Int) -> Unit)? = null,
    onSuccess: suspend (R?) -> O
): ApiResponse<O> {
    return when (this) {
        is ApiResponse.Success -> ApiResponse.Success(onSuccess(this.data))
        is ApiResponse.Error -> {
            if (onError != null) {
                onError(this.message, this.status)
            }
            this as ApiResponse<O>
        }

        else -> this as ApiResponse<O>
    }
}

