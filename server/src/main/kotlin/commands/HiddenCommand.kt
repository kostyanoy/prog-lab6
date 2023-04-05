package commands

abstract class HiddenCommand : StorageCommand(){
  abstract fun hidden(): Boolean

}
