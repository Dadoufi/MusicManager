package dadoufi.musicmanager.ui.album.top


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import dadoufi.musicmanager.R
import dadoufi.musicmanager.base.PagedFragment
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.extensions.visibleOrGone
import dadoufi.musicmanager.ui.album.AlbumController
import dadoufi.musicmanager.util.glide.GlideApp
import kotlinx.android.synthetic.main.fragment_album_list.*
import kotlinx.android.synthetic.main.include_toolbar_title.*


class TopAlbumFragment : PagedFragment<Any, AlbumEntity, TopAlbumViewModel>(),
    AlbumController.TopAlbumCallbacks {


    override val classToken: Class<TopAlbumViewModel>
        get() = TopAlbumViewModel::class.java


    private lateinit var params: TopAlbumFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        params = TopAlbumFragmentArgs.fromBundle(arguments)
        controller = AlbumController(this, params.artist.name)
        setupSharedElement()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        title.text = params.artist.name

        backdrop.transitionName = params.transitionName
        GlideApp.with(this)
            .load(params.artist.icon)
            .dontAnimate().dontTransform()
            .override(Target.SIZE_ORIGINAL)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    backdrop.setImageDrawable(resource)
                    scheduleStartPostponedTransitions()
                    return true
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    backdrop.setImageResource(R.drawable.ic_broken_image)
                    scheduleStartPostponedTransitions()
                    return true
                }
            })
            .into(backdrop)

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            title.visibleOrGone = verticalOffset == -appBarLayout.height + toolbar.height
        })

        back.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.setId(params.artist.name)


    }


    override fun getLayoutId(): Int = R.layout.fragment_album_list


    override fun onItemClicked(item: AlbumEntity, sharedElementImageView: ImageView) {
        val transitionName = sharedElementImageView.transitionName
        val extras = FragmentNavigatorExtras(
            sharedElementImageView to transitionName
        )
        navController().navigate(
            TopAlbumFragmentDirections.actionShowAlbumDetailsFragment(
                item,
                transitionName
            ), extras
        )
    }

    override fun onSaveClicked(item: AlbumEntity, position: Int) {
        viewModel.updateItem(item)
    }

    override fun retryClicked() {
        viewModel.callLoadMore()
    }

}


