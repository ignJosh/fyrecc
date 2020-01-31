package net.evilblock.stark.core.config.transform

import net.evilblock.stark.core.StarkCore
import java.util.ArrayList
import net.evilblock.stark.core.config.ConfigEntryTransformer

class StringListTransformer : ConfigEntryTransformer<List<String>> {

    override fun transform(original: Any): List<String> {
        val newList = ArrayList<String>()

        if (original is List<*>) {
            for (element in original) {
                newList.add(element.toString().replace('&', StarkCore.COLOR_CODE_CHAR))
            }
        }

        return newList
    }

}
