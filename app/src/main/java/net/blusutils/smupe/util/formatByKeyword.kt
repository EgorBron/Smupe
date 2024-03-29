package net.blusutils.smupe.util

/**
 * Simply format a string with given keywords map.
 * @param values the keyword map
 * @param strict if true, throw exception when missing value for keyword
 * @return the formatted string
 */
fun String.formatByKeywords(values: Map<String, Any?>, strict: Boolean = false): String {
    val regex = "\\{(.*?)\\}".toRegex()
    return regex.replace(this) { matchResult ->
        val keyword = matchResult.groupValues[1]
        val value = values[keyword] ?: if (strict) null else matchResult.value
        if (!strict || value != null) {
            value.toString()
        } else {
            throw IllegalArgumentException("Missing value for keyword: $keyword")
        }
    }
}