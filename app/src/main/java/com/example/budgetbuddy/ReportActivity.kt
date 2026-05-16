package com.example.budgetbuddy

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ReportActivity : AppCompatActivity() {

    private lateinit var tvTotalIncome: TextView
    private lateinit var tvTotalExpense: TextView
    private lateinit var tvRemaining: TextView
    private lateinit var tvGoalLines: TextView
    private lateinit var tvCategoryBreakdown: TextView
    private lateinit var tvSpendingGraph: TextView
    private lateinit var btnBackToDashboard: Button

    private lateinit var dao: ExpenseDao

    private lateinit var currentUserKey: String
    private lateinit var currentDisplayName: String
    private lateinit var userPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        tvTotalIncome = findViewById(R.id.tvTotalIncome)
        tvTotalExpense = findViewById(R.id.tvTotalExpense)
        tvRemaining = findViewById(R.id.tvRemaining)
        tvGoalLines = findViewById(R.id.tvGoalLines)
        tvCategoryBreakdown = findViewById(R.id.tvCategoryBreakdown)
        tvSpendingGraph = findViewById(R.id.tvSpendingGraph)
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard)

        currentUserKey = getCurrentUserKey()
        currentDisplayName = getCurrentDisplayName()
        userPrefs = getUserPrefs(currentUserKey)

        dao = AppDatabase.getDatabase(this).expenseDao()

        btnBackToDashboard.setOnClickListener {
            NavigationUtils.closeScreen(this)
        }

        lifecycleScope.launch {
            dao.getExpensesForUser(currentUserKey).collect { expenses ->
                updateReport(expenses)
            }
        }
    }

    private fun updateReport(expenses: List<Expense>) {
        val minGoal = userPrefs.getFloat("minGoal", 0f)
        val maxGoal = userPrefs.getFloat("maxGoal", 0f)

        val totalIncome = expenses
            .filter { it.type.equals("income", ignoreCase = true) }
            .sumOf { it.amount }

        val totalExpense = expenses
            .filter { !it.type.equals("income", ignoreCase = true) }
            .sumOf { it.amount }

        val remaining = totalIncome - totalExpense
        val categoryTotals = BudgetBuddyLogic.spendingByCategory(expenses)

        tvTotalIncome.text = "Total Income: R${formatMoney(totalIncome)}"
        tvTotalExpense.text = "Total Expenses: R${formatMoney(totalExpense)}"
        tvRemaining.text = "Remaining: R${formatMoney(remaining)}"

        tvGoalLines.text = if (minGoal <= 0f && maxGoal <= 0f) {
            "Budget Goals: Not set for $currentDisplayName"
        } else {
            "Budget Goals: Min R${formatMoney(minGoal.toDouble())} | Max R${formatMoney(maxGoal.toDouble())}"
        }

        if (categoryTotals.isEmpty()) {
            tvCategoryBreakdown.text = "No expense data available yet for $currentDisplayName."
            tvSpendingGraph.text = "No graph data available yet."
            return
        }

        tvCategoryBreakdown.text = categoryTotals.entries
            .sortedByDescending { it.value }
            .joinToString("\n") { "${it.key}: R${formatMoney(it.value)}" }

        tvSpendingGraph.text = buildCategoryGraph(categoryTotals, maxGoal)
    }

    private fun buildCategoryGraph(categoryTotals: Map<String, Double>, maxGoal: Float): String {
        val highestAmount = categoryTotals.values.maxOrNull() ?: 0.0

        if (highestAmount <= 0.0) {
            return "No graph data available yet."
        }

        val graph = StringBuilder()

        graph.append("Category Spending Graph\n")
        graph.append("Each bar compares categories against the highest spending category.\n\n")

        categoryTotals.entries
            .sortedByDescending { it.value }
            .forEach { entry ->
                val barLength = ((entry.value / highestAmount) * 20).roundToInt().coerceAtLeast(1)
                val bar = "█".repeat(barLength)
                val warning = if (maxGoal > 0f && entry.value > maxGoal) {
                    "  ⚠ Over max goal"
                } else {
                    ""
                }

                graph.append("${entry.key}\n")
                graph.append("$bar  R${formatMoney(entry.value)}$warning\n\n")
            }

        if (maxGoal > 0f) {
            graph.append("Maximum monthly goal reference: R${formatMoney(maxGoal.toDouble())}")
        }

        return graph.toString()
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

    private fun formatMoney(value: Double): String {
        return "%.2f".format(value)
    }
}