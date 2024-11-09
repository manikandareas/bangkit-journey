package com.manikandareas.stories.di

import com.manikandareas.stories.core.data.networking.HttpClientFactory
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

val networkModule = module{
    single { HttpClientFactory.create(engine = CIO.create()) }
}