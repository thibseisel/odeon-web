# Odeon Web

Odeon Web allows for displaying audio analysis information from
the [Spotify Web API](https://developer.spotify.com/documentation/web-api/).

Spotify API exposes a lot of useful statistics on how music impacts how we feel.
This app focuses on simplifying analysis of those data using visual graphs. It
also acts as a proxy to request Spotify Web API without needing to create a
developer account.

## Tech stack

Odeon is made of 2 software components:

- A Spring Boot Kotlin back-end REST API,
- An Angular front-end.

## Hosting

The app is currently hosted on [Heroku](https://odeon-web.herokuapp.com). Please
be patient when accessing the website as the server is automatically paused when
not requested for 1 hour and takes about 30 seconds to restart due to being
hosted for free.

## Work in progress

- Redesign website to be responsive. The current UX on small screens is quite
  bad.
- Migrate server code from Spring Boot to Ktor.
- Encapsulate back-end and front-end in Docker containers.
