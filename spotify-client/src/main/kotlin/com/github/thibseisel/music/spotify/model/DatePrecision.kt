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

package com.github.thibseisel.music.spotify.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The precision with which a related date is known.
 */
@Serializable
enum class DatePrecision {
    /**
     * Only the year is known.
     *
     * Format: `YYYY`.
     */
    @SerialName("year")
    YEAR,

    /**
     * The related date is precise to the month.
     *
     * Format: `YYYY-MM`.
     */
    @SerialName("month")
    MONTH,

    /**
     * The related date is precise to the day.
     *
     * Format: `YYYY-MM-DD`.
     */
    @SerialName("day")
    DAY,
}
