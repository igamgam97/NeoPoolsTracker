package org.neopool.project.poolreward.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neopool.project.poolreward.data.repository.PoolRepository
import org.neopool.project.poolreward.data.repository.PoolRepositoryImpl
import org.neopool.project.poolreward.presentation.PoolViewModel

val poolModule = module {
    single<PoolRepository> { PoolRepositoryImpl(get()) }
    viewModelOf(::PoolViewModel)
}