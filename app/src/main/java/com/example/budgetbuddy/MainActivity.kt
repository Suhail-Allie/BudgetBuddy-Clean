package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tvWelcomeUser: TextView
    private lateinit var tvBalance: TextView
    private lateinit var tvGoals: TextView
    private lateinit var tvCategoryCount: TextView
    private lateinit var tvTransactions: TextView

    private lateinit var etAmount: EditText
    private lateinit var btnAddIncome: Button
    private lateinit var btnAddExpense: Button
    private lateinit var btnClearHistory: Button
    private lateinit var btnLogout: Button

    private lateinit var btnManageCategories: Button
    private lateinit var btnBudgetGoals: Button
    private lateinit var btnFilter: Button
    private lateinit var btnViewReports: Button

    private var balance = 0.0
    private val transactionList = mutableListOf<String>()

    private lateinit var dao: ExpenseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser)
        tvBalance = findViewById(R.id.tvBalance)
        tvGoals = findViewById(R.id.tvGoals)
        tvCategoryCount = findViewById(R.id.tvCategoryCount)
        tvTransactions = findViewById(R.id.tvTransactions)

        etAmount = findViewById(R.id.etAmount)
        btnAddIncome = findViewById(R.id.btnAddIncome)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnClearHistory = findViewById(R.id.btnClearHistory)
        btnLogout = findViewById(R.id.btnLogout)

        btnManageCategories = findViewById(R.id.btnManageCategories)
        btnBudgetGoals = findViewById(R.id.btnBudgetGoals)
        btnFilter = findViewById(R.id.btnFilter)
        btnViewReports = findViewById(R.id.btnViewReports)

        // Database
        dao = AppDatabase.getDatabase(this).expenseDao()

        loadUserData()
        updateBalanceDisplay()

        // ✅ ADD INCOME
        btnAddIncome.setOnClickListener {
            val amount = getAmountInput() ?: return@setOnClickListener

            balance += amount
            transactionList.add("Income: +R$amount")

            saveExpense(amount, "Income")

            updateUI()
            Log.d("Main", "Income added: $amount")
        }

        // ❌ ADD EXPENSE
        btnAddExpense.setOnClickListener {
            val amount = getAmountInput() ?: return@setOnClickListener

            balance -= amount
            transactionList.add("Expense: -R$amount")

            saveExpense(amount, "General")

            updateUI()
            Log.d("Main", "Expense added: $amount")
        }

        // 🧹 CLEAR HISTORY
        btnClearHistory.setOnClickListener {
            transactionList.clear()
            tvTransactions.text = "No transactions yet"
            Log.d("Main", "Transaction history cleared")
        }

        // 🚪 LOGOUT
        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 📂 NAVIGATION
        btnManageCategories.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        btnBudgetGoals.setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }

        btnFilter.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        btnViewReports.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
    }

    // =========================
    // HELPERS
    // =========================

    private fun getAmountInput(): Double? {
        val input = etAmount.text.toString().trim()

        if (input.isEmpty()) {
            etAmount.error = "Enter amount"
            return null
        }

        return try {
            input.toDouble()
        } catch (e: Exception) {
            etAmount.error = "Invalid number"
            null
        }
    }

    private fun updateUI() {
        updateBalanceDisplay()
        tvTransactions.text = transactionList.joinToString("\n")
        etAmount.text.clear()
    }

    private fun updateBalanceDisplay() {
        tvBalance.text = "Balance: R%.2f".format(balance)
    }

    private fun loadUserData() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val username = sharedPref.getString("username", "User")

        tvWelcomeUser.text = "Welcome, $username"

        val min = sharedPref.getString("minGoal", "Not set")
        val max = sharedPref.getString("maxGoal", "Not set")

        tvGoals.text = "Goals: Min R$min | Max R$max"

        val categories = sharedPref.getStringSet("categories", emptySet())
        tvCategoryCount.text = "Categories: ${categories?.size ?: 0}"
    }

    private fun saveExpense(amount: Double, category: String) {
        lifecycleScope.launch {
            dao.insertExpense(
                Expense(
                    amount = amount,
                    category = category,
                    date = System.currentTimeMillis()
                )
            )
        }
    }
}