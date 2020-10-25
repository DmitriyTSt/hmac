package hash

import kotlin.math.abs
import kotlin.math.sin

object MD5 {
    private val INIT_A = 0x67452301
    private val INIT_B = 0xEFCDAB89.toInt()
    private val INIT_C = 0x98BADCFE.toInt()
    private val INIT_D = 0x10325476

    private val SHIFT_AMTS = intArrayOf(
        7, 12, 17, 22,
        5, 9, 14, 20,
        4, 11, 16, 23,
        6, 10, 15, 21
    )

    private val TABLE_T = IntArray(64) {
        ((1L shl 32) * abs(sin(it + 1.0))).toLong().toInt()
    }

    fun get(message: ByteArray): ByteArray {
        val messageLenBytes = message.size
        val numBlocks = ((messageLenBytes + 8) ushr 6) + 1
        val totalLen = numBlocks shl 6
        val paddingBytes = ByteArray(totalLen - messageLenBytes)
        paddingBytes[0] = 0x80.toByte()
        var messageLenBits = (messageLenBytes shl 3).toLong()

        repeat(8) { i ->
            paddingBytes[paddingBytes.size - 8 + i] = messageLenBits.toByte()
            messageLenBits = messageLenBits ushr 8
        }

        var a = INIT_A
        var b = INIT_B
        var c = INIT_C
        var d = INIT_D
        val buffer = IntArray(16)

        repeat(numBlocks) { i ->
            var index = i shl 6

            repeat(64) { j ->
                val temp = if (index < messageLenBytes) message[index] else
                    paddingBytes[index - messageLenBytes]
                buffer[j ushr 2] = (temp.toInt() shl 24) or (buffer[j ushr 2] ushr 8)
                index++
            }

            val originalA = a
            val originalB = b
            val originalC = c
            val originalD = d

            repeat(64) { j ->
                val div16 = j ushr 4
                var f = 0
                var bufferIndex = j
                when (div16) {
                    0 -> {
                        f = (b and c) or (b.inv() and d)
                    }

                    1 -> {
                        f = (b and d) or (c and d.inv())
                        bufferIndex = (bufferIndex * 5 + 1) and 0x0F
                    }

                    2 -> {
                        f = b xor c xor d;
                        bufferIndex = (bufferIndex * 3 + 5) and 0x0F
                    }

                    3 -> {
                        f = c xor (b or d.inv());
                        bufferIndex = (bufferIndex * 7) and 0x0F
                    }
                }

                val temp = b + Integer.rotateLeft(
                    a + f + buffer[bufferIndex] +
                            TABLE_T[j], SHIFT_AMTS[(div16 shl 2) or (j and 3)]
                )
                a = d
                d = c
                c = b
                b = temp
            }

            a += originalA
            b += originalB
            c += originalC
            d += originalD
        }

        val md5 = ByteArray(16)
        var count = 0

        repeat(4) { i ->
            var n = if (i == 0) a else (if (i == 1) b else (if (i == 2) c else d))

            repeat(4) {
                md5[count++] = n.toByte()
                n = n ushr 8
            }
        }
        return md5
    }
}