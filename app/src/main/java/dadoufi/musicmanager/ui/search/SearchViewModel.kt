package dadoufi.musicmanager.ui.search

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import dadoufi.musicmanager.base.PagedViewModel
import dadoufi.musicmanager.data.entities.ArtistEntity
import dadoufi.musicmanager.data.repositories.PagedRepository.Page
import dadoufi.musicmanager.data.repositories.PagedRepository.Params
import dadoufi.musicmanager.data.repositories.artist.SearchArtistRepository
import dadoufi.musicmanager.extensions.distinctUntilChangedNonEmptyList
import dadoufi.musicmanager.util.AbsentLiveData
import dadoufi.musicmanager.util.AppRxSchedulers
import io.reactivex.rxkotlin.plusAssign
import java.util.*
import javax.inject.Inject

@SuppressLint("RxSubscribeOnError")
class SearchViewModel @Inject constructor(
    schedulers: AppRxSchedulers,
    override var repository: SearchArtistRepository
) : PagedViewModel<Any, ArtistEntity>(schedulers, repository, 30, 2) {

    private val query = MutableLiveData<String>()

    override val liveList: LiveData<PagedList<ArtistEntity>> by lazy(mode = LazyThreadSafetyMode.NONE) {
        Transformations.switchMap(query) { searchQuery ->
            if (searchQuery.isNullOrEmpty()) {
                AbsentLiveData.create()
            } else {
                createLivePagedListBuilder(searchQuery)
                    .build().distinctUntilChangedNonEmptyList()
            }
        }
    }

    override fun callLoadMore() {
        query.value?.let { name ->
            disposables += repository.execute(
                Params(
                    name,
                    Page.LOAD_NEXT_PAGE
                )
            ).subscribe { uiStateLiveData.postValue(it) }

        }
    }

    override fun callRefresh(forceUpdate: Boolean) {
        query.value?.let { name ->
            disposables += repository.execute(
                Params(
                    name,
                    Page.REFRESH
                )
            ).subscribe {
                liveList.value?.dataSource?.invalidate()
                uiStateLiveData.postValue(it)
            }
        }
    }


    fun setQuery(searchInput: String?) {
        if (searchInput.isNullOrEmpty()) {
            return
        }
        val input = searchInput.toLowerCase(Locale.getDefault()).trim()
        if (input == query.value) {
            //   return
        }
        query.value = input
        callRefresh()

    }


}









