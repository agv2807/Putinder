package com.example.putinder.content_screen.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.putinder.R
import com.example.putinder.content_screen.swipes_screen.view.SwipesFragment
import com.example.putinder.content_screen.chats_screen.view.ChatsFragment
import com.example.putinder.content_screen.chats_screen.create_chat_screen.view.CreateChatFragment
import com.example.putinder.content_screen.map_screen.MapFragment
import com.example.putinder.content_screen.profile_screen.view.ProfileFragment
import com.example.putinder.content_screen.swipes_screen.place_card_screen.PlaceCardFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialContainerTransform

class ContentActivity :
    AppCompatActivity(),
    SwipesFragment.Callbacks,
    ChatsFragment.Callbacks,
    CreateChatFragment.Callbacks {

    private lateinit var bottomMenu: BottomNavigationView

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        currentFragment = supportFragmentManager.findFragmentById(R.id.container)

        if (currentFragment == null) {
            val fragment = ProfileFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, fragment)
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
            R.id.add_icon -> routeToAddPlaceFragment()
        }

        return true
    }

    private fun routeToSwipes() {
        val fragment = SwipesFragment.newInstance()
        if (currentFragment != fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }

    }

    private fun routeToProfile() {
        val fragment = ProfileFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun routeToChats() {
        val fragment = ChatsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun routeToCreateChat() {
        val fragment = CreateChatFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack("CreateChatFragment")
            .commit()
    }

    private fun deleteFragment() {
        val fragment = ChatsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun routeToPlaceCardFragment(view: View) {
        val fragment = PlaceCardFragment.newInstance()
        fragment.sharedElementEnterTransition = MaterialContainerTransform()
        supportFragmentManager.beginTransaction()
            .addSharedElement(view, "shared_element_container")
            .replace(R.id.container, fragment)
            .addToBackStack("PlaceCardFragment")
            .commit()
    }

    private fun routeToAddPlaceFragment() {

    }

    override fun onFabPressed() {
        val fragment = MapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack("MapFragment")
            .commit()
    }

    override fun onOpenCardButtonPressed(view: View) {
        routeToPlaceCardFragment(view)
    }

    override fun onNewChatPressed() {
        routeToCreateChat()
    }

    override fun onNewChatCreated() {
        deleteFragment()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ContentActivity::class.java)
        }
    }
}