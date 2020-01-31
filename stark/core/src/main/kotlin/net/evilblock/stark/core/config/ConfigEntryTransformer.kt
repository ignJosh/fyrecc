package net.evilblock.stark.core.config

import net.evilblock.stark.core.config.transform.CastTransformer
import net.evilblock.stark.core.config.transform.StringListTransformer
import net.evilblock.stark.core.config.transform.StringTransformer

interface ConfigEntryTransformer<R> {

    fun transform(original: Any): R?

    companion object {

        val STRING: ConfigEntryTransformer<String> = StringTransformer()
        val STRING_LIST: ConfigEntryTransformer<List<String>> = StringListTransformer()
        val BOOLEAN: ConfigEntryTransformer<Boolean> = CastTransformer(Boolean::class.java)
        val INTEGER: ConfigEntryTransformer<Int> = CastTransformer(Int::class.java)
        val DOUBLE: ConfigEntryTransformer<Double> = CastTransformer(Double::class.java)

    }

}
