package com.example.putinder.application

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.putinder.query_preferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.activity.ContentActivity
import com.example.putinder.sign_screen.view.MainActivity
import com.example.putinder.sign_screen.view_model.SignViewModel

@SuppressLint("CustomSplashScreen")
@Suppress ( "DEPRECATION" )
class SplashActivity : AppCompatActivity() {

    private val signViewModel: SignViewModel by lazy {
        ViewModelProvider(this)[SignViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val token = QueryPreferences.getStoredToken(this)
        signViewModel.checkToken(token) {
            if (it) {
                val intent = ContentActivity.newIntent(this)
                startActivity(intent)
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out)
                finish()
            } else {
                val intent = MainActivity.newIntent(this)
                startActivity(intent)
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out)
                finish()
            }
        }

    }
}