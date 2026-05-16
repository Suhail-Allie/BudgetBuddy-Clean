package com.example.budgetbuddy

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ExpenseLogicTest {

    @Test
    fun calculateBalance_subtractsExpensesFromIncome() {
        val items = listOf(
            BudgetBuddyLogic.BudgetItem(
                amount = 1000.0,
                category = "Income",
                type = "income",
                date = System.currentTimeMillis()
            ),
            BudgetBuddyLogic.BudgetItem(
                amount = 250.0,
                category = "Groceries",
                type = "expense",
                date = System.currentTimeMillis()
            )
        )

        assertEquals(750.0, BudgetBuddyLogic.calculateBalanceFromItems(items), 0.0)
    }

    @Test
    fun budgetStatus_returnsOverBudgetWhenSpendingIsAboveMaximum() {
        val status = BudgetBuddyLogic.budgetStatus(
            monthlySpent = 1200.0,
            minGoal = 500f,
            maxGoal = 1000f
        )

        assertTrue(status.contains("Over maximum budget"))
    }

    @Test
    fun progressPercent_capsAt100Percent() {
        val progress = BudgetBuddyLogic.progressPercent(
            monthlySpent = 1500.0,
            maxGoal = 1000f
        )

        assertEquals(100, progress)
    }

    @Test
    fun earnedBadges_unlocksFirstExpenseAndBudgetKeeper() {
        val now = Calendar.getInstance().timeInMillis
        val items = listOf(
            BudgetBuddyLogic.BudgetItem(
                amount = 100.0,
                category = "Transport",
                type = "expense",
                date = now
            )
        )

        val badges = BudgetBuddyLogic.earnedBadgesFromItems(
            items = items,
            monthlySpent = 100.0,
            minGoal = 0f,
            maxGoal = 500f
        )

        assertTrue(badges.any { it.contains("First Expense") })
        assertTrue(badges.any { it.contains("Budget Keeper") })
    }
}