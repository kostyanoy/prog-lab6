package utils

sealed class CommandResult {
    data class Success(val commandName: String, val message: String? = null) : CommandResult()
    data class Failure(val commandName: String, val throwable: Throwable) : CommandResult()
}
