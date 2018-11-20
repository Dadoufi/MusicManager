package dadoufi.musicmanager.ui.album.favorite


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.doOnNextLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper
import dadoufi.musicmanager.R
import dadoufi.musicmanager.base.PagedFragment
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.extensions.clearDecorations
import dadoufi.musicmanager.extensions.color
import dadoufi.musicmanager.extensions.reObserveForever
import dadoufi.musicmanager.ui.album.AlbumController
import dadoufi.musicmanager.ui.album.AlbumModel_
import dadoufi.musicmanager.util.epoxy.SwipeAwayCallbacks
import kotlinx.android.synthetic.main.fragment_favorite_albums.*
import kotlinx.android.synthetic.main.item_album_favorite.*
import timber.log.Timber


class FavoriteAlbumFragment : PagedFragment<Any, AlbumEntity, FavoriteAlbumViewModel>(),
    AlbumController.Callbacks {

    override val classToken: Class<FavoriteAlbumViewModel>
        get() = FavoriteAlbumViewModel::class.java


    var removableItemDecoration: Pair<Int, ArtistHeadersDecoration?>? = null

    private val observer by lazy {
        Observer<PagedList<AlbumEntity>?>(function = livePagedObserver())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = AlbumController(this, isFavorite = true, context = context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeToDelete()

        title.text = getString(R.string.favorites)

        search.setOnClickListener {
            navController().navigate(R.id.action_show_search_fragment)
        }

    }

    private fun setupSwipeToDelete() {
        val context = requireContext()

        val swipeCallback = object : SwipeAwayCallbacks<AlbumModel_>(
            context.getDrawable(R.drawable.ic_delete)!!,
            context.resources.getDimensionPixelSize(R.dimen.margin_swipe_delete),
            context.color(R.color.swipe_background),
            context.color(R.color.colorAccent)
        ) {
            override fun onSwipeCompleted(
                model: AlbumModel_,
                itemView: View,
                position: Int,
                direction: Int
            ) {
                viewModel.removeFavoriteAlbum(model.id())
            }

            override fun onSwipeStarted(model: AlbumModel_, itemView: View?, adapterPosition: Int) {
                super.onSwipeStarted(model, itemView, adapterPosition)
                epoxyRecyclerView?.let { recyclerView ->
                    removableItemDecoration = null
                    val itemDecorationCount = recyclerView.itemDecorationCount
                    if (itemDecorationCount > 0) {
                        (itemDecorationCount - 1 downTo 0)
                            .filter { recyclerView.getItemDecorationAt(it) !is ItemTouchHelper } //do not remove itemTouchHelper so we can swipe to deleteCompletable
                            .map { i ->
                                (recyclerView.getItemDecorationAt(i) as ArtistHeadersDecoration).headers.filterKeys { it == adapterPosition - 1 }
                                    .map {
                                        Timber.d(it.toString())
                                        removableItemDecoration = Pair(
                                            i,
                                            recyclerView.getItemDecorationAt(i) as ArtistHeadersDecoration
                                        )
                                        recyclerView.removeItemDecorationAt(i)

                                    }
                            }
                    }
                }
            }


            override fun onSwipeReleased(model: AlbumModel_?, itemView: View?) {
                super.onSwipeReleased(model, itemView)
                removableItemDecoration?.second?.let { itemDecoration ->
                    epoxyRecyclerView?.addItemDecoration(
                        itemDecoration,
                        removableItemDecoration!!.first
                    )
                }
            }


        }

        EpoxyTouchHelper.initSwiping(epoxyRecyclerView)
            .let { if (view?.layoutDirection == View.LAYOUT_DIRECTION_RTL) it.right() else it.left() }
            .withTarget(AlbumModel_::class.java)
            .andCallbacks(swipeCallback)


    }

    override fun observeList() {
        viewModel.observer = observer
        viewModel.liveList.reObserveForever(observer)

    }

    private fun livePagedObserver(): (PagedList<AlbumEntity>?) -> Unit {
        return { data ->
            controller.submitList(data)
            epoxyRecyclerView?.let { recyclerView ->

                recyclerView.doOnNextLayout { _ ->
                    recyclerView.clearDecorations()

                    val albums = controller.adapter.copyOfModels
                        .asSequence()
                        .filter { it is AlbumModel_ }
                        .map { (it as AlbumModel_).artistName[0] }
                        .toList()



                    recyclerView.addItemDecoration(
                        ArtistHeadersDecoration(
                            recyclerView.context,
                            albums
                        )
                    )
                }

            }
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_favorite_albums


    override fun onItemClicked(item: AlbumEntity, sharedElementImageView: ImageView) {
        val statusBar = view?.findViewById<View>(android.R.id.statusBarBackground)
        val transitionName = sharedElementImageView.transitionName
        val extras = FragmentNavigatorExtras(
            sharedElementImageView to transitionName, appbar to "appbar"
        )
        navController().navigate(
            FavoriteAlbumFragmentDirections.actionShowAlbumDetailsFragment(
                item,
                transitionName
            ), extras
        )
    }
}
