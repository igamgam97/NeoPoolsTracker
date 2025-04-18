package org.planner3d.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform