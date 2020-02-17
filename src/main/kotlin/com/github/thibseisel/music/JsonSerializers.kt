package com.github.thibseisel.music

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.http.HttpStatus

/**
 * A custom JSON serializer that maps constants from [HttpStatus] to their equivalent HTTP status code.
 */
class HttpStatusSerializer : JsonSerializer<HttpStatus>() {

    override fun serialize(value: HttpStatus, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeNumber(value.value())
    }
}