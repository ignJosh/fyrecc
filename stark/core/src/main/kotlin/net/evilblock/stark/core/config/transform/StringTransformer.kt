package net.evilblock.stark.core.config.transform

import net.evilblock.stark.core.StarkCore
import net.evilblock.stark.core.config.ConfigEntryTransformer

class StringTransformer : ConfigEntryTransformer<String> {

    override fun transform(original: Any): String {
        return if (original is String) {
            original.replace('&', StarkCore.COLOR_CODE_CHAR)
        } else {
            ""
        }
    }

}
