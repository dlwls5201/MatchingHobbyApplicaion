package com.tistory.blackjin.birdviewassignment.util

import android.content.Context

object ReadTextFileUtil {

    fun getItems(context: Context, fileName: String): Array<List<String>> {
        val inputStream = context.assets.open(fileName)
        val strItems = inputStream.bufferedReader().use { it.readText() }
        val items = strItems.split("\n")

        val userCount = items[0].toInt()

        return Array(size = userCount, init = { index ->
            items[index + 1].split(" ")
        })
    }
}