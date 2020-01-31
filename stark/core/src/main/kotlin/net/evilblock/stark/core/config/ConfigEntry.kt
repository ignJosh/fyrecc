package net.evilblock.stark.core.config

import org.apache.commons.lang3.StringUtils
import java.lang.UnsupportedOperationException

abstract class ConfigEntry<T>(private val transformer: ConfigEntryTransformer<T>, private val key: String) {

    private var cached: T? = null

    abstract fun getCachedObject(): Map<String, Any>

    fun get(): T? {
        if (cached != null) {
            return cached
        }

        cached = transformer.transform(getRecursive(getCachedObject(), key) as Any)

        return cached
    }

    private fun getRecursive(node: Map<String, Any>, key: String): T? {
        val splitPath = key.split(".").toTypedArray()

        return if (node.containsKey(splitPath[0])) {
            if (splitPath.size > 1) {
                if (node[splitPath[0]] is Map<*, *>) {
                    getRecursive(node[splitPath[0]] as Map<String, Any>, StringUtils.join(splitPath, '.', 1, splitPath.size))
                } else {
                    throw UnsupportedOperationException("Recursive node key is not a map")
                }
            } else node[splitPath[0]] as T?
        } else null
    }

}
