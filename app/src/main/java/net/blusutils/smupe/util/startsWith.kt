package net.blusutils.smupe.util

/**
 * Checks whether the byte array starts with the specified bytes.
 * @param sequence bytes to compare
 */
@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.startsWith(sequence: UByteArray): Boolean {
    if (this.size < sequence.size) return false
    for (i in sequence.indices) {
        if (this[i] != sequence[i]) return false
    }
    return true
}
