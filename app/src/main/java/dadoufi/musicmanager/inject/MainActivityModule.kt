package dadoufi.musicmanager.inject

import dadoufi.musicmanager.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            FragmentBuildersModule::class,
            ViewModelBuilder::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity

//     @Binds
//     @IntoMap
//     @ViewModelKey(HomeActivityViewModel::class)
//     abstract fun bindHomeActivityViewModel(viewModel: HomeActivityViewModel): ViewModel
}
