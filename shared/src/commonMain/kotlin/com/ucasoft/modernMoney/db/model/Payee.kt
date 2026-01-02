package com.ucasoft.modernMoney.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payees")
data class Payee(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val logo: ByteArray? = null,
    val aliases: List<String> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Payee

        if (id != other.id) return false
        if (name != other.name) return false
        if (!logo.contentEquals(other.logo)) return false
        if (aliases != other.aliases) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (logo?.contentHashCode() ?: 0)
        result = 31 * result + aliases.hashCode()
        return result
    }
}