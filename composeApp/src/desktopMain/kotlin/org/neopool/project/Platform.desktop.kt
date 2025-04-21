@file:Suppress("MatchingDeclarationName")

package org.neopool.project

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()