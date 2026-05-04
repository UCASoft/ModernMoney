package com.ucasoft.modernMoney.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banks")
data class Bank(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val logo: ByteArray? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Bank

        if (id != other.id) return false
        if (name != other.name) return false
        if (!logo.contentEquals(other.logo)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (logo?.contentHashCode() ?: 0)
        return result
    }
}