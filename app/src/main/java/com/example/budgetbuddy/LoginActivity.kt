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

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty()) {
                etUsername.error = "Please enter your username"
                etUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etPassword.error = "Please enter your password"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            val userKey = username.lowercase()
            val accountPrefs = getSharedPreferences("BudgetBuddyAccounts", MODE_PRIVATE)

            val savedPassword = accountPrefs.getString("password_$userKey", null)
            val savedDisplayName = accountPrefs.getString("displayName_$userKey", username)

            if (savedPassword == null) {
                Toast.makeText(this, "User not found. Please register first.", Toast.LENGTH_SHORT).show()
                Log.d("Login", "Login failed. User not found: $username")
                return@setOnClickListener
            }

            if (savedPassword != password) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                Log.d("Login", "Login failed. Incorrect password for: $username")
                return@setOnClickListener
            }

            /*
             * This is the important part:
             * currentUserKey is used for database filtering and per-user preferences.
             * currentUsername is used only for display.
             */
            getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
                .edit()
                .putString("currentUserKey", userKey)
                .putString("currentUsername", savedDisplayName)
                .apply()

            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            Log.d("Login", "Login successful. userKey=$userKey displayName=$savedDisplayName")

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}