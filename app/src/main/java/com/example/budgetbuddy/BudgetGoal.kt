package com.example.budgetbuddy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_goals")
data class BudgetGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryName: String,
    val monthlyBudget: Double,
    val month: Int, // 1-12
    val year: Int
)
