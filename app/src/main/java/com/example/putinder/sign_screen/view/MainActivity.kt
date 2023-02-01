package com.example.putinder.sign_screen.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.putinder.R

class MainActivity : AppCompatActivity() {

    private val signInFragment = SignInFragment.newInstance()
    private val signUpFragment = SignUpFragment.newInstance()
    private var activeFragment: Fragment = signInFragment

    private lateinit var signButton: Button
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signButton = findViewById(R.id.sign_text_view)
        signUpButton = findViewById(R.id.sign_up_text_view)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, signInFragment)
                .add(R.id.main_container, signUpFragment)
                .hide(signUpFragment)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        signButton.setOnClickListener {
            signUpButton.setTextColor(resources.getColor(R.color.gray))
            signButton.setTextColor(resources.getColor(R.color.white))
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(signInFragment)
                .commit()
            activeFragment = signInFragment
        }

        signUpButton.setOnClickListener {
            signUpButton.setTextColor(resources.getColor(R.color.white))
            signButton.setTextColor(resources.getColor(R.color.gray))
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(signUpFragment)
                .commit()
            activeFragment = signUpFragment
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}