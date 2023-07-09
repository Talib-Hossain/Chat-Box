package com.example.chatbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {

    private lateinit var etEmailLayout: TextInputLayout
    private lateinit var etPasswordLayout: TextInputLayout
    private lateinit var etEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()

        mAuth= FirebaseAuth.getInstance()

        etEmailLayout=findViewById(R.id.etEmailLayout)
        etPasswordLayout=findViewById(R.id.etPasswordLayout)
        etEmail = findViewById(R.id.etEmail)
        edtPassword= findViewById(R.id.etPassword)
        btnLogIn= findViewById(R.id.btnLogIn)
        btnSignUp= findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val intent= Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogIn.setOnClickListener {
            val email= etEmail.text.toString()
            val password= edtPassword.text.toString()

            login(email, password)
        }
    }

    private fun login(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent= Intent(this@LogIn, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    etPasswordLayout.helperText="Wrong Credentials"
                    etEmailLayout.helperText="Wrong Credentials"
                    Toast.makeText(this@LogIn, "Invalid User", Toast.LENGTH_SHORT).show()
                }
            }
    }
}