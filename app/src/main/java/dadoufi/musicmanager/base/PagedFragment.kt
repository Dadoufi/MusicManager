package dadoufi.musicmanager.base

import android.os.Bundle
import android.view.View
import dadoufi.musicmanager.data.entities.PagedEntity
import dadoufi.musicmanager.data.entities.UiState
import dadoufi.musicmanager.extensions.observeK
import dadoufi.musicmanager.extensions.visibleOrGone
import dadoufi.musicmanager.util.epoxy.BaseController
import kotlinx.android.synthetic.main.include_error.*


abstract class PagedFragment<Result : Any, T : PagedEntity, PVM : PagedViewModel<Result, T>> :
    BaseFragment<Result, T, PVM>() {

    lateinit var controller: BaseController<T>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeList()


    }

    private fun setupRecyclerView() {
        with(epoxyRecyclerView) {
            this?.setController(controller)

            epoxyRecyclerView?.adapter = controller.adapter
        }
    }


    open fun observeList() {
        viewModel.liveList.observeK(this) {
            controller.submitList(it)
        }
    }

    override fun handleSuccessState(data: Result?) {
        super.handleSuccessState(data)
        controller.toggleRetry(false)
    }

    override fun handleErrorState(uiState: UiState.Error) {
        controller.toggleLoading(false)
        super.handleErrorState(uiState)
    }

    override fun handlePagedError() {
        errorLayout?.visibleOrGone = false
        controller.toggleRetry(true)
        controller.toggleLoading(false)
    }

    override fun handlePagedEmptyError() {
        errorLayout?.visibleOrGone = false
        //controller.toggleRetry(false)
        controller.toggleLoading(false)
    }


    override fun handleLoadMoreState() {
        swipeRefreshLatch.refreshing = false
        errorLayout?.visibleOrGone = false
        // loadingLatch.refreshing = false
        controller.toggleRetry(false)
        controller.toggleLoading(true)
    }

    override fun handleLoadingState() {
        super.handleLoadingState()
        //controller.toggleRetry(false)
        controller.toggleLoading(false)
    }

}