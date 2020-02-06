package com.github.thibseisel.music

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class SpotifyClientConfig {

    private companion object {
        const val SPOTIFY_BASE_URL = "https://api.spotify.com/v1"
    }

    @Bean
    fun configure(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.oauth2Client(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun providesSpotifyClient(
        clientRegistrations: ReactiveClientRegistrationRepository,
        authorizedClients: ServerOAuth2AuthorizedClientRepository
    ): WebClient {
        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients)
        oauth.setDefaultOAuth2AuthorizedClient(true)
        oauth.setDefaultClientRegistrationId("spotify")

        return WebClient.builder()
            .baseUrl(SPOTIFY_BASE_URL)
            .filter(oauth)
            .build()
    }
}