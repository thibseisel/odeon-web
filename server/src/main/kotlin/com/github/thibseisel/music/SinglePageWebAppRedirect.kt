package com.github.thibseisel.music

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

private val FILE_EXTENSION = Regex("\\.[a-z]+")

/**
 * Bring support for serving a Single Page Web Application from application resources.
 *
 * This redirects requests so that:
 * - `/` is mapped to `/index.html`
 * - `/{route}` is mapped to `/index.html`, allowing client-side HTML5 routing to work properly
 * - requests to REST API (under `/api`) and static files are served as-is.
 */
@Component
class SinglePageWebAppRedirect : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val requestPath = exchange.request.uri.path
        val pathSegments = requestPath.substringAfter('/').split('/')

        return when {
            pathSegments.isEmpty() -> {
                // Redirect requests to route
                val redirectedRequest = exchange.request.mutate().path("/index.html").build()
                val redirectedExchange = exchange.mutate().request(redirectedRequest).build()
                chain.filter(redirectedExchange)
            }
            pathSegments.first() == "api" -> {
                // Don't redirect API calls.
                chain.filter(exchange)
            }
            else -> {
                val lastSegment = pathSegments.last()
                if (FILE_EXTENSION in lastSegment) {
                    chain.filter(exchange)
                } else {
                    val redirectedRequest = exchange.request.mutate().path("/index.html").build()
                    val redirectedExchange = exchange.mutate().request(redirectedRequest).build()
                    chain.filter(redirectedExchange)
                }
            }
        }
    }
}
