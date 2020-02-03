package com.github.thibseisel.music

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OdeonApplication

fun main(args: Array<String>) {
    runApplication<OdeonApplication>(*args)
}
