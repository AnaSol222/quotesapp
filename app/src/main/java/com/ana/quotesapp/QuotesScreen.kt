package com.ana.quotesapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import android.content.Intent
import com.google.gson.Gson


@Composable
fun QuotesScreen() {

    val context = LocalContext.current
    var folderUri by remember { mutableStateOf<Uri?>(null)   }

    val folderPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            folderUri = uri
        }
    }

    var quotes by remember { mutableStateOf(emptyList<Quote>()) }
    var quoteText by remember { mutableStateOf("") }
    var authorText by remember { mutableStateOf("") }



    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Button(
            onClick = {
                folderPicker.launch(null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Choose Storage Folder (Documents)")
        }


        OutlinedTextField(
            value = quoteText,
            onValueChange = { quoteText = it },
            label = { Text("Quote") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 8,
            minLines = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = authorText,
            onValueChange = { authorText = it },
            label = { Text("Author") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (quoteText.isNotBlank() &&
                    authorText.isNotBlank() &&
                    folderUri != null) {

                    val newQuote = Quote(
                        id = System.currentTimeMillis(),
                        text = quoteText,
                        author = authorText
                    )

                    quotes = quotes + newQuote
                    saveQuotesToChosenFolder(
                        context,
                        folderUri!!,
                        quotes
                    )


                    quoteText = ""
                    authorText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Quote")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(quotes, key = { it.id }) { quote ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("“${quote.text}”")
                            Text("- ${quote.author}", style = MaterialTheme.typography.bodySmall)
                        }

                        IconButton(
                            onClick = {
                                if (folderUri != null) {
                                    quotes = quotes.filter { it.id != quote.id }
                                    saveQuotesToChosenFolder(
                                        context,
                                        folderUri!!,
                                        quotes
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete quote"
                            )
                        }
                    }
                }
            }
        }
    }
}
fun saveQuotesToChosenFolder(
    context: android.content.Context,
    folderUri: Uri,
    quotes: List<Quote>
) {
    val folder = DocumentFile.fromTreeUri(context, folderUri)
        ?: return

    val file = folder.findFile("quotes.json")
        ?: folder.createFile("application/json", "quotes")

    file?.let {
        context.contentResolver.openOutputStream(it.uri, "wt")?.use { output ->
            val json = Gson().toJson(quotes)
            output.write(json.toByteArray())
        }
    }
}
