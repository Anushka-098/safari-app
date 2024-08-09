package com.example.safariapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()

        // Example: Logout button click listener
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            logOutUser()
        }
    }

    private fun logOutUser() {
        // Sign out the current user
        auth.signOut()

        // Redirect the user to the login screen
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}