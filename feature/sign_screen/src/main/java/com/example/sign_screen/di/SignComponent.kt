package com.example.sign_screen.di

import com.example.sign_screen.view.SignFragment
import com.example.sign_screen.view.SignInFragment
import com.example.sign_screen.view.SignUpFragment
import com.example.sign_screen.view_model.SignViewModel
import dagger.Component

@Component(modules = [
    FragmentsProviderModule::class
])
interface SignComponent {

    fun inject(fragment: SignFragment)
    fun inject(fragment: SignInFragment)
    fun inject(viewModel: SignViewModel)
}