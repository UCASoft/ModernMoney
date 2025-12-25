package com.ucasoft.modernMoney.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = [ "id" ],
            childColumns = [ "parentId" ],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val parentId: Long? = null,
    val logo: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (parentId != other.parentId) return false
        if (name != other.name) return false
        if (!logo.contentEquals(other.logo)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (parentId?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + (logo?.contentHashCode() ?: 0)
        return result
    }
}

data class CategoryWithParent(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "parentId",
        entityColumn = "id"
    )
    val parent: Category?
)