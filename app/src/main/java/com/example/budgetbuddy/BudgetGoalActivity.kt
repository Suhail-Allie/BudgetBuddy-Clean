package com.example.budgetbuddy

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class BudgetGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goal)

        val etMin = findViewById<EditText>(R.id.etMinGoal)
        val etMax = findViewById<EditText>(R.id.etMaxGoal)
        val btnSave = findViewById<Button>(R.id.btnSaveGoals)
        val tvGoals = findViewById<TextView>(R.id.tvGoals)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        // Load existing values
        val min = sharedPref.getFloat("minGoal", 0f)
        val max = sharedPref.getFloat("maxGoal", 0f)

        if (min == 0f && max == 0f) {
            tvGoals.text = "No goals saved"
        } else {
            tvGoals.text = "Min: R%.2f | Max: R%.2f".format(min, max)
        }

        btnSave.setOnClickListener {
            val minVal = etMin.text.toString().toFloatOrNull()
            val maxVal = etMax.text.toString().toFloatOrNull()

            if (minVal == null || minVal <= 0) {
                etMin.error = "Enter valid minimum"
                return@setOnClickListener
            }

            if (maxVal == null || maxVal <= 0) {
                etMax.error = "Enter valid maximum"
                return@setOnClickListener
            }

            if (minVal > maxVal) {
                Toast.makeText(this, "Min cannot be greater than Max", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPref.edit()
                .putFloat("minGoal", minVal)
                .putFloat("maxGoal", maxVal)
                .apply()

            tvGoals.text = "Min: R%.2f | Max: R%.2f".format(minVal, maxVal)
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}