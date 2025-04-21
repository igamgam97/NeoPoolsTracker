package org.neopool.project.di

import org.koin.dsl.module
import org.neopool.project.core.ktor.di.httpModule
import org.neopool.project.poolreward.di.poolModule

fun appModule() = module {
    includes(httpModule, poolModule)
}