package net.blusutils.smupe.util

/**
 * Helper object for identifying file types by their magic numbers.
 */
object MagicNumberIdentifier {
    @OptIn(ExperimentalUnsignedTypes::class)
    private val magicNumbers: Map<UByteArray, String> = mapOf(
        ubyteArrayOf(137u, 80u, 78u, 71u) to "png",
        ubyteArrayOf(255u, 216u, 255u, 224u) to "jpg",
        ubyteArrayOf(52u, 49u, 46u, 46u) to "webp", // it is RIFF actually, btw AVI and WAVE have same magic
        ubyteArrayOf(66u, 77u) to "bmp",
        ubyteArrayOf(71u, 73u, 70u, 56u) to "gif",
        ubyteArrayOf(39u, 104u, 116u, 115u) to "txt",
        ubyteArrayOf(127u, 69u, 76u, 70u) to "elf",
        ubyteArrayOf(80u, 75u, 3u, 4u) to "zip",
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    fun identify(bytes: UByteArray): String {
        for ((magicNumber, extension) in magicNumbers) {
            if (bytes.startsWith(magicNumber)) {
                return extension
            }
        }
        throw IllegalArgumentException("Magic for this byte sequence is not found: $bytes")
    }
}
