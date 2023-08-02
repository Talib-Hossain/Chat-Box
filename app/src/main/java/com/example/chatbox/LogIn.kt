package com.example.chatbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {

    private lateinit var etEmailLayout: TextInputLayout
    private lateinit var etPasswordLayout: TextInputLayout
    private lateinit var etEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var tvSignUp: TextView
    private lateinit var forgotPasswordTextView: TextView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        etEmailLayout = findViewById(R.id.etEmailLayout)
        etPasswordLayout = findViewById(R.id.etPasswordLayout)
        etEmail = findViewById(R.id.etEmail)
        edtPassword = findViewById(R.id.etPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        tvSignUp = findViewById(R.id.signupText)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)

        tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogIn.setOnClickListener {
            val email = etEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                etEmailLayout.helperText = "Enter a Email"
                etPasswordLayout.helperText = "Enter a Password"
            } else {
                login(email, password)
            }
        }

        forgotPasswordTextView.setOnClickListener {
            val email = etEmail.text.toString()
            if (email.isEmpty()) {
                etEmailLayout.helperText = "Enter a valid email"
                etPasswordLayout.helperText = ""
            } else {
                passwordResetEmail(email)
            }
        }
    }

    private fun passwordResetEmail(email: String) {
        Log.d("Reset", "Email: $email")
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Log.d("Reset", "Error: $exception")
            Toast.makeText(
                this,
                "This email is not registered if you are a new user then please Click Sign Up Button",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LogIn, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    etPasswordLayout.helperText = "Wrong Credentials"
                    etEmailLayout.helperText = "Wrong Credentials"
                    Toast.makeText(this@LogIn, "Invalid User", Toast.LENGTH_SHORT).show()
                }
            }
    }
}