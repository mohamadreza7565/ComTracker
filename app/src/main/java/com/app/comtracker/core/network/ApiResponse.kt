package com.app.comtracker.core.network

sealed class ApiResponse<out T> {

    data class Base<T>(
        val data: T?,
        val message: String?
    ) : ApiResponse<T>()

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

inline fun <T> ApiResponse<T>.onBase(block: () -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Base) {
        block()
    }
    return this
}

val ApiResponse<*>.isSuccess: Boolean
    get() = this is ApiResponse.Success

val ApiResponse<*>.isError: Boolean
    get() = this is ApiResponse.Error

class ApiResponseScope<T>(initial: T) {

    var result: T = initial
        private set

    private var error: ApiResponse.Error<T>? = null

    fun update(transform: T.() -> T) {
        result = result.transform()
    }

    private fun fail(message: String, status: Int): Nothing {
        error = ApiResponse.Error(message = message, status = status)
        throw AbortComposeException()
    }

    fun build(): ApiResponse<T> = error ?: ApiResponse.Success(result)

    suspend fun <R, O> ApiResponse<R>.mapper(
        onError: (suspend (String, Int) -> Unit)? = null,
        onSuccess: suspend (R?) -> O
    ): ApiResponse<O> {
        return when (this) {
            is ApiResponse.Success -> ApiResponse.Success(onSuccess(this.data))
            is ApiResponse.Error -> {
                if (onError != null) {
                    onError(this.message, this.status)
                    this as ApiResponse<O>
                } else {
                    fail(this.message, this.status)
                }
            }
            else -> this as ApiResponse<O>
        }
    }

    class AbortComposeException : Exception()
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

suspend fun <T> combineApiResponse(
    result: T,
    block: suspend ApiResponseScope<T>.() -> Unit
): ApiResponse<T> {
    ApiResponseScope(result).apply {
        return try {
            block()
            build()
        } catch (_: ApiResponseScope.AbortComposeException) {
            build()
        }
    }
}
