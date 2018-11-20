package dadoufi.musicmanager.data.repositories

import androidx.paging.DataSource
import dadoufi.musicmanager.data.entities.UiState
import dadoufi.musicmanager.data.repositories.PagedRepository.Page
import io.reactivex.Flowable
import io.reactivex.Maybe

interface BaseRepository<T : Any>

interface Repository<R, T : Any> : BaseRepository<Any> {
    fun refresh(query: R, forceRefresh: Boolean = true): Flowable<UiState<T>>


    fun execute(params: Params<R>): Flowable<UiState<T>> {
        return when (params.updateSource) {
            UpdateSource.CACHE -> refresh(params.query, false)
            UpdateSource.NETWORK -> refresh(params.query)
        }
    }

    data class Params<R>(val query: R, val updateSource: UpdateSource)

    enum class UpdateSource {
        NETWORK,
        CACHE
    }


}


interface PagedRepository<T : Any> : BaseRepository<Any> {

    fun getPagedDataSource(query: String? = null): DataSource.Factory<Int, T>


    data class Params(val query: String, val page: Page)

    enum class Page {
        LOAD_NEXT_PAGE, REFRESH
    }

}

abstract class PagedNetworkRepository<T : Any> : PagedRepository<T> {

    abstract fun getLastPage(query: String): Maybe<Int>

    fun execute(params: PagedRepository.Params): Flowable<UiState<Any>> {
        return when (params.page) {
            Page.LOAD_NEXT_PAGE -> loadNextPage(params.query)
            Page.REFRESH -> refresh(params.query)
        }
    }

    abstract fun refresh(query: String): Flowable<UiState<Any>>

    abstract fun loadNextPage(query: String): Flowable<UiState<Any>>

}



