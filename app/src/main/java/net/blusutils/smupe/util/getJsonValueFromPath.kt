package net.blusutils.smupe.util

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

val anyJsonProcessor: JsonAdapter<Any> = Moshi.Builder().build().adapter(Any::class.java)

/**
 * Get a value from a JSON string based on a path.
 * @param json The JSON string to get the value from.
 * @param path The path to the value to get.
 * @param delimiter The delimiter to use to split the path. By default, comma character is used.
 * @return The JSON value at the given path or null if not found.
 */
fun getJsonValueFromPath(
    json: String,
    path: String,
    delimiter: String = ","
): Any? {

    val keys = path.split(delimiter)
    var currentValue: Any? = anyJsonProcessor.fromJson(json)

    for (key in keys) {
        when (currentValue) {
            is List<*> -> {
                Log.d("getJsonValueFromPath", "$currentValue, $keys")
                currentValue = when (key) {
                    "~" -> currentValue.random()
                    else -> currentValue.getOrNull(key.toInt())
                }
            }

            is Map<*, *> -> {
                currentValue = currentValue[key]
            }

            else -> {
                return currentValue
            }
        }
    }
    return currentValue
}