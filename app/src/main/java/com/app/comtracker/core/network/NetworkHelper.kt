package com.app.comtracker.core.network

import com.app.comtracker.utilities.Constant.IO_EXCEPTION_MESSAGE
import com.app.comtracker.utilities.Constant.UN_HANDLE_EXCEPTION_MESSAGE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.isSuccess
import kotlinx.io.IOException


suspend inline fun <reified T> HttpClient.fetch(
    block: HttpRequestBuilder.() -> Unit
): ApiResponse<T> {
    return try {
        val call = request { block() }
        val response = call.body<T>()

        when (call.status.isSuccess()) {
            true -> ApiResponse.Success(data = response)

            false -> {
                val errorMessage = UN_HANDLE_EXCEPTION_MESSAGE
                ApiResponse.Error(
                    status = call.status.value,
                    message = errorMessage
                )
            }
        }
    } catch (e: ClientRequestException) {
        ApiResponse.Error(
            status = e.response.status.value,
            message = UN_HANDLE_EXCEPTION_MESSAGE
        )
    } catch (e: ServerResponseException) {
        ApiResponse.Error(
            status = e.response.status.value,
            message = UN_HANDLE_EXCEPTION_MESSAGE
        )
    } catch (_: IOException) {
        ApiResponse.Error(message = IO_EXCEPTION_MESSAGE, status = 0)
    } catch (_: Exception) {
        ApiResponse.Error(message = UN_HANDLE_EXCEPTION_MESSAGE)
    }
}
