package com.example.putinder.content_screen.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.putinder.R
import com.example.putinder.content_screen.swipes_screen.view.SwipesFragment
import com.example.putinder.content_screen.chats_screen.view.ChatsFragment
import com.example.putinder.content_screen.chats_screen.create_chat_screen.view.CreateChatFragment
import com.example.putinder.content_screen.map_screen.MapFragment
import com.example.putinder.content_screen.profile_screen.view.ProfileFragment
import com.example.putinder.content_screen.swipes_screen.place_card_screen.PlaceCardFragment
import com.example.putinder.query_preferences.QueryPreferences
import com.example.putinder.sign_screen.view.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialContainerTransform

class ContentActivity :
    AppCompatActivity(),
    SwipesFragment.Callbacks,
    ChatsFragment.Callbacks,
    CreateChatFragment.Callbacks,
    MapFragment.Callbacks {

    private val chatsFragment = ChatsFragment.newInstance()
    private val swipesFragment = SwipesFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()
    private var createChatFragment = CreateChatFragment.newInstance()
    private val placeCardFragment = PlaceCardFragment.newInstance()
    private val mapFragment = MapFragment.newInstance()
    private var activeFragment: Fragment = profileFragment

    private lateinit var bottomMenu: BottomNavigationView

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        currentFragment = supportFragmentManager.findFragmentById(R.id.container)

        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, profileFragment)
                .add(R.id.container, swipesFragment)
                .hide(swipesFragment)
                .add(R.id.container, chatsFragment)
                .hide(chatsFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, currentFragment!!)
                .commit()
        }

        bottomMenu = findViewById(R.id.bottom_menu)
        bottomMenu.selectedItemId = R.id.profile_item
        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.swipes_item -> if (it.itemId != bottomMenu.selectedItemId) routeToSwipes()
                R.id.profile_item -> if (it.itemId != bottomMenu.selectedItemId) routeToProfile()
                R.id.chats_item -> if (it.itemId != bottomMenu.selectedItemId) routeToChats()
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
//            R.id.add_icon -> routeToAddPlaceFragment()
            //R.id.add_icon -> onNewChatPressed()
        }

        return true
    }

    override fun onStart() {
        super.onStart()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(activeFragment) {
                    createChatFragment -> deleteCreateChatFragment()
                    mapFragment -> deleteMapFragment()
                    else -> finish()
                }
            }

        })
    }

    private fun routeToSwipes() {
        if (activeFragment != swipesFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(swipesFragment)
                .commit()
            activeFragment = swipesFragment
        }

    }

    private fun routeToProfile() {
        if (activeFragment != profileFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(profileFragment)
                .commit()
            activeFragment = profileFragment
        }
    }

    private fun routeToChats() {
        if (activeFragment != chatsFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(chatsFragment)
                .commit()
            activeFragment = chatsFragment
        }
    }

    private fun routeToCreateChat() {
        createChatFragment = CreateChatFragment.newInstance()
        bottomMenu.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .add(R.id.container, createChatFragment)
            .hide(activeFragment)
            .commit()
        activeFragment = createChatFragment
    }

    private fun deleteCreateChatFragment() {
        bottomMenu.visibility = View.VISIBLE
        activeFragment = chatsFragment
        supportFragmentManager.beginTransaction()
            .remove(createChatFragment)
            .show(activeFragment)
            .commit()
    }

    private fun routeToPlaceCardFragment(view: View) {
        placeCardFragment.sharedElementEnterTransition = MaterialContainerTransform()
        supportFragmentManager.beginTransaction()
            .addSharedElement(view, "shared_element_container")
            .add(R.id.container, placeCardFragment)
            .hide(activeFragment)
            .commit()
        activeFragment = placeCardFragment
    }

    private fun routeToAddPlaceFragment() {

    }

    private fun deleteMapFragment() {
        bottomMenu.visibility = View.VISIBLE
        activeFragment = swipesFragment
        supportFragmentManager.beginTransaction()
            .remove(mapFragment)
            .show(activeFragment)
            .commit()
    }

    override fun onFabPressed() {
        bottomMenu.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .add(R.id.container, mapFragment)
            .hide(activeFragment)
            .addToBackStack("MapFragment")
            .commit()
        activeFragment = mapFragment
    }



    override fun onOpenCardButtonPressed(view: View) {
        routeToPlaceCardFragment(view)
    }

    override fun onNewChatPressed() {
        routeToCreateChat()
    }

    override fun onNewChatCreated() {
        deleteCreateChatFragment()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ContentActivity::class.java)
        }
    }

    override fun onMapClosed() {
        deleteMapFragment()
    }

}