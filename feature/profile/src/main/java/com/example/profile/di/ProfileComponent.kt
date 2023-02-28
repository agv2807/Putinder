package com.example.profile.di

import com.example.profile.view_model.ProfileViewModel
import dagger.Component

@Component(modules = [RepositoryProviderModule::class])
interface ProfileComponent {

    fun inject(viewModel: ProfileViewModel)
}