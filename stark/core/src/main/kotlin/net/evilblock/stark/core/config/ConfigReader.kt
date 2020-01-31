package net.evilblock.stark.core.config

import com.google.common.io.Files
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

object ConfigReader {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun readJsonToMap(file: File): Map<String, Any>? {
        var map: Map<String, Any>? = null

        try {
            Files.newReader(file, Charsets.UTF_8).use { reader ->
                val type = object : TypeToken<Map<String, Any>>() {}.type
                map = gson.fromJson(reader, type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return map
    }

}