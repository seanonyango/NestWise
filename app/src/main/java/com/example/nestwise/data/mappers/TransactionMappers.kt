package com.example.nestwise.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nestwise.data.Transaction
import com.example.nestwise.data.entities.TransactionEntity

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        title = this.title,
        amount = this.amount,
        category = this.category,
        date = this.date.toString(),
        type = this.type.name
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun TransactionEntity.toModel(): Transaction {
    return Transaction(
        id = this.id,
        title = this.title,
        amount = this.amount,
        category = this.category,
        date = java.time.LocalDate.parse(this.date),
        type = com.example.nestwise.data.TransactionType.valueOf(this.type)
    )
}
