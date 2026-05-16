package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNewUsername: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvBackToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNewUsername = findViewById(R.id.etNewUsername)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)

        btnRegister.setOnClickListener {
            val username = etNewUsername.text.toString().trim()
            val password = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (username.isEmpty()) {
                etNewUsername.error = "Please enter a username"
                etNewUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etNewPassword.error = "Please enter a password"
                etNewPassword.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                etConfirmPassword.error = "Please confirm your password"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                etConfirmPassword.error = "Passwords do not match"
                etConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            val userKey = username.lowercase()
            val accountPrefs = getSharedPreferences("BudgetBuddyAccounts", MODE_PRIVATE)

            if (accountPrefs.contains("password_$userKey")) {
                Toast.makeText(this, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show()
                Log.d("Register", "Duplicate username blocked: $username")
                return@setOnClickListener
            }

            accountPrefs.edit()
                .putString("password_$userKey", password)
                .putString("displayName_$userKey", username)
                .apply()

            getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
                .edit()
                .clear()
                .apply()

            Toast.makeText(this, "Registration successful. Please login.", Toast.LENGTH_SHORT).show()
            Log.d("Register", "Registered user: userKey=$userKey displayName=$username")

            NavigationUtils.openScreen(this, Intent(this, LoginActivity::class.java))
            finish()
        }

        tvBackToLogin.setOnClickListener {
            NavigationUtils.openScreen(this, Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}