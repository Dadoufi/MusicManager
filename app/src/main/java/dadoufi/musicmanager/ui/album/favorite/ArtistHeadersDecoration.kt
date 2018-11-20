/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dadoufi.musicmanager.ui.album.favorite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Build
import android.text.Layout.Alignment.ALIGN_CENTER
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.res.*
import androidx.core.graphics.withTranslation
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import dadoufi.musicmanager.R
import timber.log.Timber

/**
 * A [RecyclerView.ItemDecoration] which draws sticky headers for a given list of albums.
 */
class ArtistHeadersDecoration(
    context: Context,
    albums: List<Char?>
) : ItemDecoration() {

    private val paint: TextPaint
    private val width: Int
    private val paddingTop: Int

    init {
        val attrs = context.obtainStyledAttributes(
            R.style.Widget_MusicManager_ArtistHeaders,
            R.styleable.ArtistHeader
        )
        paint = TextPaint(ANTI_ALIAS_FLAG).apply {
            color = attrs.getColorOrThrow(R.styleable.ArtistHeader_android_textColor)
            textSize = attrs.getDimensionOrThrow(R.styleable.ArtistHeader_artistTextSize)
            try {
                typeface = ResourcesCompat.getFont(
                    context,
                    attrs.getResourceIdOrThrow(R.styleable.ArtistHeader_android_fontFamily)
                )
            } catch (_: Exception) {
                // ignore
            }
        }
        width = attrs.getDimensionPixelSizeOrThrow(R.styleable.ArtistHeader_android_width)
        paddingTop = attrs.getDimensionPixelSizeOrThrow(R.styleable.ArtistHeader_android_paddingTop)
        attrs.recycle()
    }

    val headers: Map<Int, StaticLayout> =
        indexArtistHeaders(albums).map {
            it.first to createHeader(it.second)
        }.toMap()

    /**
     * Loop over each child and draw any corresponding headers i.e. items who'regex position is a key in
     * [headers]. We also look back to see if there are any headers _before_ the first header we
     * found i.e. which needs to be sticky.
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
        if (headers.isEmpty() || parent.isEmpty()) {
            return
        }
        var earliestFoundHeaderPos = -1
        var prevHeaderTop = Int.MAX_VALUE

        // Loop over each attached view looking for header items.
        // Loop backwards as a lower header can push another higher one upward.
        for (i in parent.childCount - 1 downTo 0) {
            val view = parent.getChildAt(i)
            if (view == null) {
                // This should not be null, but observed null at times.
                // Guard against it to avoid crash and log the state.
                Timber.w(
                    """View is null. Index: $i, childCount: ${parent.childCount},
                        |RecyclerView.State: $state""".trimMargin()
                )
                continue
            }
            val viewTop = view.top + view.translationY.toInt()
            if (view.bottom > 0 && viewTop < parent.height) {
                val position = parent.getChildAdapterPosition(view)
                headers[position]?.let { layout ->
                    paint.alpha = (view.alpha * 255).toInt()
                    val top = (viewTop + paddingTop.times(2))
                        .coerceAtLeast(paddingTop)
                        .coerceAtMost(prevHeaderTop - layout.height)
                    c.withTranslation(y = top.toFloat()) {
                        layout.draw(c)
                    }
                    earliestFoundHeaderPos = position
                    prevHeaderTop = viewTop
                }
            }
        }

        // If no headers found, ensure header of the first shown item is drawn.
        if (earliestFoundHeaderPos < 0) {
            earliestFoundHeaderPos = parent.getChildAdapterPosition(parent[0])
        }

        // Look back over headers to see if a prior item should be drawn sticky.
        for (headerPos in headers.keys.reversed()) {
            if (headerPos < earliestFoundHeaderPos) {
                headers[headerPos]?.let {
                    val top = (prevHeaderTop - it.height).coerceAtMost(paddingTop)
                    c.withTranslation(y = top.toFloat()) {
                        it.draw(c)
                    }
                }
                break
            }
        }
    }


    private fun createHeader(artistInitial: Char): StaticLayout {

        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> StaticLayout.Builder.obtain(
                artistInitial.toString(),
                0,
                1,
                paint,
                width
            )
                .setAlignment(ALIGN_CENTER).setLineSpacing(1f, 0f).setIncludePad(false)
                .build()
            else -> StaticLayout(
                artistInitial.toString(),
                paint,
                width,
                ALIGN_CENTER,
                1f,
                0f,
                false
            )
        }

    }

    private fun indexArtistHeaders(artists: List<Char?>): List<Pair<Int, Char>> {
        return artists
            .asSequence()
            .mapIndexed { index, artist ->
                index to (artist ?: '?')
            }
            .distinctBy { it.second }
            .toList()

    }

}
