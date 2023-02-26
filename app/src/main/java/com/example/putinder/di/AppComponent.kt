package com.example.putinder.di

import com.example.putinder.activities.ContentFragment
import com.example.putinder.activities.MainActivity
import dagger.Component

@Component(modules = [FragmentsProviderModule::class])
interface AppComponent {

    fun inject(fragment: ContentFragment)
}