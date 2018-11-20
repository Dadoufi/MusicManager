package dadoufi.musicmanager.ui.search


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.core.view.postDelayed
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import dadoufi.musicmanager.R
import dadoufi.musicmanager.base.PagedFragment
import dadoufi.musicmanager.data.entities.ArtistEntity
import dadoufi.musicmanager.extensions.onTextChanged
import dadoufi.musicmanager.extensions.visibleOrGone
import kotlinx.android.synthetic.main.fragment_search_artist.*
import kotlinx.android.synthetic.main.view_toolbar_text_input.*

class SearchFragment : PagedFragment<Any, ArtistEntity, SearchViewModel>(),
    ArtistController.Callbacks {


    override val classToken: Class<SearchViewModel>
        get() = SearchViewModel::class.java


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = ArtistController(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(textInputToolbar) {
            searchView.onTextChanged {
                toggleErase(it)
            }
            onDoneButtonPressed = { viewModel.setQuery(searchView.text.toString()) }
            onBackButtonPressed = { findNavController().popBackStack() }
            doOnPreDraw {
                if (!searchView.visibleOrGone) {
                    postDelayed(200) {
                        toggleSearchVisibility()
                    }

                }
            }
        }

    }


    override fun getLayoutId(): Int = R.layout.fragment_search_artist

    override fun retryClicked() {
        viewModel.callLoadMore()
    }

    override fun onItemClicked(item: ArtistEntity, sharedElementImageView: ImageView) {
        val transitionName = sharedElementImageView.transitionName
        val extras = FragmentNavigatorExtras(
            sharedElementImageView to transitionName
        )
        findNavController().navigate(
            SearchFragmentDirections.actionShowTopAlbumFragment(item, transitionName), extras
        )
    }

}