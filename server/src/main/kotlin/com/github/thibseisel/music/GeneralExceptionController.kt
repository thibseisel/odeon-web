package com.github.thibseisel.music

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * Groups [ExceptionHandler]s that are applied for all API routes.
 */
@RestController
@ControllerAdvice
internal class GeneralExceptionController {

    private val logger = LoggerFactory.getLogger(GeneralExceptionController::class.java)

    /**
     * Handle exceptions that are manually thrown from any API route,
     * responding with an [ErrorPayload] with the status and the reason.
     */
    @ExceptionHandler
    fun handle(httpException: ResponseStatusException): ResponseEntity<ErrorPayload> {
        val errorStatus = httpException.status
        val errorPayload =
            ErrorPayload(errorStatus, httpException.reason ?: errorStatus.reasonPhrase)
        return ResponseEntity.status(errorStatus).body(errorPayload)
    }

    /**
     * Handle unexpected server errors.
     * This responds with a generic error payload.
     */
    @ExceptionHandler
    fun handleUnexpected(serverException: Throwable): ResponseEntity<ErrorPayload> {
        logger.error("An unexpected error occurred", serverException)
        val payload = ErrorPayload(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(payload)
    }
}
