package org.neopool.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform