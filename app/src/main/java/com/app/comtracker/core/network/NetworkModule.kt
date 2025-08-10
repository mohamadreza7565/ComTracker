package com.app.comtracker.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideKtorHttpClient(): HttpClient {
        return HttpClient(Android) {
            HttpClientConfig.install(ContentNegotiation) {
                gson()
            }

            HttpClientConfig.install(HttpTimeout) {
                HttpTimeoutConfig.requestTimeoutMillis = 30_000
                HttpTimeoutConfig.connectTimeoutMillis = 20_000
                HttpTimeoutConfig.socketTimeoutMillis = 30_000
            }


            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }
    }
}