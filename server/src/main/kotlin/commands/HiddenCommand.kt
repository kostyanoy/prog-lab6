package commands

import ServerApp
import org.koin.core.component.inject

abstract class HiddenCommand : StorageCommand(){
  val serverApp: ServerApp by inject()
  abstract fun hidden(): Boolean

}
