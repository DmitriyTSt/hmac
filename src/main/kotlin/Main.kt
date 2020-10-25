import extension.toHexString
import hash.HMAC
import java.io.File
import java.lang.RuntimeException

fun main(args: Array<String>) {
    val launchParams = ArgumentManager(args.toList()).getLaunchParams()
    val file = File(launchParams.path)
    if (file.exists()) {
        val contents = file.readLines()
        if (launchParams.isSignMode) {
            val hmac = HMAC.get(
                launchParams.key.toByteArray(Charsets.UTF_8),
                contents.joinToString("\n").toByteArray(Charsets.UTF_8)
            ).toHexString()
            file.appendText("\n$hmac")
            println("Message is signed")
        } else {
            val hmacString = contents.last()
            val newHmac = HMAC.get(
                launchParams.key.toByteArray(Charsets.UTF_8),
                contents.subList(0, contents.size - 1).joinToString("\n").toByteArray(Charsets.UTF_8)
            ).toHexString()
            if (hmacString == newHmac) {
                println("Message correct")
            } else {
                println("Message incorrect")
            }
        }
    } else {
        throw RuntimeException("Wrong message file path, use -help")
    }
}
