package com.ucasoft.modernMoney.db.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ucasoft.modernMoney.db.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query(" SELECT * FROM categories")
    fun allCategories() : Flow<List<Category>>

    @Insert
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)
}