package dadoufi.musicmanager.inject


import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelBuilder {


//    @Binds
//    @IntoMap
//    @ViewModelKey(FavoriteAlbumViewModel::class)
//    abstract fun bindFavoriteAlbumViewModel(viewModel: FavoriteAlbumViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(TopAlbumViewModel::class)
//    abstract fun bindTopAlbumViewModel(viewModel: TopAlbumViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(SearchViewModel::class)
//    abstract fun bindAlbumDetailsViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}