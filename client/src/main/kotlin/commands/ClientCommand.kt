package commands

import Command
import org.koin.core.component.inject
import utils.Interactor

abstract class ClientCommand : Command() {
    val interactor: Interactor by inject()
}