package com.example.putinder.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chats.view.ChatsFragment
import com.example.create_chat.view.CreateChatFragment
import com.example.data.query_preferences.StoreUserTokenAndId
import com.example.profile.view.ProfileFragment
import com.example.sign_screen.view.SignFragment
import com.example.swipes.view.PlaceCardFragment
import com.example.swipes.view.SwipesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialContainerTransform
import com.example.ui.R
import com.example.yandex_map.MapFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentFragment = ContentFragment.newInstance()
        val signFragment = SignFragment.newInstance()

        exitFromAcc()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.exit_icon -> exitFromAcc()
        }

        return true
    }

    private fun routeToContent() {
        val fragment = ContentFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_container, fragment)
            .commit()
    }

    private fun exitFromAcc() {
        val fragment = SignFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.app_container, fragment)
            .commit()
        StoreUserTokenAndId.setStoredTokenAndId(this, "", "")
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}