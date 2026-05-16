package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class FilterActivity : AppCompatActivity() {

    private lateinit var dpStart: DatePicker
    private lateinit var dpEnd: DatePicker
    private lateinit var btnApplyFilter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        dpStart = findViewById(R.id.dpStart)
        dpEnd = findViewById(R.id.dpEnd)
        btnApplyFilter = findViewById(R.id.btnApplyFilter)

        btnApplyFilter.setOnClickListener {
            val startCalendar = Calendar.getInstance()
            startCalendar.set(
                dpStart.year,
                dpStart.month,
                dpStart.dayOfMonth,
                0,
                0,
                0
            )
            startCalendar.set(Calendar.MILLISECOND, 0)

            val endCalendar = Calendar.getInstance()
            endCalendar.set(
                dpEnd.year,
                dpEnd.month,
                dpEnd.dayOfMonth,
                23,
                59,
                59
            )
            endCalendar.set(Calendar.MILLISECOND, 999)

            val intent = Intent(this, ExpenseListActivity::class.java)
            intent.putExtra("startDate", startCalendar.timeInMillis)
            intent.putExtra("endDate", endCalendar.timeInMillis)

            NavigationUtils.openScreen(this, intent)
        }
    }
}