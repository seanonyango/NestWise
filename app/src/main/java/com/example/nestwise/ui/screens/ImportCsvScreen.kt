package com.example.nestwise.ui.screens

import android.content.Context
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nestwise.data.csv.CsvImporter
import com.example.nestwise.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

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
                        Icon(Icons.Default.Info, "Back")
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
 * Downloads the CSV template from assets into the device Downloads folder.
 */
fun downloadTemplate(
    context: Context,
    snackbarHost: SnackbarHostState,
    scope: CoroutineScope
) {
    try {
        val input = context.assets.open("transaction_template.csv")

        val downloads = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        val outFile = File(downloads, "transaction_template.csv")

        input.copyTo(outFile.outputStream())

        scope.launch {
            snackbarHost.showSnackbar("Template saved to Downloads")
        }

    } catch (e: Exception) {
        scope.launch {
            snackbarHost.showSnackbar("Failed to save template")
        }
    }
}
