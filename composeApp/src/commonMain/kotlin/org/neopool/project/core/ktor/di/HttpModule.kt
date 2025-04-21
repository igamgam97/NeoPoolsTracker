package org.neopool.project.core.ktor.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.neopool.project.core.ktor.KtorHttpClient

val httpModule = module {
    single { Json { isLenient = true; ignoreUnknownKeys = true } }
    single {
        KtorHttpClient.httpClient()
    }
}