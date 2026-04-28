package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class FilterActivity : AppCompatActivity() {

    private lateinit var dpStart: DatePicker
    private lateinit var dpEnd: DatePicker
    private lateinit var btnApply: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        dpStart = findViewById(R.id.dpStart)
        dpEnd = findViewById(R.id.dpEnd)
        btnApply = findViewById(R.id.btnApplyFilter)

        btnApply.setOnClickListener {

            val startCal = Calendar.getInstance()
            startCal.set(dpStart.year, dpStart.month, dpStart.dayOfMonth, 0, 0)

            val endCal = Calendar.getInstance()
            endCal.set(dpEnd.year, dpEnd.month, dpEnd.dayOfMonth, 23, 59)

            val intent = Intent(this, ExpenseListActivity::class.java)
            intent.putExtra("startDate", startCal.timeInMillis)
            intent.putExtra("endDate", endCal.timeInMillis)

            startActivity(intent)
        }
    }
}