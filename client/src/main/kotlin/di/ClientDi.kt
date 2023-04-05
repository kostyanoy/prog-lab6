package di

import FileManager
import Frame
import org.koin.dsl.module
import serialize.FrameSerializer
import serialize.Serializer
import utils.*
import utils.console.ConsoleManager

val clientModule = module {
    factory<ReaderWriter> {
        ConsoleManager()
    }

    factory<Serializer<Frame>> {
        FrameSerializer()
    }

    factory<Validator> {
        ValidationManager(interactor = get(), userManager = get())
    }

    factory {
        CommandManager()
    }

    factory {
        FileManager()
    }

    factory<Interactor> {
        InteractionManager(userManager = get(), fileManager = get())
    }
}