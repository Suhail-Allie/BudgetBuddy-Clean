package com.example.budgetbuddy

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ExpenseListActivity : AppCompatActivity() {

    private lateinit var tvList: TextView
    private lateinit var dao: ExpenseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        tvList = findViewById(R.id.tvExpenseList)
        dao = AppDatabase.getDatabase(this).expenseDao()

        val start = intent.getLongExtra("startDate", 0)
        val end = intent.getLongExtra("endDate", Long.MAX_VALUE)

        lifecycleScope.launch {
            dao.getExpensesBetweenDates(start, end).collect { list ->
                if (list.isEmpty()) {
                    tvList.text = "No expenses found"
                } else {
                    val text = list.joinToString("\n") {
                        "R${it.amount} - ${it.category}"
                    }
                    tvList.text = text
                }
            }
        }
    }
}