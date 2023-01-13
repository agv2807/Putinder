package com.example.putinder.sign_screen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.putinder.QueryPreferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.activity.ContentActivity
import com.example.putinder.sign_screen.view_model.SignViewModel

class MainActivity : AppCompatActivity(), SignUpFragment.Callbacks, SignInFragment.Callbacks {

    private val signViewModel: SignViewModel by lazy {
        ViewModelProvider(this)[SignViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        if (currentFragment == null) {
            val fragment = SignInFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, fragment)
                .commit()
        }

        val token = QueryPreferences.getStoredToken(this)
        if (signViewModel.checkToken(token)) {
            val intent = ContentActivity.newIntent(this)
            startActivity(intent)
        }
    }

    override fun onAuthPressed() {
        val fragment = SignInFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    override fun onRegPressed() {
        val fragment = SignUpFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

}