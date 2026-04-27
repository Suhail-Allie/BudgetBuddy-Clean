package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        // Handle login button click
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validate username input
            if (username.isEmpty()) {
                etUsername.error = "Please enter username"
                etUsername.requestFocus()
                Log.d("Login", "Login failed: username empty")
                return@setOnClickListener
            }

            // Validate password input
            if (password.isEmpty()) {
                etPassword.error = "Please enter password"
                etPassword.requestFocus()
                Log.d("Login", "Login failed: password empty")
                return@setOnClickListener
            }

            // Retrieve saved user credentials
            val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
            val savedUsername = sharedPref.getString("username", null)
            val savedPassword = sharedPref.getString("password", null)

            if (savedUsername == null || savedPassword == null) {
                Toast.makeText(this, "No user registered yet", Toast.LENGTH_SHORT).show()
                Log.d("Login", "No registered user found")
                return@setOnClickListener
            }

            // Check credentials
            if (username == savedUsername && password == savedPassword) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                Log.d("Login", "User logged in successfully")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                Log.d("Login", "Invalid login attempt")
            }
        }

        // Navigate to register screen
        tvRegister.setOnClickListener {
            Log.d("Login", "Navigating to RegisterActivity")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}