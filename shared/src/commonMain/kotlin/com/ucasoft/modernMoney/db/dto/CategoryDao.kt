package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ucasoft.modernMoney.db.model.Category
import com.ucasoft.modernMoney.db.model.CategoryWithParent
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("""
        WITH RECURSIVE category(id, name, logo, parentId) as (
            SELECT id, name, logo, parentId FROM categories WHERE parentId IS NULL
            UNION ALL
            SELECT c.id, c.name, c.logo, c.parentId FROM categories c
            JOIN category ON category.id = c.parentId
        )
        SELECT * FROM category
    """)
    fun allCategories() : Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun categoryById(id: Long) : Flow<CategoryWithParent>

    @Query(
        """
            SELECT 1 FROM categories
            WHERE name = :name AND parentId IS :parentId
            LIMIT 1
        """
    )
    suspend fun doesExist(name: String, parentId: Long?) : Boolean

    @Insert
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)
}