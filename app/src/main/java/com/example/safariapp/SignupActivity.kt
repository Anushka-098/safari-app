package com.example.safariapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var signUpButton : Button
    lateinit var progressBar : ProgressBar

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        email=findViewById(R.id.emailEditText)
        password=findViewById(R.id.passwordEditText)

        signUpButton=findViewById(R.id.signUpButton)

        progressBar=findViewById(R.id.progressBar)

        signUpButton.setOnClickListener {
            val email = email.text.toString()
            val password =password.text.toString()

            signUpWithFirebase(email,password)

        }
        signUpButton.setOnClickListener {
            val email = email.text.toString().trim()
            val password =password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signUpWithFirebase(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun signUpWithFirebase(email:String, password:String)
    {
        progressBar.visibility= View.VISIBLE

        signUpButton.isClickable=false

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->

            if(task.isSuccessful)
            {
                Toast.makeText(applicationContext,"Your account has been created", Toast.LENGTH_LONG).show()
                progressBar.visibility= View.INVISIBLE
                signUpButton.isClickable=true
                finish()

            }
            else
            {
                Toast.makeText(applicationContext,task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
