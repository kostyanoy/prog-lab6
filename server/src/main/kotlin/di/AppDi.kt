package di
import Server
import org.koin.dsl.module

val appModule = module {
    single {
        Server()
    }
}
