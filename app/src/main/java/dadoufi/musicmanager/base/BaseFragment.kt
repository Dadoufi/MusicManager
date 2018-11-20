/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dadoufi.musicmanager.base

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.transition.addListener
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import dadoufi.musicmanager.R
import dadoufi.musicmanager.data.entities.Entity
import dadoufi.musicmanager.data.entities.UiState
import dadoufi.musicmanager.extensions.createSnackBar
import dadoufi.musicmanager.extensions.observeK
import dadoufi.musicmanager.extensions.visibleOrGone
import dadoufi.musicmanager.util.ProgressTimeLatch
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_album_list.*
import kotlinx.android.synthetic.main.include_error.*
import javax.inject.Inject

/**
 * Base fragment class which supports LifecycleOwner and Dagger injection.
 */
abstract class BaseFragment<Result : Any, in T : Entity, VM : BaseViewModel<Result>> :
    DaggerFragment() {

    var epoxyRecyclerView: EpoxyRecyclerView? = null
    lateinit var swipeRefreshLatch: ProgressTimeLatch

    private var startedTransition = false
    private var postponed = false
    private var hasPendingSnackBar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: VM

    protected abstract val classToken: Class<VM>

    private lateinit var parentView: View
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(getLayoutId(), container, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(classToken)
        TransitionInflater.from(context).run {
            val fadeSlide = inflateTransition(R.transition.fade_slide).apply {
                addListener(
                    onEnd = {
                        showPendingSnackBar()
                    }
                )
            }
            enterTransition = fadeSlide

            val fade = inflateTransition(R.transition.fade)
            returnTransition = fade
            exitTransition = fade
            reenterTransition = fade
            allowEnterTransitionOverlap = true
            allowReturnTransitionOverlap = true
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentView = view
        epoxyRecyclerView = view.findViewById(R.id.recyclerView)
        setupSwipeToRefresh()
        setUpAppbarElevation()

        swipeRefreshLayout = parentView.findViewById(R.id.swipeRefresh)


        viewModel.uiStateLiveData.observeK(this) {
            when (it) {
                is UiState.Loading -> {
                    handleLoadingState()
                }
                is UiState.LoadMore -> {
                    handleLoadMoreState()
                }
                is UiState.Error -> {
                    handleErrorState(it)
                }
                is UiState.Success -> {
                    handleSuccessState(it.data)
                }
            }
        }


    }

    override fun postponeEnterTransition() {
        super.postponeEnterTransition()
        postponed = true
    }

    override fun onStart() {
        super.onStart()

        if (postponed && !startedTransition) {
            // If we're postponed and haven't started a transition yet, we'll delay for a max of 2000ms
            view?.postDelayed(::scheduleStartPostponedTransitions, 2000)
        }
    }

    override fun onStop() {
        super.onStop()
        startedTransition = false
    }

    protected fun scheduleStartPostponedTransitions() {
        if (!startedTransition) {
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
                activity?.startPostponedEnterTransition()
            }
            startedTransition = true
        }
    }

    open fun handleSuccessState(data: Result?) {
        swipeRefreshLatch.refreshing = false
        errorLayout?.visibleOrGone = false
    }

    open fun handleErrorState(uiState: UiState.Error) {
        swipeRefreshLatch.refreshing = false
        when (uiState) {
            is UiState.Error.PagedError -> {
                handlePagedError()
            }
            is UiState.Error.RefreshError -> {

                hasPendingSnackBar = createSnackBar(parentView, uiState) {
                    viewModel.callRefresh()
                }
                if (!startedTransition) {
                    showPendingSnackBar()
                }
            }
            is UiState.Error.PagedEmptyError -> {
                handlePagedEmptyError()
            }
        }
    }

    private fun showPendingSnackBar() {
        hasPendingSnackBar?.show()
        hasPendingSnackBar = null
    }

    open fun handlePagedError() = Unit

    open fun handlePagedEmptyError() = Unit

    open fun handleLoadMoreState() = Unit

    open fun handleLoadingState() {
        errorLayout?.visibleOrGone = false
        swipeRefreshLatch.refreshing = true

    }

    open fun setupSwipeToRefresh() {
        swipeRefreshLayout = parentView.findViewById(R.id.swipeRefresh)
        swipeRefreshLayout?.let { swipeRefreshLayout ->
            swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorPrimary
            )
            swipeRefreshLatch = ProgressTimeLatch {
                swipeRefreshLayout.isRefreshing = it

            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.callRefresh()
                swipeRefreshLatch.refreshing = true
            }

        }

    }

    private fun setUpAppbarElevation() {
        val appbar: AppBarLayout? = parentView.findViewById(R.id.appbar)
        appbar?.let {
            epoxyRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    appbar.isSelected = recyclerView.canScrollVertically(-1)
                }
            })
        }

    }

    fun setupSharedElement() {
        TransitionInflater.from(context).run {
            sharedElementEnterTransition =
                    inflateTransition(R.transition.fragment_shared_enter)
            sharedElementReturnTransition = null
        }

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                back?.let {
                    back.animate().setStartDelay(850).scaleXBy(1f).scaleYBy(1f).setDuration(450)
                        .start()
                }

            }

            override fun onSharedElementStart(
                sharedElementNames: MutableList<String>?,
                sharedElements: MutableList<View>?,
                sharedElementSnapshots: MutableList<View>?
            ) {
                super.onSharedElementStart(
                    sharedElementNames,
                    sharedElements,
                    sharedElementSnapshots
                )
                back.scaleX = 0.0f
                back.scaleY = 0.0f
            }
        })
    }

    abstract fun getLayoutId(): Int

    fun navController() = findNavController()
}