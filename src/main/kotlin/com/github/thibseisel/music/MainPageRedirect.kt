package com.github.thibseisel.music

import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

/**
 * Configures Spring to redirect requests from the app root (`/`) to `/index.html`.
 *
 * When serving static content, Spring correctly serves files stored in `src/main/resources/static`
 * (or `src/main/resources/public`) but strangely does not automatically serve `index.html` when requesting `/`.
 */
@Component
class MainPageRedirect : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        if ("/" == exchange.request.uri.path) {
            val redirectedRequest = exchange.request.mutate().path("/index.html").build()
            val redirectedExchange = exchange.mutate().request(redirectedRequest).build()
            return chain.filter(redirectedExchange)
        }

        return chain.filter(exchange)
    }
}