package com.example.putinder.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chats.view.ChatsFragment
import com.example.create_chat.view.CreateChatFragment
import com.example.data.query_preferences.StoreUserTokenAndId
import com.example.navigation_menu.NavigationFragment
import com.example.profile.view.ProfileFragment
import com.example.putinder.di.DaggerAppComponent
import com.example.sign_screen.view.SignFragment
import com.example.swipes.view.PlaceCardFragment
import com.example.swipes.view.SwipesFragment
import com.example.ui.R
import com.example.yandex_map.MapFragment
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

private const val TOKEN_STATUS = "token_status"

class MainActivity : AppCompatActivity(),
    SwipesFragment.Callbacks,
    ChatsFragment.Callbacks,
    CreateChatFragment.Callbacks,
    MapFragment.Callbacks,
    NavigationFragment.Callbacks {

    @Inject lateinit var chatsFragment: ChatsFragment
    @Inject lateinit var swipesFragment: SwipesFragment
    @Inject lateinit var profileFragment: ProfileFragment
    @Inject lateinit var createChatFragment: CreateChatFragment
    @Inject lateinit var placeCardFragment: PlaceCardFragment
    @Inject lateinit var mapFragment: MapFragment
    @Inject lateinit var navigationFragment: NavigationFragment
    @Inject lateinit var signFragment: SignFragment

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerAppComponent.create().inject(this)

        val tokenStatus = intent.getBooleanExtra(TOKEN_STATUS, false)

        if (tokenStatus) {
            routeToSign()
        } else {
            routeToContent()
        }
    }

    override fun onStart() {
        super.onStart()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(currentFragment) {
                    createChatFragment -> removeFragment(createChatFragment, chatsFragment)
//                    mapFragment -> deleteMapFragment()
//                    placeCardFragment -> deletePlaceCardFragment()
                    else -> onBackPressed()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.exit_icon -> routeToSign()
        }

        return true
    }

    private fun routeToContent() {
        val appContainer = R.id.app_container
        supportFragmentManager.beginTransaction()
            .add(R.id.menu_container, navigationFragment)
            .add(appContainer, swipesFragment)
            .hide(swipesFragment)
            .add(appContainer, chatsFragment)
            .hide(chatsFragment)
            .add(appContainer, profileFragment)
            .commit()
        currentFragment = profileFragment
    }

    private fun routeToSign() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_container, signFragment)
            .detach(navigationFragment)
            .commit()
        StoreUserTokenAndId.setStoredTokenAndId(this, "", "")
    }

    private fun routeToSwipes() {
        changeFragmentWithBottomMenu(swipesFragment)
    }

    private fun routeToProfile() {
        changeFragmentWithBottomMenu(profileFragment)
    }

    private fun routeToChats() {
        changeFragmentWithBottomMenu(chatsFragment)
    }

    private fun changeFragmentWithBottomMenu(fragment: Fragment) {
        if (navigationFragment.isHidden) {
            supportFragmentManager.beginTransaction()
                .show(navigationFragment)
                .commit()
        }
        supportFragmentManager.beginTransaction()
            .show(fragment)
            .hide(currentFragment!!)
            .commit()
        currentFragment = fragment
    }

    private fun changeFragmentWithoutBottomMenu(fragment: Fragment) {
        if (navigationFragment.isVisible) {
            supportFragmentManager.beginTransaction()
                .hide(navigationFragment)
                .commit()
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.app_container, fragment)
            .hide(currentFragment!!)
            .commit()
        currentFragment = fragment
    }

    private fun removeFragment(fragment: Fragment, lastFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
        changeFragmentWithBottomMenu(lastFragment)
    }

    override fun onNewChatPressed() {
        changeFragmentWithoutBottomMenu(createChatFragment)
    }

    override fun onNewChatCreated() {
        TODO("Not yet implemented")
    }

    override fun onFabPressed() {
        TODO("Not yet implemented")
    }

    override fun onOpenCardButtonPressed(view: View) {
        TODO("Not yet implemented")
    }

    override fun onMapClosed() {
        TODO("Not yet implemented")
    }

    override fun onSwipesPressed() {
        routeToSwipes()
    }

    override fun onChatsPressed() {
        routeToChats()
    }

    override fun onProfilePressed() {
        routeToProfile()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}