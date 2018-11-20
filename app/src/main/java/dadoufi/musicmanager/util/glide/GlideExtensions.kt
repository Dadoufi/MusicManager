package dadoufi.musicmanager.util.glide

import android.annotation.SuppressLint
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dadoufi.musicmanager.R


@SuppressLint("CheckResult")
@GlideExtension
object GlideExtensions {
    @GlideOption
    @JvmStatic
    fun round(options: RequestOptions, size: Int): RequestOptions {
        return options.circleCrop().override(size)
    }

    @GlideOption
    @JvmStatic
    fun rounded(options: RequestOptions): RequestOptions {
        return options
            .fallback(R.drawable.ic_no_album)
            .error(R.drawable.ic_no_album)
            .transforms(RoundedCorners(16))
    }


    @GlideOption
    @JvmStatic
    fun roundedArtist(options: RequestOptions): RequestOptions {
        return options
            .fallback(R.drawable.ic_no_artist)
            .error(R.drawable.ic_no_artist)
            .transforms(RoundedCorners(16))
    }

}