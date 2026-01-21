package com.ana.quotesapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object Storage {

    private const val FILE_NAME = "quotes.json"

    fun save(context: Context, quotes: List<Quote>) {
        val file = File(context.filesDir, FILE_NAME)
        val json = Gson().toJson(quotes)
        file.writeText(json)
    }

    fun load(context: Context): List<Quote> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return emptyList()

        val type = object : TypeToken<List<Quote>>() {}.type
        return Gson().fromJson(file.readText(), type)
    }
}