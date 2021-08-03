package com.karimsinouh.devhub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.features.json.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SingletonsModule {

    @Provides
    @Singleton
    fun ktorClient()= HttpClient {
        install(JsonFeature)
    }

}