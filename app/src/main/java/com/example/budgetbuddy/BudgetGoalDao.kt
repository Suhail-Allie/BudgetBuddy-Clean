package com.example.budgetbuddy

import androidx.room.*

@Dao
interface BudgetGoalDao {

    @Insert
    suspend fun insertBudgetGoal(goal: BudgetGoal): Long

    @Update
    suspend fun updateBudgetGoal(goal: BudgetGoal)

    @Delete
    suspend fun deleteBudgetGoal(goal: BudgetGoal)

    @Query("SELECT * FROM budget_goals WHERE month = :month AND year = :year")
    suspend fun getGoalsForMonth(month: Int, year: Int): List<BudgetGoal>

    @Query("SELECT * FROM budget_goals WHERE categoryName = :categoryName AND month = :month AND year = :year")
    suspend fun getGoalForCategory(categoryName: String, month: Int, year: Int): BudgetGoal?
}
