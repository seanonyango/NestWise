package com.example.nestwise.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Double, currencyCode: String): String {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.currency = java.util.Currency.getInstance(currencyCode)
    return format.format(value)
}
