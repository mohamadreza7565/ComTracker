package com.app.comtracker.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpSendPipeline
import io.ktor.util.AttributeKey
import kotlin.apply

internal class HeaderInterceptor() {


    private fun addPasswordToHeader(request: HttpRequestBuilder) {
        request.headers["leadPassword"] = "zavoshE0ne"
    }

    class Config

    companion object : HttpClientPlugin<Config, HeaderInterceptor> {
        override val key: AttributeKey<HeaderInterceptor>
            get() = io.ktor.util.AttributeKey("TokenInterceptor")

        override fun install(plugin: HeaderInterceptor, scope: HttpClient) {
            scope.sendPipeline.intercept(HttpSendPipeline.Before) {
                plugin.addPasswordToHeader(context)
                proceed()
            }

        }

        override fun prepare(block: Config.() -> Unit): HeaderInterceptor {
            val config = Config().apply(block)
            return HeaderInterceptor( )
        }
    }
}
