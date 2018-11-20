package dadoufi.musicmanager.inject

import androidx.lifecycle.ViewModel
import dadoufi.musicmanager.ui.album.favorite.FavoriteAlbumFragment
import dadoufi.musicmanager.ui.album.favorite.FavoriteAlbumViewModel
import dadoufi.musicmanager.ui.album.top.TopAlbumFragment
import dadoufi.musicmanager.ui.album.top.TopAlbumViewModel
import dadoufi.musicmanager.ui.details.AlbumDetailsFragment
import dadoufi.musicmanager.ui.details.AlbumDetailsViewModel
import dadoufi.musicmanager.ui.search.SearchFragment
import dadoufi.musicmanager.ui.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun favoriteAlbumFragment(): FavoriteAlbumFragment

    @ContributesAndroidInjector
    abstract fun topAlbumFragment(): TopAlbumFragment

    @ContributesAndroidInjector
    abstract fun searchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun detailsFragment(): AlbumDetailsFragment

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteAlbumViewModel::class)
    abstract fun bindFavoriteAlbumViewModel(viewModel: FavoriteAlbumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAlbumViewModel::class)
    abstract fun bindTopAlbumViewModel(viewModel: TopAlbumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchFragmentViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlbumDetailsViewModel::class)
    abstract fun bindAlbumDetailsViewModel(viewModel: AlbumDetailsViewModel): ViewModel

}