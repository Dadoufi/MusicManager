package dadoufi.musicmanager.util.epoxy

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.paging.PagedListEpoxyController


abstract class BaseController<T>(val diffUtilCallback: DiffUtil.ItemCallback<T> = DEFAULT_ITEM_DIFF_CALLBACK as DiffUtil.ItemCallback<T>) :
    PagedListEpoxyController<T>(
        modelBuildingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler(),
        diffingHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler(),
        itemDiffCallback = diffUtilCallback

    ) {
    open fun toggleLoading(isLoading: Boolean) = Unit
    open fun toggleRetry(retry: Boolean) = Unit

    var isLoading = false
        set(value) {
            if (value != field) {
                field = value
                requestDelayedModelBuild(500)
            }
        }

    var retry = false
        set(value) {
            if (value != field) {
                field = value
                requestDelayedModelBuild(200)
            }
        }


    public override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            (recyclerView.layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
        }
    }
}


