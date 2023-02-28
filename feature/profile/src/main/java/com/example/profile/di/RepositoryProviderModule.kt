package com.example.profile.di

import com.example.data.profile.ProfileRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryProviderModule {

    @Provides
    fun provideRepository() = ProfileRepository()
}