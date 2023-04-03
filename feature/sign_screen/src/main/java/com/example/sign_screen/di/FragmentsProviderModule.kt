package com.example.sign_screen.di

import com.example.sign_screen.view.SignInFragment
import com.example.sign_screen.view.SignUpFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentsProviderModule {

    @Provides
    fun provideSignInFragment() = SignInFragment.newInstance()

    @Provides
    fun provideSignUpFragment() = SignUpFragment.newInstance()
}