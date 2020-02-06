package com.github.thibseisel.music

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.http.HttpStatus

class ErrorPayload(

    @JsonSerialize(using = HttpStatusSerializer::class)
    @JsonProperty("status") val status: HttpStatus,

    @JsonProperty("message") val message: String?
)