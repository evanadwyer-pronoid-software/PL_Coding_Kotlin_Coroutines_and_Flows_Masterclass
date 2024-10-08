package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass

import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

object RemoteService {
    suspend fun uploadFile() {
        val chunks = List(400) { it }
        println("Uploading File")
        var index = 0
        while (index < chunks.size) {
            try {
                // Simulate uploading a single chunk of data
                delay(5)
                index++
                println("Progress: ${index * 100 / chunks.size}%")
            } catch (e: Exception) {
                println("Error uploading file: ${e.message}")
                yield()
            }
        }
        println("Upload Complete")
    }
}