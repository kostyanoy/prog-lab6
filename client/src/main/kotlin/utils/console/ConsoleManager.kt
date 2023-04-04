package utils.console

import utils.ReaderWriter

/**
 * Implements the [ReaderWriter] by interacting with the console with build-in methods
 */
class ConsoleManager : ReaderWriter {
    override fun readLine(): String = readln()
    override fun writeLine(text: String) = println(text)
    override fun write(text: String) = print(text)

    override fun getValidatedValue(message: String, validator: (String) -> Boolean): String {
        write(message)
        while (true){
            val userInput = readLine()
            if (validator(userInput)){
                return userInput
            }

            write("Попробуйте ещё раз: ")
        }
    }
}