package com.example.putinder.di

import com.example.chats.view.ChatsFragment
import com.example.create_chat.view.CreateChatFragment
import com.example.profile.view.ProfileFragment
import com.example.swipes.view.PlaceCardFragment
import com.example.swipes.view.SwipesFragment
import com.example.yandex_map.MapFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentsProviderModule {

    @Provides
    fun provideProfileFragment() = ProfileFragment.newInstance()

    @Provides
    fun provideChatsFragment() = ChatsFragment.newInstance()

    @Provides
    fun provideCreateChatFragment() = CreateChatFragment.newInstance()

    @Provides
    fun providePlaceCardFragment() = PlaceCardFragment.newInstance()

    @Provides
    fun provideMapFragment() = MapFragment.newInstance()

}