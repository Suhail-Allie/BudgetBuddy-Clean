package com.example.budgetbuddy

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getExpensesBetweenDates(start: Long, end: Long): Flow<List<Expense>>

    @Query("""
        SELECT category, SUM(amount) as total 
        FROM expenses 
        GROUP BY category
    """)
    fun getTotalsPerCategory(): Flow<List<CategoryTotal>>

    @Query("""
        SELECT category, SUM(amount) as total 
        FROM expenses 
        WHERE date BETWEEN :start AND :end
        GROUP BY category
    """)
    fun getTotalsPerCategoryBetweenDates(start: Long, end: Long): Flow<List<CategoryTotal>>

    @Query("DELETE FROM expenses")
    suspend fun clearAll()
}