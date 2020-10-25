package hash

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object InnerHMAC {
    fun hmac(secretKey: String, message: String): ByteArray {
        try {
            return hmac(secretKey.toByteArray(Charsets.UTF_8), message.toByteArray(Charsets.UTF_8))
        } catch (e: Exception) {
            throw RuntimeException("Failed to generate HMAC_MD5 encrypt", e)
        }
    }

    fun hmac(secretKey: ByteArray, message: ByteArray): ByteArray {
        try {
            val mac = Mac.getInstance ("HmacMD5");
            val sks = SecretKeySpec(secretKey, "HmacMD5");
            mac.init(sks);
            val hmac256 = mac.doFinal(message);
            return hmac256;
        } catch (e: Exception) {
            throw RuntimeException ("Failed to generate HMACSHA256 encrypt ");
        }
    }
}