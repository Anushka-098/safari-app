package com.example.safariapp

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(3000)
            // Start the main activity after the delay
            val intent = Intent(this@SplashActivity, SignInActivity::class.java)
            startActivity(intent)
            finish() // onDestroy() call ho rha hai
        }
    }
}