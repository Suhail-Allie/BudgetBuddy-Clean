package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcomeUser: TextView
    private lateinit var tvBalance: TextView
    private lateinit var tvGoals: TextView
    private lateinit var tvCategoryCount: TextView
    private lateinit var etAmount: EditText
    private lateinit var tvTransactions: TextView

    private var balance = 0.0
    private var transactionHistory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

        tvWelcomeUser = findViewById(R.id.tvWelcomeUser)
        tvBalance = findViewById(R.id.tvBalance)
        tvGoals = findViewById(R.id.tvGoals)
        tvCategoryCount = findViewById(R.id.tvCategoryCount)
        etAmount = findViewById(R.id.etAmount)
        tvTransactions = findViewById(R.id.tvTransactions)

        val btnIncome = findViewById<Button>(R.id.btnAddIncome)
        val btnExpense = findViewById<Button>(R.id.btnAddExpense)
        val btnCategories = findViewById<Button>(R.id.btnManageCategories)
        val btnGoals = findViewById<Button>(R.id.btnBudgetGoals)
        val btnClear = findViewById<Button>(R.id.btnClearHistory)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val username = sharedPref.getString("username", "User")
        tvWelcomeUser.text = "Welcome, $username"

        balance = sharedPref.getFloat("balance", 0f).toDouble()
        transactionHistory = sharedPref.getString("transactions", "") ?: ""

        updateBalance()
        updateTransactions()

        // 🔥 LOAD GOALS
        val min = sharedPref.getFloat("minGoal", 0f)
        val max = sharedPref.getFloat("maxGoal", 0f)

        if (min == 0f && max == 0f) {
            tvGoals.text = "Goals: Not set"
        } else {
            tvGoals.text = "Goals: R%.2f - R%.2f".format(min, max)
        }

        // 🔥 CATEGORY COUNT
        val categories = sharedPref.getString("categories", "") ?: ""
        val count = if (categories.isEmpty()) 0 else categories.split("\n").size
        tvCategoryCount.text = "Categories: $count"

        btnIncome.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            balance += amount
            transactionHistory += "\n+ R%.2f".format(amount)
            saveData()
            updateBalance()
            updateTransactions()
            etAmount.text.clear()
        }

        btnExpense.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            balance -= amount
            transactionHistory += "\n- R%.2f".format(amount)
            saveData()
            updateBalance()
            updateTransactions()
            etAmount.text.clear()
        }

        btnCategories.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        btnGoals.setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }

        btnClear.setOnClickListener {
            transactionHistory = ""
            saveData()
            updateTransactions()
        }

        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun updateBalance() {
        tvBalance.text = "Balance: R%.2f".format(balance)
    }

    private fun updateTransactions() {
        tvTransactions.text = if (transactionHistory.isEmpty()) {
            "No transactions yet"
        } else transactionHistory
    }

    private fun saveData() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putFloat("balance", balance.toFloat())
        editor.putString("transactions", transactionHistory)
        editor.apply()
    }
}