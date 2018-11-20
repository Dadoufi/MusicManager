package dadoufi.musicmanager.util.epoxy.common

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import dadoufi.musicmanager.R
import dadoufi.musicmanager.util.epoxy.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_infinite_loading)
abstract class LoadingModel : EpoxyModelWithHolder<LoadingHolder>() {


    override fun bind(holder: LoadingHolder) {


    }
}


class LoadingHolder : KotlinEpoxyHolder()
