package com.example.budgetbuddy

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    /*
     * Final app should use these user-specific methods.
     */

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC")
    fun getExpensesForUser(userId: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND date BETWEEN :start AND :end ORDER BY date DESC")
    fun getExpensesBetweenDatesForUser(
        userId: String,
        start: Long,
        end: Long
    ): Flow<List<Expense>>

    @Query("""
        SELECT category, SUM(amount) AS total
        FROM expenses
        WHERE userId = :userId AND type != 'income'
        GROUP BY category
    """)
    fun getTotalsPerCategoryForUser(userId: String): Flow<List<CategoryTotal>>

    @Query("""
        SELECT category, SUM(amount) AS total
        FROM expenses
        WHERE userId = :userId 
        AND type != 'income'
        AND date BETWEEN :start AND :end
        GROUP BY category
    """)
    fun getTotalsPerCategoryBetweenDatesForUser(
        userId: String,
        start: Long,
        end: Long
    ): Flow<List<CategoryTotal>>

    @Query("DELETE FROM expenses WHERE userId = :userId")
    suspend fun clearExpensesForUser(userId: String)

    /*
     * Older methods are kept so older tests or older screens do not break,
     * but MainActivity, ReportActivity and ExpenseListActivity now use the user-specific methods above.
     */

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getExpensesBetweenDates(start: Long, end: Long): Flow<List<Expense>>

    @Query("""
        SELECT category, SUM(amount) AS total
        FROM expenses
        WHERE type != 'income'
        GROUP BY category
    """)
    fun getTotalsPerCategory(): Flow<List<CategoryTotal>>

    @Query("""
        SELECT category, SUM(amount) AS total
        FROM expenses
        WHERE type != 'income'
        AND date BETWEEN :start AND :end
        GROUP BY category
    """)
    fun getTotalsPerCategoryBetweenDates(start: Long, end: Long): Flow<List<CategoryTotal>>

    @Query("DELETE FROM expenses")
    suspend fun clearAll()
}