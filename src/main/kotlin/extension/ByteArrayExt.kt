package extension

import kotlin.experimental.xor

fun ByteArray.toHexString(): String {
    val sb = StringBuilder()
    for (b in this) sb.append(String.format("%02x", b.toInt() and 0xFF))
    return sb.toString()
}

infix fun ByteArray.xor(other: ByteArray): ByteArray {
    val result = this.copyOf()
    repeat(result.size) {
        result[it] = result[it] xor other[it]
    }
    return result
}