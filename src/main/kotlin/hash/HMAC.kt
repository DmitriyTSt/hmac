package hash

import extension.xor

object HMAC {
    private const val BLOCK_SIZE = 64
    private const val MAGIC_1 = 0x36.toByte()
    private const val MAGIC_2 = 0x5c.toByte()

    fun get(byteKey: ByteArray, message: ByteArray): ByteArray {
        val key = if (byteKey.size > BLOCK_SIZE) {
            MD5.get(byteKey)
        } else {
            byteKey + ByteArray (BLOCK_SIZE - byteKey.size) { 0 }
        }

        val ipad = ByteArray(BLOCK_SIZE) { MAGIC_1 }
        val opad = ByteArray(BLOCK_SIZE) { MAGIC_2 }

        val ikeypad = ipad xor key
        val okeypad = opad xor key

        return MD5.get(okeypad + MD5.get(ikeypad + message))
    }
}