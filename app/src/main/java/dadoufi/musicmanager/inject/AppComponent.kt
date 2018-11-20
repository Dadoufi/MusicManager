package dadoufi.musicmanager.inject

import dadoufi.musicmanager.App
import dadoufi.musicmanager.data.DatabaseModule
import dadoufi.musicmanager.remote.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        DatabaseModule::class,
        ViewModelBuilder::class,
        NetworkModule::class,
        MainActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}