package com.app.comtracker.data.remote

import com.app.comtracker.core.network.ApiResponse
import com.app.comtracker.core.network.fetch
import com.app.comtracker.data.model.TrackDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import javax.inject.Inject

internal class TrackerRemoteSource @Inject constructor(
    private val client: HttpClient,
) {

    suspend fun singleTrack(): ApiResponse<TrackDTO> {
        return client.fetch<ApiResponse.Base<TrackDTO>, TrackDTO> {
            url("")
        }
    }

}