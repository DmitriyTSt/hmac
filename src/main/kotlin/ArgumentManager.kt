import kotlin.system.exitProcess

class ArgumentManager(private val args: List<String>) {
    companion object {
        private val ALL_ARGS = listOf("-help", "-s", "-c", "-k", "-m")
    }

    private var isSignMode = true
    private var key: String? = null
    private var path: String? = null

    init {
        if (args.isNotEmpty()) {
            if (args.first() == "-help") {
                println("-s\t Generate signature (default mode)")
                println("-c\t Check signature")
                println("-k (value)\t Hmac key")
                println("-m (value)\t Path to message file. Need first line signature to check")
                exitProcess(0)
            }

            repeat(args.size) {
                when (args[it]) {
                    "-s" -> {
                        isSignMode = true
                    }
                    "-c" -> {
                        isSignMode = false
                    }
                    "-k" -> {
                        val value = args.getOrNull(it + 1)
                        if (value == null || ALL_ARGS.contains(value)) {
                            throw RuntimeException("Error using, run -help")
                        } else {
                            key = value
                        }
                    }
                    "-m" -> {
                        val value = args.getOrNull(it + 1)
                        if (value == null || ALL_ARGS.contains(value)) {
                            throw RuntimeException("Error using, run -help")
                        } else {
                            path = value
                        }
                    }
                }
            }
        } else {
            throw RuntimeException("Error using, run -help")
        }
    }

    fun getLaunchParams(): LaunchParams {
        val valKey = key
        val valPath = path
        if (valKey != null && valPath != null) {
            return LaunchParams(
                isSignMode = isSignMode,
                key = valKey,
                path = valPath
            )
        } else {
            throw RuntimeException("Error using, run -help")
        }
    }
}