package com.example.nestwise.ui.screens

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nestwise.data.csv.CsvImporter
import com.example.nestwise.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportCsvScreen(
    navController: NavController,
    viewModel: TransactionViewModel

) {
    val context = LocalContext.current

    // Snackbar + coroutines
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // File picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                scope.launch {
                    val count = CsvImporter.importFromUri(context, uri, viewModel)
                    snackbarHostState.showSnackbar(
                        "Imported $count transactions",
                        withDismissAction = true
                    )
                }
            }
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Import Transactions") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")

                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Bulk Import CSV",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Download the template, fill it in Excel or Google Sheets, " +
                        "then upload it here. This allows you to quickly import many transactions.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Download template button
            Button(
                onClick = {
                    downloadTemplate(context, snackbarHostState, scope)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Description, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Download Template")
            }

            // Import CSV button
            ElevatedButton(
                onClick = {
                    launcher.launch(arrayOf("text/csv", "text/*"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.FileUpload, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Upload Completed CSV")
            }

            Spacer(Modifier.height(30.dp))

            AssistChip(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Template requires: title, amount, category, date, type"
                        )
                    }
                },
                label = { Text("View Requirements") },
                leadingIcon = {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
            )

        }
    }
}

/**
 * Downloads the CSV template from assets into the device Downloads folder using MediaStore.
 */
fun downloadTemplate(
    context: Context,
    snackbarHost: SnackbarHostState,
    scope: CoroutineScope
) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "transaction_template.csv")
        put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
    }

    val uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
    var outputStreamUri: android.net.Uri? = null

    try {
        outputStreamUri = resolver.insert(uri, contentValues)
            ?: throw IOException("Failed to create MediaStore entry.")

        resolver.openOutputStream(outputStreamUri)?.use { output ->
            context.assets.open("transaction_template.csv").use { input ->
                input.copyTo(output)
            }
        } ?: throw IOException("Failed to open output stream.")

        scope.launch {
            snackbarHost.showSnackbar("Template saved to Downloads")
        }
    } catch (e: Exception) {
        outputStreamUri?.let { resolver.delete(it, null, null) }
        scope.launch {
            snackbarHost.showSnackbar("Failed to save template: ${e.javaClass.simpleName}")
        }
    }
}