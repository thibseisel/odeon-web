package com.github.thibseisel.music

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.http.HttpStatus

class HttpStatusSerializer : JsonSerializer<HttpStatus>() {

    override fun serialize(value: HttpStatus?, gen: JsonGenerator, serializers: SerializerProvider) {
        if (value != null) {
            gen.writeNumber(value.value())
        } else {
            gen.writeNull()
        }
    }
}