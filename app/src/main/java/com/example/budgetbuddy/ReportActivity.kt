package com.example.budgetbuddy

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ReportActivity : AppCompatActivity() {

    private lateinit var tvReport: TextView
    private lateinit var dao: ExpenseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        // Using tvCategoryBreakdown from activity_report.xml
        tvReport = findViewById(R.id.tvCategoryBreakdown)
        dao = AppDatabase.getDatabase(this).expenseDao()

        lifecycleScope.launch {
            dao.getTotalsPerCategory().collect { totals ->
                if (totals.isEmpty()) {
                    tvReport.text = "No data available"
                } else {
                    val text = totals.joinToString("\n") {
                        "${it.category}: R${String.format("%.2f", it.total)}"
                    }
                    tvReport.text = text
                }
            }
        }
    }
}