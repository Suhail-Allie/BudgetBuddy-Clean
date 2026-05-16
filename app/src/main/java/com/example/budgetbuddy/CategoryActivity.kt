package com.example.budgetbuddy

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CategoryActivity : AppCompatActivity() {

    private lateinit var etCategoryName: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var btnClearCategories: Button
    private lateinit var btnBackToDashboard: Button
    private lateinit var tvCategoryList: TextView

    private lateinit var currentUserKey: String
    private lateinit var currentDisplayName: String
    private lateinit var userPrefs: SharedPreferences

    private var categories = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        etCategoryName = findViewById(R.id.etCategoryName)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        btnClearCategories = findViewById(R.id.btnClearCategories)
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard)
        tvCategoryList = findViewById(R.id.tvCategoryList)

        currentUserKey = getCurrentUserKey()
        currentDisplayName = getCurrentDisplayName()
        userPrefs = getUserPrefs(currentUserKey)

        categories = userPrefs.getString("categories", "") ?: ""
        updateCategoryList()

        btnAddCategory.setOnClickListener {
            val categoryName = etCategoryName.text.toString().trim()

            if (categoryName.isEmpty()) {
                etCategoryName.error = "Please enter a category name"
                etCategoryName.requestFocus()
                return@setOnClickListener
            }

            val existingCategories = categories
                .lines()
                .map { it.trim().lowercase() }
                .filter { it.isNotEmpty() }

            if (existingCategories.contains(categoryName.lowercase())) {
                Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            categories = if (categories.isBlank()) {
                categoryName
            } else {
                "$categories\n$categoryName"
            }

            saveCategories()
            updateCategoryList()
            etCategoryName.text.clear()

            Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
            Log.d("Category", "Category added for userKey=$currentUserKey category=$categoryName")
        }

        btnClearCategories.setOnClickListener {
            categories = ""
            saveCategories()
            updateCategoryList()

            Toast.makeText(this, "Categories cleared", Toast.LENGTH_SHORT).show()
            Log.d("Category", "Categories cleared for userKey=$currentUserKey")
        }

        btnBackToDashboard.setOnClickListener {
            NavigationUtils.closeScreen(this)
        }
    }

    private fun updateCategoryList() {
        tvCategoryList.text = if (categories.isBlank()) {
            "No categories added yet for $currentDisplayName"
        } else {
            categories
        }
    }

    private fun saveCategories() {
        userPrefs.edit()
            .putString("categories", categories)
            .apply()
    }

    private fun getCurrentUserKey(): String {
        val sessionPrefs = getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
        val userKey = sessionPrefs.getString("currentUserKey", null)

        return if (!userKey.isNullOrBlank()) {
            userKey.trim().lowercase()
        } else {
            "guest"
        }
    }

    private fun getCurrentDisplayName(): String {
        val sessionPrefs = getSharedPreferences("BudgetBuddySession", MODE_PRIVATE)
        val displayName = sessionPrefs.getString("currentUsername", null)

        return if (!displayName.isNullOrBlank()) {
            displayName.trim()
        } else {
            "Guest"
        }
    }

    private fun getUserPrefs(userKey: String): SharedPreferences {
        return getSharedPreferences("UserData_${userKey.lowercase()}", MODE_PRIVATE)
    }
}