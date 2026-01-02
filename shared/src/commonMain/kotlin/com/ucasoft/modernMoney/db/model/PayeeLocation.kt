package com.ucasoft.modernMoney.db.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "payee_location",
    primaryKeys = ["payeeId", "locationId"],
    foreignKeys = [
        ForeignKey(
            entity = Payee::class,
            parentColumns = [ "id" ],
            childColumns = [ "payeeId" ],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = [ "id" ],
            childColumns = [ "locationId" ],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PayeeLocation(
    val payeeId: Long,
    val locationId: Long
)