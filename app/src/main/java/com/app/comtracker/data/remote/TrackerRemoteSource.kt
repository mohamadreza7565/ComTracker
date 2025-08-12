package com.app.comtracker.data.remote

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.core.network.fetch
import com.app.comtracker.data.model.TrackBodyModelDTO
import com.app.comtracker.data.model.TrackDTO
import com.app.comtracker.utilities.Constant.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import javax.inject.Inject
import io.ktor.http.HttpMethod

internal class TrackerRemoteSource @Inject constructor(
    private val client: HttpClient,
) {

    suspend fun singleTrack(body: TrackBodyModelDTO): ApiResponse<TrackDTO> {
        return client.fetch<TrackDTO> {
            setBody(body)
            method = HttpMethod.Post
            url("${BASE_URL}SingleAdd")
        }
    }

    suspend fun multiTrack(body: List<TrackBodyModelDTO>): ApiResponse<TrackDTO> {
        return client.fetch<TrackDTO> {
            setBody(body)
            method = HttpMethod.Post
            url("${BASE_URL}MultiAdd")
        }
    }

}