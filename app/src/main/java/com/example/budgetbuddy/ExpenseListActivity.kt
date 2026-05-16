package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseListActivity : AppCompatActivity() {

    private lateinit var tvList: TextView
    private lateinit var btnBackToFilter: Button
    private lateinit var btnBackToDashboard: Button
    private lateinit var dao: ExpenseDao

    private lateinit var currentUserKey: String
    private lateinit var currentDisplayName: String

    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        tvList = findViewById(R.id.tvExpenseList)
        btnBackToFilter = findViewById(R.id.btnBackToFilter)
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard)

        dao = AppDatabase.getDatabase(this).expenseDao()

        currentUserKey = getCurrentUserKey()
        currentDisplayName = getCurrentDisplayName()

        val start = intent.getLongExtra("startDate", 0L)
        val end = intent.getLongExtra("endDate", Long.MAX_VALUE)

        btnBackToFilter.setOnClickListener {
            NavigationUtils.closeScreen(this)
        }

        btnBackToDashboard.setOnClickListener {
            val dashboardIntent = Intent(this, MainActivity::class.java)
            dashboardIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            NavigationUtils.openScreen(this, dashboardIntent)
            finish()
        }

        lifecycleScope.launch {
            dao.getExpensesBetweenDatesForUser(currentUserKey, start, end).collect { list ->
                tvList.text = if (list.isEmpty()) {
                    "No expenses found for $currentDisplayName in this period."
                } else {
                    list.joinToString("\n\n") { expense ->
                        val typeLabel = if (expense.type.equals("income", ignoreCase = true)) {
                            "Income"
                        } else {
                            "Expense"
                        }

                        val receiptText = if (expense.receiptPhotoPath.isNullOrBlank()) {
                            "Receipt: Not attached"
                        } else {
                            "Receipt: Attached"
                        }

                        "$typeLabel: ${expense.title}\n" +
                                "Amount: R${"%.2f".format(expense.amount)}\n" +
                                "Category: ${expense.category}\n" +
                                "Date: ${dateFormatter.format(Date(expense.date))}\n" +
                                receiptText
                    }
                }
            }
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
}