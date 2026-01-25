package com.ana.quotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.ana.quotesapp.ui.theme.QuotesappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuotesappTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    QuotesScreen()
                }
            }
        }
    }
}