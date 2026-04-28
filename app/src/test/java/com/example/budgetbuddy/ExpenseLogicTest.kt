package com.example.budgetbuddy

import org.junit.Assert.*
import org.junit.Test

class ExpenseLogicTest {

    @Test
    fun testAddition() {
        val result = 100.0 + 50.0
        assertEquals(150.0, result, 0.0)
    }

    @Test
    fun testExpenseReduction() {
        val balance = 200.0
        val expense = 50.0
        val newBalance = balance - expense

        assertEquals(150.0, newBalance, 0.0)
    }

    @Test
    fun testCategoryTotal() {
        val totals = listOf(
            CategoryTotal("Food", 200.0),
            CategoryTotal("Transport", 100.0)
        )

        assertEquals(2, totals.size)
        assertEquals("Food", totals[0].category)
    }
}