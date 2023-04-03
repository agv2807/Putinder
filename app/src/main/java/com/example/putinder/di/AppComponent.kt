package com.example.putinder.di

import com.example.navigation_menu.NavigationFragment
import com.example.putinder.activities.MainActivity
import dagger.Component

@Component(modules = [FragmentsProviderModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
}