package net.evilblock.stark.core.config.transform

import net.evilblock.stark.core.config.ConfigEntryTransformer

class CastTransformer<T>(private val type: Class<T>) : ConfigEntryTransformer<T> {

    override fun transform(original: Any): T {
        return type.cast(original)
    }

}
