package dadoufi.musicmanager.base

import android.annotation.SuppressLint
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dadoufi.musicmanager.data.entities.PagedEntity
import dadoufi.musicmanager.data.repositories.PagedRepository
import dadoufi.musicmanager.extensions.distinctUntilChanged
import dadoufi.musicmanager.ui.album.PAGE_SIZE
import dadoufi.musicmanager.util.AppRxSchedulers

@SuppressLint("RxSubscribeOnError")
abstract class PagedViewModel<Result : Any, T : PagedEntity>(
    val schedulers: AppRxSchedulers,
    open val repository: PagedRepository<T>,
    private val pageSize: Int = PAGE_SIZE,
    private val prefetchDistance: Int = PAGE_SIZE,
    private val initialLoadSize: Int = PAGE_SIZE.times(3)
) : BaseViewModel<Result>() {

    //todo maybe create a NetworkPagedViewModel and Local


    private val livePagedListBuilder by lazy {
        createLivePagedListBuilder()
    }

    fun createLivePagedListBuilder(query: String? = null): LivePagedListBuilder<Int, T> {
        return LivePagedListBuilder<Int, T>(
            repository.getPagedDataSource(query),
            PagedList.Config.Builder().run {
                setPageSize(pageSize)
                    .setPrefetchDistance(prefetchDistance)
                    .setInitialLoadSizeHint(initialLoadSize)
                build()
            }
        ).setBoundaryCallback(object : PagedList.BoundaryCallback<T>() {
            override fun onItemAtEndLoaded(itemAtEnd: T) {
                liveList.value?.let {
                    if (it.count() >= pageSize) {
                        callLoadMore()
                    }
                } ?: run {
                    callLoadMore()
                }
            }

        })
    }

    open val liveList by lazy(mode = LazyThreadSafetyMode.NONE) {
        livePagedListBuilder
            .build().distinctUntilChanged()
    }


    open fun callLoadMore() = Unit


}