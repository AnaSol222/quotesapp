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

@Composable
fun QuotesScreen() {

    val context = LocalContext.current

    var quotes by remember {
        mutableStateOf(Storage.load(context))
    }

    var quoteText by remember { mutableStateOf("") }
    var authorText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

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
                if (quoteText.isNotBlank() && authorText.isNotBlank()) {

                    val newQuote = Quote(
                        id = System.currentTimeMillis(),
                        text = quoteText,
                        author = authorText
                    )

                    quotes = quotes + newQuote
                    Storage.save(context, quotes)

                    quoteText = ""
                    authorText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Quote")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
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
                                quotes = quotes.filter { it.id != quote.id }
                                Storage.save(context, quotes)
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