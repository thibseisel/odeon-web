rootProject.name = "odeon-web"

// Configure alternative plugin repositories from the com.github.thibseisel.mirrors property.
pluginManagement.repositories {
    if (extra.has("com.github.thibseisel.mirrors")) {
        (extra["com.github.thibseisel.mirrors"] as? String)
                ?.split(",")
                ?.forEach { mirrorUrl -> maven(url = mirrorUrl) }
    }
    gradlePluginPortal()
}