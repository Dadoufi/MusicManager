package dadoufi.musicmanager.data.repositories.favorite

import io.reactivex.Completable

interface FavoriteDataSource {

    fun removeFavorite(mid: Long): Completable


}