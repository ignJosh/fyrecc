package net.evilblock.stark.config

import net.evilblock.stark.Stark
import net.evilblock.stark.core.config.ConfigReader
import java.io.File

object ConfigMemory {

    var langConfig: Map<String, Any>? = null

    fun load() {
        langConfig = ConfigReader.readJsonToMap(ensureFileExists("lang.json"))
    }

    private fun ensureFileExists(fileName: String): File {
        val file = File(Stark.instance.dataFolder, fileName)

        if (!file.exists()) {
            Stark.instance.saveResource(fileName, false)
        }

        return file
    }

}