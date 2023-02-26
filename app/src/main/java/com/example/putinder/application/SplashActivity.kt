package com.example.putinder.application

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.putinder.query_preferences.QueryPreferences
import com.example.putinder.R
import com.example.content_screen.ContentActivity

@SuppressLint("CustomSplashScreen")
@Suppress ( "DEPRECATION" )
class SplashActivity : AppCompatActivity() {

    private val signViewModel: com.example.sign_screen.view_model.SignViewModel by lazy {
        ViewModelProvider(this)[com.example.sign_screen.view_model.SignViewModel::class.java]
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
                val intent = com.example.sign_screen.view.MainActivity.newIntent(this)
                startActivity(intent)
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out)
                finish()
            }
        }

    }
}