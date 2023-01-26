package com.example.putinder.sign_screen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.putinder.QueryPreferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.activity.ContentActivity
import com.example.putinder.sign_screen.view_model.SignViewModel

class MainActivity : AppCompatActivity(), SignUpComposeFragment.Callbacks, SignInComposeFragment.Callbacks {

    private val signViewModel: SignViewModel by lazy {
        ViewModelProvider(this)[SignViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        if (currentFragment == null) {
            val fragment = SignInComposeFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, fragment)
                .commit()
        }

        val token = QueryPreferences.getStoredToken(this)
        signViewModel.checkToken(token) {
            if (it) {
                val intent = ContentActivity.newIntent(this)
                startActivity(intent)
            }
        }
    }

    override fun onAuthPressed() {
        val fragment = SignInComposeFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    override fun onRegPressed() {
        val fragment = SignUpComposeFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

}