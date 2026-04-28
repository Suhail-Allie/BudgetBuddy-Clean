package com.example.budgetbuddy

import androidx.room.*

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY name")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE name = :categoryName")
    suspend fun getCategoryByName(categoryName: String): Category?

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
}
