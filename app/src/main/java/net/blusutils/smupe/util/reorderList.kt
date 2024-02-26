package net.blusutils.smupe.util

/**
 * Tries to reorder a list based on a given order string.
 * @param data The list to reorder. Length should be lesser than 10!
 * @param order The order string with indices.
 * @return The reordered list.
 */
fun<T> reorderList(data: List<T>, order: String): List<T> {
    // Map characters to indices
    val orderMap = order.withIndex().associate { it.value.digitToInt() to it.index }

    return data.withIndex().sortedWith(compareBy {
        orderMap[it.index]!! // Sort based on desired indices
    }).map { it.value }
}

/**
 * Tries to reorder a list based on a given order string.
 * @param data The list to reorder. Length can be any.
 * @param order The order string with indices, separated by delimiter.
 * @param delimiter Indices delimiter.
 * @return The reordered list.
 */
fun<T> reorderListWithDelimiter(data: List<T>, order: String, delimiter: String = ","): List<T> {
    // Map characters to indices
    val orderMap = order.split(delimiter).withIndex().associate { it.value.toInt() to it.index }

    return data.withIndex().sortedWith(compareBy {
        orderMap[it.index]!! // Sort based on desired indices
    }).map { it.value }
}

/**
 * Reorders this list based on a given order string.
 * @see reorderList
 */
fun<T> List<T>.reorder(order: String): List<T> =
    reorderList(this, order)

/**
 * Reorders this list based on a given order string with delimiters.
 * @see reorderListWithDelimiter
 */
fun<T> List<T>.reorderWithDelimiter(order: String, delimiter: String = ","): List<T> =
    reorderListWithDelimiter(this, order, delimiter)
