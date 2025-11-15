package com.example.nestwise.data.csv

import android.content.Context
import android.net.Uri
import com.example.nestwise.viewmodel.TransactionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CsvImporter {

    suspend fun importFromUri(
        context: Context,
        uri: Uri,
        viewModel: TransactionViewModel
    ): Int = withContext(Dispatchers.IO) {

        val inputStream = context.contentResolver.openInputStream(uri)
        val rows = inputStream?.bufferedReader()?.readLines() ?: return@withContext 0

        val entities = rows
            .drop(1)   // Skip header
            .mapNotNull { CsvParser.parseRow(it) }

        viewModel.importTransactions(entities)

        return@withContext entities.size
    }
}
