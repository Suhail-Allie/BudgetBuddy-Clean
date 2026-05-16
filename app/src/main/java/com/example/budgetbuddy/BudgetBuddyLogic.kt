package com.example.budgetbuddy

import java.util.Calendar
import kotlin.math.roundToInt

/**
 * Small reusable logic helpers for BudgetBuddy.
 * Keeping these calculations outside Activities makes them easier to test.
 */
object BudgetBuddyLogic {

    data class BudgetItem(
        val amount: Double,
        val category: String,
        val type: String,
        val date: Long
    )

    fun calculateBalance(expenses: List<Expense>): Double {
        return calculateBalanceFromItems(expenses.map { it.toBudgetItem() })
    }

    fun calculateBalanceFromItems(items: List<BudgetItem>): Double {
        val income = items.filter { it.type.equals("income", ignoreCase = true) }.sumOf { it.amount }
        val spending = items.filter { !it.type.equals("income", ignoreCase = true) }.sumOf { it.amount }
        return income - spending
    }

    fun monthlySpending(expenses: List<Expense>, month: Int, year: Int): Double {
        return monthlySpendingFromItems(expenses.map { it.toBudgetItem() }, month, year)
    }

    fun monthlySpendingFromItems(items: List<BudgetItem>, month: Int, year: Int): Double {
        return items
            .filter { !it.type.equals("income", ignoreCase = true) }
            .filter { isInMonth(it.date, month, year) }
            .sumOf { it.amount }
    }

    fun spendingByCategory(expenses: List<Expense>): Map<String, Double> {
        return spendingByCategoryFromItems(expenses.map { it.toBudgetItem() })
    }

    fun spendingByCategoryFromItems(items: List<BudgetItem>): Map<String, Double> {
        return items
            .filter { !it.type.equals("income", ignoreCase = true) }
            .groupBy { it.category.ifBlank { "Uncategorised" } }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    fun budgetStatus(monthlySpent: Double, minGoal: Float, maxGoal: Float): String {
        return when {
            minGoal <= 0f && maxGoal <= 0f -> "Set budget goals to track your progress."
            maxGoal > 0f && monthlySpent > maxGoal -> "Over maximum budget. Reduce spending where possible."
            minGoal > 0f && monthlySpent < minGoal -> "Below minimum spending goal. You are spending carefully."
            else -> "Within your monthly budget range. Great work!"
        }
    }

    fun progressPercent(monthlySpent: Double, maxGoal: Float): Int {
        if (maxGoal <= 0f) return 0
        return ((monthlySpent / maxGoal) * 100).roundToInt().coerceIn(0, 100)
    }

    fun earnedBadges(expenses: List<Expense>, monthlySpent: Double, minGoal: Float, maxGoal: Float): List<String> {
        return earnedBadgesFromItems(expenses.map { it.toBudgetItem() }, monthlySpent, minGoal, maxGoal)
    }

    fun earnedBadgesFromItems(items: List<BudgetItem>, monthlySpent: Double, minGoal: Float, maxGoal: Float): List<String> {
        val expenseOnly = items.filter { !it.type.equals("income", ignoreCase = true) }
        val badges = mutableListOf<String>()

        if (expenseOnly.isNotEmpty()) badges.add("🏁 First Expense")
        if (differentExpenseDays(expenseOnly) >= 3) badges.add("🔥 Consistent Logger")
        if (maxGoal > 0f && monthlySpent <= maxGoal && expenseOnly.isNotEmpty()) badges.add("🛡️ Budget Keeper")
        if (minGoal > 0f && maxGoal > 0f && monthlySpent in minGoal.toDouble()..maxGoal.toDouble()) badges.add("⭐ Smart Saver")

        return badges
    }

    private fun Expense.toBudgetItem(): BudgetItem {
        return BudgetItem(
            amount = amount,
            category = category,
            type = type,
            date = date
        )
    }

    private fun differentExpenseDays(items: List<BudgetItem>): Int {
        return items.map { item ->
            Calendar.getInstance().apply { timeInMillis = item.date }.let { cal ->
                "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)}-${cal.get(Calendar.DAY_OF_MONTH)}"
            }
        }.toSet().size
    }

    private fun isInMonth(timeMillis: Long, month: Int, year: Int): Boolean {
        val cal = Calendar.getInstance().apply { timeInMillis = timeMillis }
        return cal.get(Calendar.MONTH) == month && cal.get(Calendar.YEAR) == year
    }
}