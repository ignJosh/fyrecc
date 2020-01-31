package net.evilblock.stark.config

import net.evilblock.stark.core.config.ConfigEntry
import net.evilblock.stark.core.config.ConfigEntryTransformer

class LangConfigEntry<T>(transformer: ConfigEntryTransformer<T>, key: String) : ConfigEntry<T>(transformer, key) {

    override fun getCachedObject(): Map<String, Any> {
        return ConfigMemory.langConfig!!
    }

    companion object {
        val REBOOT_LINES: ConfigEntry<List<String>> = LangConfigEntry(ConfigEntryTransformer.STRING_LIST, "reboot.broadcast-lines")
    }

}
