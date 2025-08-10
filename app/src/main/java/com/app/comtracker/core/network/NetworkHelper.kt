package com.app.comtracker.core.network

import com.app.comtracker.utilities.Constant.IO_EXCEPTION_MESSAGE
import com.app.comtracker.utilities.Constant.UN_HANDLE_EXCEPTION_MESSAGE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import kotlin.text.orEmpty

suspend inline fun <reified T> HttpResponse.execute() = body<T>()

suspend inline fun <reified T, reified R> HttpClient.fetch(
    block: HttpRequestBuilder.() -> Unit
): ApiResponse<R> {
    return try {
        val call = request { block() }
        val response = call.body<T>()

        when (call.status.isSuccess()) {
            true -> {
                val data = when (response) {
                    is ApiResponse.Base<*> -> response.data
                    else -> response
                } as R
                ApiResponse.Success(
                    data = data
                )
            }

            false -> {
                val errorMessage = when (response) {
                    is ApiResponse.Base<*> -> response.message ?: UN_HANDLE_EXCEPTION_MESSAGE
                    else -> UN_HANDLE_EXCEPTION_MESSAGE
                }
                ApiResponse.Error(
                    status = call.status.value,
                    message = errorMessage
                )
            }
        }
    } catch (e: ClientRequestException) {
        ApiResponse.Error(
            status = e.response.status.value,
            message = e.response.body<ApiResponse.Base<T>>().message.orEmpty()
        )
    } catch (e: ServerResponseException) {
        ApiResponse.Error(
            status = e.response.status.value,
            message = e.response.body<ApiResponse.Base<T>>().message.orEmpty()
        )
    } catch (_: IOException) {
        ApiResponse.Error(message = IO_EXCEPTION_MESSAGE, status = 0)
    } catch (_: Exception) {
        ApiResponse.Error(message = UN_HANDLE_EXCEPTION_MESSAGE)
    }
}
