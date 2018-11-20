package dadoufi.musicmanager.data.daos

import io.reactivex.Maybe

interface PaginatedDao {

    fun getLastPage(query: String?): Maybe<Int>
}