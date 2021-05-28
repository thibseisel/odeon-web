/*
 * Copyright 2021 Thibault SEISEL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.thibseisel.ktor.client.oauth

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.HttpSend
import io.ktor.client.features.get
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.seconds

/**
 * Automatically retry [HttpClient] requests that failed due to server errors.
 *
 * ```
 * val http = HttpClient {
 *   install(AutoRetry) {
 *     maxRetryCount = 2,
 *     retryDuration = 5.seconds
 *   }
 * }
 * ```
 */
internal class AutoRetry private constructor(
    private val maxRetryCount: Int,
    private val retryDelay: Duration
) {
    companion object Feature : HttpClientFeature<Config, AutoRetry> {
        private const val DEFAULT_RETRY_COUNT = 2
        private val DEFAULT_RETRY_DELAY = 5.seconds

        override val key: AttributeKey<AutoRetry> = AttributeKey("AutoRetry")

        override fun prepare(block: Config.() -> Unit): AutoRetry {
            val featureConfig = Config(
                maxRetryCount = DEFAULT_RETRY_COUNT,
                retryDelay = DEFAULT_RETRY_DELAY
            ).apply(block)

            require(featureConfig.maxRetryCount >= 0)
            require(featureConfig.retryDelay.isPositive())

            return AutoRetry(
                featureConfig.maxRetryCount,
                featureConfig.retryDelay
            )
        }

        @OptIn(KtorExperimentalAPI::class)
        override fun install(feature: AutoRetry, scope: HttpClient) {
            scope[HttpSend].intercept { origin, context ->
                var retries = 0
                var call = origin

                while (origin.response.status.value >= 500 && retries < feature.maxRetryCount) {
                    delay(feature.retryDelay)
                    call = execute(context)
                    retries++
                }

                call
            }
        }
    }

    class Config(
        /**
         * Maximum number of times a request that failed should be retried.
         * Should be a positive number or zero.
         *
         * Defaults to `2`.
         */
        var maxRetryCount: Int,
        /**
         * Duration to wait after a request failed before sending it again.
         * Should be a positive duration.
         *
         * Defaults to `5 seconds`.
         */
        var retryDelay: Duration
    )
}

