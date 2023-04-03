package com.example.putinder.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.data.query_preferences.StoreUserTokenAndId
import com.example.sign_screen.view_model.SignViewModel
import com.example.ui.R

private const val TOKEN_STATUS = "token_status"

@SuppressLint("CustomSplashScreen")
@Suppress ( "DEPRECATION" )
class SplashActivity : AppCompatActivity() {

    private val signViewModel: SignViewModel by lazy {
        ViewModelProvider(this)[SignViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val token = StoreUserTokenAndId.getStoredToken(this)
        signViewModel.checkToken(token) {
            val intent = MainActivity.newIntent(this)
            intent.putExtra(TOKEN_STATUS, it)
            startActivity(intent)
            overridePendingTransition(R.transition.fade_in, R.transition.fade_out)
            finish()
        }
    }
}