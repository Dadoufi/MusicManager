package dadoufi.musicmanager.ui.details


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import dadoufi.musicmanager.R
import dadoufi.musicmanager.base.BaseFragment
import dadoufi.musicmanager.data.entities.AlbumDetailsWithTracksEntity
import dadoufi.musicmanager.extensions.visibleOrGone
import dadoufi.musicmanager.util.glide.GlideApp
import kotlinx.android.synthetic.main.fragment_album_list.*
import kotlinx.android.synthetic.main.include_toolbar_title.*


class AlbumDetailsFragment :
    BaseFragment<AlbumDetailsWithTracksEntity, AlbumDetailsWithTracksEntity, AlbumDetailsViewModel>() {


    override val classToken: Class<AlbumDetailsViewModel>
        get() = AlbumDetailsViewModel::class.java //To change initializer of created properties use File | Settings | File Templates.


    private lateinit var params: AlbumDetailsFragmentArgs
    lateinit var controller: TracksController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        params = AlbumDetailsFragmentArgs.fromBundle(arguments)
        controller = TracksController()
        setupSharedElement()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        title.text = params.album.albumName

        backdrop.transitionName = params.transitionName
        GlideApp.with(this)
            .load(params.album.albumIcon)
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

        with(epoxyRecyclerView) {
            this?.setController(controller)
            this?.adapter = controller.adapter
        }

        back.setOnClickListener { findNavController().popBackStack() }

        viewModel.setAlbumInfoEntries(params.album)

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_album_list
    }

    override fun handleSuccessState(data: AlbumDetailsWithTracksEntity?) {
        super.handleSuccessState(data)
        val tracks = data?.getRelationsSorted()
        val albumInfo = data?.albumDetails
        controller.setData(
            tracks,
            albumInfo,
            data?.getTotalDuration(),
            context
        )

    }


}
