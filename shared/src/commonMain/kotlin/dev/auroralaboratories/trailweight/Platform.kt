package dev.auroralaboratories.trailweight

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform