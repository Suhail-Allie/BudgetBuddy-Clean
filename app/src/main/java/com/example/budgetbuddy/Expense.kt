package com.example.budgetbuddy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String = "",
    val amount: Double,
    val category: String,
    val type: String = "expense", // "income" or "expense"
    val date: Long,
    val receiptPhotoPath: String? = null
)