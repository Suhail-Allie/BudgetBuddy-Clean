package com.example.budgetbuddy

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BudgetGoalActivity : AppCompatActivity() {

    private lateinit var userPrefs: SharedPreferences
    private lateinit var currentUserKey: String
    private lateinit var currentDisplayName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goal)

        val etMin = findViewById<EditText>(R.id.etMinGoal)
        val etMax = findViewById<EditText>(R.id.etMaxGoal)
        val btnSave = findViewById<Button>(R.id.btnSaveGoals)
        val tvGoals = findViewById<TextView>(R.id.tvGoals)
        val btnBack = findViewById<Button>(R.id.btnBack)

        currentUserKey = getCurrentUserKey()
        currentDisplayName = getCurrentDisplayName()
        userPrefs = getUserPrefs(currentUserKey)

        val min = userPrefs.getFloat("minGoal", 0f)
        val max = userPrefs.getFloat("maxGoal", 0f)

        if (min <= 0f && max <= 0f) {
            tvGoals.text = "No goals saved yet for $currentDisplayName"
        } else {
            tvGoals.text = "Min: R%.2f | Max: R%.2f".format(min, max)
            etMin.setText("%.2f".format(min))
            etMax.setText("%.2f".format(max))
        }

        btnSave.setOnClickListener {
            val minVal = etMin.text.toString().trim().toFloatOrNull()
            val maxVal = etMax.text.toString().trim().toFloatOrNull()

            if (minVal == null || minVal <= 0f) {
                etMin.error = "Enter a valid minimum goal"
                etMin.requestFocus()
                return@setOnClickListener
            }

            if (maxVal == null || maxVal <= 0f) {
                etMax.error = "Enter a valid maximum goal"
                etMax.requestFocus()
                return@setOnClickListener
            }

            if (minVal > maxVal) {
                Toast.makeText(this, "Minimum goal cannot be greater than maximum goal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userPrefs.edit()
                .putFloat("minGoal", minVal)
                .putFloat("maxGoal", maxVal)
                .apply()

            tvGoals.text = "Min: R%.2f | Max: R%.2f".format(minVal, maxVal)

            Toast.makeText(this, "Goals saved", Toast.LENGTH_SHORT).show()
            Log.d("BudgetGoal", "Goals saved for userKey=$currentUserKey min=$minVal max=$maxVal")
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getCurrentUserKey(): String {
        val sessionPrefs = getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
        val userKey = sessionPrefs.getString("currentUserKey", null)

        return if (!userKey.isNullOrBlank()) {
            userKey.trim().lowercase()
        } else {
            "guest"
        }
    }

    private fun getCurrentDisplayName(): String {
        val sessionPrefs = getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
        val displayName = sessionPrefs.getString("currentUsername", null)

        return if (!displayName.isNullOrBlank()) {
            displayName.trim()
        } else {
            "Guest"
        }
    }

    private fun getUserPrefs(userKey: String): SharedPreferences {
        return getSharedPreferences("UserData_${userKey.lowercase()}", MODE_PRIVATE)
    }
}