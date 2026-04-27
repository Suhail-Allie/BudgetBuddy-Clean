package com.example.budgetbuddy

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

    private var categories = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Initialize UI components
        etCategoryName = findViewById(R.id.etCategoryName)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        btnClearCategories = findViewById(R.id.btnClearCategories)
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard)
        tvCategoryList = findViewById(R.id.tvCategoryList)

        // Load saved categories from local storage
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        categories = sharedPref.getString("categories", "") ?: ""

        updateCategoryList()

        // Add a new category after validation
        btnAddCategory.setOnClickListener {
            val categoryName = etCategoryName.text.toString().trim()

            if (categoryName.isEmpty()) {
                etCategoryName.error = "Please enter a category name"
                etCategoryName.requestFocus()
                Log.d("Category", "Category not added: empty input")
                return@setOnClickListener
            }

            if (categories.contains(categoryName, ignoreCase = true)) {
                Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show()
                Log.d("Category", "Duplicate category blocked: $categoryName")
                return@setOnClickListener
            }

            categories = if (categories.isEmpty()) {
                categoryName
            } else {
                "$categories\n$categoryName"
            }

            saveCategories()
            updateCategoryList()
            etCategoryName.text.clear()

            Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
            Log.d("Category", "Category added: $categoryName")
        }

        // Clear all saved categories
        btnClearCategories.setOnClickListener {
            categories = ""
            saveCategories()
            updateCategoryList()
            Toast.makeText(this, "Categories cleared", Toast.LENGTH_SHORT).show()
            Log.d("Category", "All categories cleared")
        }

        // Return to dashboard
        btnBackToDashboard.setOnClickListener {
            Log.d("Category", "Returning to dashboard")
            finish()
        }
    }

    // Display category list on screen
    private fun updateCategoryList() {
        tvCategoryList.text = if (categories.isEmpty()) {
            "No categories added yet"
        } else {
            categories
        }
    }

    // Save categories locally
    private fun saveCategories() {
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("categories", categories)
        editor.apply()
    }
}