package com.example.putinder.sign_screen.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.putinder.R

class MainActivity : AppCompatActivity(), SignUpFragment.Callbacks, SignInFragment.Callbacks {

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

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}