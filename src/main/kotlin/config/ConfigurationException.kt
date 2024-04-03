package com.rokucraft.rokubot.config

class ConfigurationException(message: String?, cause: Throwable?) : Throwable(message, cause) {
    constructor(message: String?) : this(message, null)

    constructor(cause: Throwable?) : this(cause?.toString(), cause)
}
